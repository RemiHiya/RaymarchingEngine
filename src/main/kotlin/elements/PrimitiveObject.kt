package elements

import utils.Transform

abstract class PrimitiveObject(private var transform: Transform, material: String): Primitive(material) {

    fun getTransform() = transform

}