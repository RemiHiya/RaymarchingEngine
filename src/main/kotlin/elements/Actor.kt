package elements

import utils.Transform4
import misc.Property

open class Actor {

    var properties: HashMap<String, Array<Any>> = HashMap()

    @Property("Transform")
    var transform: Transform4 = Transform4()

    @Property("Primitives")
    var primitiveObjects: Array<PrimitiveObject> = arrayOf()
    var displayName: String = ""



    fun addPrimitive(p: PrimitiveObject) {
        primitiveObjects += p
    }
    fun getPrimitives() = primitiveObjects


}