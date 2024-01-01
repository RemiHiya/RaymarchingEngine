package editor.material.nodes

import editor.material.MaterialNode
import editor.material.Pin
import editor.material.PinType
import utils.Color
import utils.Vector3

private val col = Color(154, 92, 49)

class Vec3(id: Int) : MaterialNode(id) {
    private val value = Vector3()

    override val name = "Vector 3"
    override val color = col
    override val hoverColor = color * 1.5f
    override val selectColor = color * 1.5f

    override val inputs = mapOf<Int, Pin>()
    override val outputs = mapOf(
        id(0) to (Pin("rgb", PinType.VECTOR3) to "vec3(${value.x}, ${value.y}, ${value.z})"),
        id(1) to (Pin("r", PinType.FLOAT) to "${value.x}"),
        id(2) to (Pin("g", PinType.FLOAT) to "${value.y}"),
        id(3) to (Pin("b", PinType.FLOAT) to "${value.z}")
    )
}