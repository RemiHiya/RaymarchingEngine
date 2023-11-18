package elements

import utils.Transform4

class Actor {

    private var primitives: Array<PrimitiveObject> = arrayOf()
    lateinit var displayName: String

    var transform: Transform4 = Transform4()


    fun addPrimitive(p: PrimitiveObject) {
        primitives += p
    }
    fun getPrimitives() = primitives


}