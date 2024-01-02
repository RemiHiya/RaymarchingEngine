package editor.material.nodes

import editor.material.MaterialNode
import editor.material.Pin
import editor.material.PinType
import utils.Color

private val col = Color(108, 90, 173)

class Add(id: Int) : MaterialNode(id) {
    override val name = "Add"
    override val color = col
    override val hoverColor = color * 1.5f
    override val selectColor = color * 1.5f

    override val inputs = mapOf(
        id(0) to Pin("a", PinType.VECTOR3),
        id(1) to Pin("b", PinType.VECTOR3)
    )
    override val outputs = mapOf(id(2) to (Pin("", PinType.VECTOR3) to "(%0 + %1)"))
}

class Subtract(id: Int) : MaterialNode(id) {
    override val name = "Subtract"
    override val color = col
    override val hoverColor = color * 1.5f
    override val selectColor = color * 1.5f

    override val inputs = mapOf(
        id(0) to Pin("a", PinType.VECTOR3),
        id(1) to Pin("b", PinType.VECTOR3)
    )
    override val outputs = mapOf(id(2) to (Pin("", PinType.VECTOR3) to "(%0 - %1)"))
}

class Multiply(id: Int) : MaterialNode(id) {
    override val name = "Multiply"
    override val color = col
    override val hoverColor = color * 1.5f
    override val selectColor = color * 1.5f

    override val inputs = mapOf(
        id(0) to Pin("a", PinType.VECTOR3),
        id(1) to Pin("b", PinType.VECTOR3)
    )
    override val outputs = mapOf(id(2) to (Pin("", PinType.VECTOR3) to "(%0 * %1)"))
}

class Divide(id: Int) : MaterialNode(id) {
    override val name = "Divide"
    override val color = col
    override val hoverColor = color * 1.5f
    override val selectColor = color * 1.5f

    override val inputs = mapOf(
        id(0) to Pin("a", PinType.VECTOR3),
        id(1) to Pin("b", PinType.VECTOR3)
    )
    override val outputs = mapOf(id(2) to (Pin("", PinType.VECTOR3) to "(%0 / %1)"))
}