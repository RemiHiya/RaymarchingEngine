package elements

import api.math.rotation
import api.math.times
import api.math.transformBy
import elements.components.PhysicsComponent
import utils.ColliderType
import utils.Vector4

class Physics {
    companion object {
        private var objects: MutableList<PhysicsComponent> = mutableListOf()
        private var potentialColliding: MutableSet<Pair<PhysicsComponent, PhysicsComponent>> = mutableSetOf()
        private var colliding: MutableSet<Pair<
                Pair<PhysicsComponent, PhysicsComponent>,
                Pair<Vector4, Float>>> = mutableSetOf()

        // TODO : pas de collision response entre 2 objets statiques

        fun simulateOnThread(scene: Scene, dt: Float) {
            val physicsThread = Thread {
                simulate(scene, dt)
            }
            physicsThread.start()
        }
        fun simulate(scene: Scene, dt: Float) {
            initialize(scene.actors)
            updatePositions(dt)
            preDetect()
            detect()
            resolve()
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
            // TODO : remplir le set "colliding" avec les collisions data
        }

        private fun collisionData(objects: Pair<PhysicsComponent, PhysicsComponent>): Pair<Vector4, Float> {
            var closestPoints: Pair<Vector4, Float> = Pair(Vector4(), 0f)
            var minDistance = Float.MAX_VALUE
            val a = objects.first
            val b = objects.second

            val other = if (a.points.size < b.points.size) b else a
            val self = if (a.points.size < b.points.size) a else b
            val points = self.points.map { (it.first+self.center) to it.second }
            val distFun = other.description

            for (p in points) {
                val worldPoint = p.first.transformBy(self.parent.transform)
                val toOther = rotation(other.parent.transform.rotation.toRadians()).inverse() * (worldPoint - other.parent.transform.location)
                val tmp = distFun(toOther) + 1f
                if (tmp < minDistance) {
                    minDistance = tmp
                    closestPoints = worldPoint to tmp
                }
            }
            return closestPoints
        }

        private fun resolve() {
        }
    }
}