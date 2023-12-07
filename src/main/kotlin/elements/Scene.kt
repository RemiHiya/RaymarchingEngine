package elements

import api.getPrimitives

open class Scene {

    /*
    TODO : Tri des objets : non smooth en premier
     */

    private var objects: Array<PrimitiveObject> = arrayOf()
    private var actors: Array<Actor> = arrayOf()

    val camera = CameraActor()

    fun update(dt: Float) {

    }

    fun add(actor: Actor) {
        if (actor.displayName.isEmpty()) {
            actor.displayName = "Actor${actors.size}"
        }
        actors += actor
        for (i in actor.getPrimitives()) {
            add(i)
        }
    }
    fun add(obj: PrimitiveObject) {
        objects += obj
    }

    fun getObjects(): Array<PrimitiveObject> {
        return objects
    }

    fun getActor(index: Int): Actor? {
        return if (index in actors.indices) actors[index]
        else null
    }

    fun getActors() = actors

}