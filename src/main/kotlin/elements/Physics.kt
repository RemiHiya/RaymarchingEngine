package elements

import api.math.transformBy
import elements.components.PhysicsComponent

class Physics {
    companion object {
        private var objects: MutableList<PhysicsComponent> = mutableListOf()
        private var potentialColliding: MutableSet<Pair<PhysicsComponent, PhysicsComponent>> = mutableSetOf()

        // TODO : Multithreading

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
            objects.forEach {
                it.linearVelocity *= it.friction
                it.angularVelocity *= it.friction
                it.linearVelocity += (it.gravity) * dt
                it.parent.transform.location += it.linearVelocity
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
        }

        private fun resolve() {
        }
    }
}