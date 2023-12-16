package elements

import editor.Debuggable
import utils.MarcherOperator
import utils.OperatorType
import utils.Transform4
import utils.Vector4

abstract class PrimitiveObject(transform: Transform4): Primitive(), Debuggable {

    init {
        v1 = transform.location
        v2 = Vector4(1f, 1f, 1f, 1f)
        ro = transform.rotation
        extra = 1f
        operator = MarcherOperator(OperatorType.UNION, 0f)
    }

}