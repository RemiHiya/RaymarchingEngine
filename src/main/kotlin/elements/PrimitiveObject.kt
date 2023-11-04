package elements

import utils.Transform4
import utils.Vector4

abstract class PrimitiveObject(private var transform: Transform4, material: String): Primitive(material) {

    fun getTransform() = transform

    init {
        v1 = transform.location
        v2 = Vector4(1f, 1f, 1f, 1f)
        extra = 0f
    }

}