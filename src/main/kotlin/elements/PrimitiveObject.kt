package elements

import utils.*

abstract class PrimitiveObject(private var transform: Transform4, material: String): Primitive(material) {

    fun getTransform() = transform

    init {
        v1 = transform.location
        v2 = Vector4(1f, 1f, 1f, 1f)
        extra = 0f
        operator = MarcherOperator(OperatorType.UNION, 0f)
    }

}