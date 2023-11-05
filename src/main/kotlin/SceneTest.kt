import elements.Scene
import elements.primitives.Cube
import elements.primitives.Sphere
import elements.primitives.Sphere4
import utils.Transform4
import utils.Vector4

class SceneTest: Scene() {
    init {
        val s = Sphere4(Transform4(Vector4(4f, 0f, 0f, 0f)), "")
        s.setPrimitiveParameters(Vector4(), 1f)

        val c = Cube(Transform4(Vector4(5f, 0f, -5f)))
        c.setPrimitiveParameters(Vector4(4f, 4f, 4f, 4f), 0f)

        add(s)
        add(c)
    }
}