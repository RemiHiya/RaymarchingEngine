import elements.Scene
import elements.primitives.*
import utils.MarcherOperator
import utils.OperatorType
import utils.Transform4
import utils.Vector4

class SceneTest: Scene() {
    init {
        val s = Sphere4(Transform4(Vector4(4f, 0f, 0f, 0f)), "")
        s.setPrimitiveParameters(Vector4(), 1f)
        s.operator = MarcherOperator(OperatorType.UNION, 0.8f)

        val c = Cube(Transform4(Vector4(5f, 0f, -5f)))
        c.setPrimitiveParameters(Vector4(4f, 4f, 4f, 4f), 0f)

        add(c)
        add(s)
    }
}