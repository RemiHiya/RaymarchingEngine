package elements

import api.math.*
import com.badlogic.gdx.Gdx
import editor.Debug
import elements.components.PhysicsComponent
import utils.ColliderType
import utils.Rotator4
import utils.Vector3
import utils.Vector4

class Physics {
    companion object {
        /**
         * Structure prévue pour encapsuler les données de collision entre 2 objets.
         * @property location Le centre du volume d'intersection.
         * @property distance La taille maximale du volume d'intersection.
         * @property normals Les normales de collision respectivement sur les 2 objets.
         */
        data class CollisionData(val location: Vector4, val distance: Float, val normals: Pair<Vector4, Vector4>)

        private val objects = mutableListOf<PhysicsComponent>()
        private val potentialColliding = mutableSetOf<Pair<PhysicsComponent, PhysicsComponent>>()
        private val colliding = mutableSetOf<Triple<PhysicsComponent, PhysicsComponent, CollisionData>>()

        // TODO : pas de collision response entre 2 objets statiques
        // TODO : Fix le multithreading

        fun simulateOnThread(scene: Scene, dt: Float) {
            val physicsThread = Thread {
                simulate(scene, dt)
            }
            physicsThread.start()
        }
        fun simulate(scene: Scene, dt: Float) {
            Gdx.app.postRunnable {
                initialize(scene.actors)
                updatePositions(dt)
                preDetect()
                detect()
                resolve()
            }

        }

        private fun initialize(actors: Array<Actor>) {
            objects.clear()
            objects.addAll(actors.map { it.components }.flatMap { it.filterIsInstance<PhysicsComponent>() })
        }

        private fun updatePositions(dt: Float) {
            for (i in objects) {
                if (i.type == ColliderType.STATIC)
                    continue

                i.linearVelocity *= i.friction
                i.angularVelocity *= i.friction
                i.linearVelocity += (i.gravity) * dt * .981f
                i.parent.transform.location += i.linearVelocity
                i.parent.transform.rotation += i.angularVelocity
            }
        }

        /**
         * Génère une liste de paires de tout les objets potentiellement en collision.
         * Deux objets sont potentiellement en collisions si leurs colliders simplifiés (sphère)
         * sont en collision.
         */
        private fun preDetect() {
            potentialColliding.clear()
            for (i in objects.indices) {
                for (j in i+1 until objects.size) {
                    val (a, b) = objects[i] to objects[j]
                    val centerA = a.center.transformBy(a.parent.transform)
                    val centerB = b.center.transformBy(b.parent.transform)
                    // Vérifie si les 2 sphères ne sont pas en collision
                    if ((centerB-centerA).length() > a.radius + b.radius)
                        continue
                    potentialColliding += Pair(a, b)
                }
            }
        }

        private fun detect() {
            colliding.clear()
            for (i in potentialColliding) {
                if (i.first.type == ColliderType.STATIC && i.second.type == ColliderType.STATIC)
                    continue
                val data = collisionData(i.first to i.second)
                if (data.third.distance <= 0) {
                    colliding += data
                }
            }
        }

        /**
         * Génère les données de collision entre 2 objets.
         * @param objects La paire d'objets sur laquelle générer des données.
         * @return Les données de collision, voir [CollisionData].
         */
        private fun collisionData(objects: Pair<PhysicsComponent, PhysicsComponent>): Triple<PhysicsComponent, PhysicsComponent, CollisionData> {
            val a = objects.first
            val b = objects.second
            var distance = Float.MAX_VALUE

            val other = if (a.points.size < b.points.size) b else a
            val self = if (a.points.size < b.points.size) a else b
            val points = self.points.map { (it.first+self.center) to it.second }
            val distFun = other.description

            // Listes permettant de faire la moyenne des points en collision
            val locations = mutableListOf<Vector4>()
            // Stoque les positions locales pour éviter d'avoir à transformer les points après la boucle
            val selfLocations = mutableListOf<Vector4>()
            val otherLocations = mutableListOf<Vector4>()

            for (p in points) {
                val worldPoint = (p.first*.5f).transformBy(self.parent.transform)
                val toOther = rotation(other.parent.transform.rotation.toRadians()).inverse() * (worldPoint - other.parent.transform.location)
                val dist = distFun(toOther) + p.second
                if (dist <= 0) {
                    locations.add(worldPoint)
                    selfLocations.add(p.first)
                    otherLocations.add(toOther)
                    if (dist < distance) {
                        distance = dist
                    }
                }
            }

            if (locations.isEmpty()) {
                return Triple(self, other, CollisionData(Vector4(), 1f, Vector4() to Vector4()))
            }
            var result = CollisionData(
                locations.reduce(Vector4::plus)/locations.size.toFloat(),
                distance,
                Vector4() to Vector4()
            )

            if (result.distance <= 0) {
                val normalA = other.getNormal(otherLocations.reduce(Vector4::plus)/otherLocations.size.toFloat()).rotate(other.parent.transform.rotation)
                val normalB = self.getNormal(selfLocations.reduce(Vector4::plus)/selfLocations.size.toFloat()).rotate(self.parent.transform.rotation)
                result = result.copy(normals = normalA to normalB)
            }
            return Triple(self, other, result)
        }


        private fun resolve() {
            for ((a, b, data) in colliding) {
                if (data.distance > 0)
                    continue
                val massRatio = if (a.type == ColliderType.STATIC) {
                    1f
                } else if (b.type == ColliderType.STATIC) {
                    0f
                } else {
                    a.mass / b.mass
                }
                Debug.drawPoint(Vector3(data.location))
                Debug.drawLine(Vector3(data.location), Vector3(data.location)+Vector3(data.normals.first))
                Debug.drawLine(Vector3(data.location), Vector3(data.location)+Vector3(data.normals.second))

                /*a.parent.transform.location +=
                    data.normals.first.mirrorByNormal(data.normals.second) * (1f-massRatio) * data.distance
                b.parent.transform.location +=
                    data.normals.second.mirrorByNormal(data.normals.first) * massRatio * data.distance*/

                val r1 = data.location - a.parent.transform.location
                val r2 = data.location - b.parent.transform.location

                a.linearVelocity +=
                    data.normals.first.mirrorByNormal(data.normals.second) * (1f-massRatio) * data.distance *1.25f* 1f/r1.length()
                b.linearVelocity +=
                    data.normals.second.mirrorByNormal(data.normals.first) * massRatio * data.distance *1.25f*1f/r2.length()

                // TODO : Fix les rotations
                val impulseTorque1 = (r1.toVector3().cross(data.normals.first.toVector3())) * Vector3(1f, -1f, 1f)
                val impulseTorque2 = (r2.toVector3().cross(data.normals.second.toVector3())) * Vector3(1f, -1f, 1f)

                a.angularVelocity += Rotator4(impulseTorque1.toVector4()) * (1f-massRatio)
                b.angularVelocity -= Rotator4(impulseTorque2.toVector4()) * massRatio

            }


        }
    }
}