package elements

import api.getPrimitives
import editor.Debug

open class Scene {

    /*
    TODO : Tri des objets : non smooth en premier
     */

    var actors: Array<Actor> = arrayOf()

    val camera = CameraActor()

    init {
        Debug.camera = camera
    }

    fun update(dt: Float) {
        Physics.simulate(this, dt)
        actors.forEach { it.update(dt) }
    }

    fun add(actor: Actor) {
        if (actor.displayName.isEmpty()) {
            actor.displayName = "Actor${actors.size}"
        }
        actors += actor
    }

    fun getObjects(): List<PrimitiveObject> {
        return actors.flatMap { it.getPrimitives().asIterable() }
    }
}