package elements

import utils.Transform4
import misc.Property

open class Actor {

    var properties: HashMap<String, Array<Any>> = HashMap()


    private var primitives: Array<PrimitiveObject> = arrayOf()
    var displayName: String = ""

    @Property("Transform")
    var transform: Transform4 = Transform4()

    fun addPrimitive(p: PrimitiveObject) {
        primitives += p
    }
    fun getPrimitives() = primitives


}