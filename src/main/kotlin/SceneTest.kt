import elements.Actor
import elements.Scene
import elements.primitives.*
import utils.*

class SceneTest: Scene() {
    init {
        val cube = Actor()
        val s = Cube4(Transform4(Vector4(4f, 0f, 0f, 0f), Rotator4(1f, 0f, 1f, 1f)))
        s.setPrimitiveParameters(Vector4(1f, 1f, 1f, 1f), 1f)
        s.operator = MarcherOperator(OperatorType.UNION, 0.8f)
        s.setMaterial("vec3(0,1,0)")
        cube.addPrimitive(s)

        val sph = Actor()
        val sphere = Sphere(Transform4(Vector4(4f, 2f, 0f, 0f)))
        sphere.setPrimitiveParameters(Vector4(1f, 1f, 1f, 1f), 1f)
        sphere.operator = MarcherOperator(OperatorType.UNION, 0.8f)
        sphere.setMaterial("vec3(0,0,1)")
        sph.addPrimitive(sphere)

        val floor = Actor()
        val c = Cube(Transform4(Vector4()))
        c.setPrimitiveParameters(Vector4(4f, 4f, 4f, 4f), 0f)
        floor.addPrimitive(c)
        floor.transform.location = Vector4(5f, 0f, -5f)

        add(floor)
        add(cube)
        //add(sph)
    }
}