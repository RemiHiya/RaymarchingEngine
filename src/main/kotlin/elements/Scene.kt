package elements

open class Scene {

    /*
    TODO : Tri des objets : non smooth en premier
     */

    private var objects: Array<PrimitiveObject> = arrayOf()

    fun add(obj: PrimitiveObject) {
        objects += obj
    }

    fun getObjects(): Array<PrimitiveObject> {
        return objects
    }

}