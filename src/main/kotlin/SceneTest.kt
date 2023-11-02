import elements.Scene
import elements.primitives.Cube
import elements.primitives.Sphere
import utils.Transform
import utils.Vector3

class SceneTest: Scene() {
    init {

        add(Sphere(Transform(Vector3(0f, 0f, -4f)), ""))
        add(Cube(Transform(Vector3(0f, -5f, -5f))))
    }
}