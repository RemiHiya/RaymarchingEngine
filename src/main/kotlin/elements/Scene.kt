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

    fun getActor(index: Int): Actor? {
        return if (index in actors.indices) actors[index]
        else null
    }

    //fun getActors() = actors

}