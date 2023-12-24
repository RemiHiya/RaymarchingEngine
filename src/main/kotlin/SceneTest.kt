import elements.Actor
import elements.Scene
import elements.components.PhysicsComponent
import elements.components.PrimitiveComponent
import elements.primitives.*
import utils.*

class SceneTest: Scene() {
    init {
        val cube = Actor()
        val s = Cube4(Transform4(Vector4(0f, 0f, 0f, 0f), Rotator4(0f, 0f, 0f, 0f)))
        s.setPrimitiveParameters(Vector4(1f, 1f, 1f, 1f), 1f)
        s.operator = MarcherOperator(OperatorType.UNION, 0f)
        s.setMaterial("vec3(0,1,0)")
        cube.addComponent(PrimitiveComponent(s))
        cube.transform.location = Vector4(5f, 0f, 0f)

        val sph = Actor()
        val sphere = Sphere(Transform4())
        sphere.setPrimitiveParameters(Vector4(1f, 1f, 1f, 1f), 1f)
        sphere.operator = MarcherOperator(OperatorType.UNION, 0.8f)
        sphere.setMaterial("vec3(0,0,1)")
        sph.addComponent(PrimitiveComponent(sphere))
        sph.transform.location = Vector4(5f, 0f, 3f)

        val floor = Actor()
        val c = Cube(Transform4(Vector4()))
        c.setPrimitiveParameters(Vector4(4f, 4f, 4f, 4f), 0f)
        val physics = PhysicsComponent()
        physics.type = ColliderType.STATIC
        floor.addComponent(PrimitiveComponent(c))
        floor.addComponent(physics)
        floor.transform.location = Vector4(5f, 0f, -5f)

        add(floor)
        add(cube)
        add(sph)
    }
}