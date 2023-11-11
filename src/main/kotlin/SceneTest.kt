import elements.Scene
import elements.primitives.*
import utils.*

class SceneTest: Scene() {
    init {
        val s = Cube(Transform4(Vector4(4f, 0f, 0f, 0f), Rotator4(0f, 0f, 0f, 0f)))
        s.setPrimitiveParameters(Vector4(1f, 1f, 1f, 1f), 1f)
        s.operator = MarcherOperator(OperatorType.UNION, 0.8f)
        s.setMaterial("vec3(0,1,0)")

        val c = Cube(Transform4(Vector4(5f, 0f, -5f)))
        c.setPrimitiveParameters(Vector4(4f, 4f, 4f, 4f), 0f)

        add(c)
        add(s)
    }
}