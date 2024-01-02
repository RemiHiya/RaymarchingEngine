package editor.material.nodes

import editor.material.MaterialNode
import editor.material.Pin
import editor.material.PinType
import utils.Color

val inputCol = Color(158, 49, 60)

@Suppress("unused")
class InputTime(id: Int) : MaterialNode(id) {
    override val name = "Input Time"
    override val color = inputCol
    override val hoverColor = color * 1.5f
    override val selectColor = color * 1.5f

    override val aliases = listOf("time")

    override val inputs = mapOf<Int, Pin>()
    override val outputs = mapOf(id(0) to (Pin("", PinType.FLOAT) to "u_time"))
}