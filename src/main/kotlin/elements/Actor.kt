package elements

import utils.Transform4
import misc.Property

class Actor {

    var properties: HashMap<String, Array<Any>> = HashMap()


    private var primitives: Array<PrimitiveObject> = arrayOf()
    var displayName: String = ""

    var transform: Transform4 = Transform4()

    @Property("test")
    var location = transform.location

    fun addPrimitive(p: PrimitiveObject) {
        primitives += p
    }
    fun getPrimitives() = primitives


}