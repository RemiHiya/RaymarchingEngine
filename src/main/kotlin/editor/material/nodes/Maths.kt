package editor.material.nodes

import editor.material.MaterialNode
import editor.material.Pin
import editor.material.PinType
import utils.Color

val mathsCol = Color(73, 135, 175)

@Suppress("unused")
class Sine(id: Int) : MaterialNode(id) {
    override val name = "Sine"
    override val color = mathsCol
    override val hoverColor = color * 1.5f
    override val selectColor = color * 1.5f

    override val aliases = listOf("sin", "sine")

    override val inputs = mapOf(id(0) to Pin("", PinType.FLOAT))
    override val outputs = mapOf(id(1) to (Pin("", PinType.VECTOR3) to "vec3(sin(%0),0,0)"))
}