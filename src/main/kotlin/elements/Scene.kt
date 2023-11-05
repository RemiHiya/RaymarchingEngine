package elements

open class Scene {

    /*
    TODO : Tri des objets : non smooth en premier
     */

    private var objects: Array<PrimitiveObject?> = arrayOfNulls<PrimitiveObject>(0)

    fun add(obj: PrimitiveObject) {
        objects += obj
    }

    fun getObjects(): Array<PrimitiveObject?> {
        return objects
    }

}