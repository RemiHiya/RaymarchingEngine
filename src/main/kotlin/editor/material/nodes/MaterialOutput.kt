package editor.material.nodes

import editor.material.MaterialNode
import editor.material.Pin
import editor.material.PinType
import utils.Color

class MaterialOutput(id: Int) : MaterialNode(id) {
    override val name = "Material Output"
    override val color = Color(124, 42, 49)
    override val hoverColor = color * 1.5f
    override val selectColor = color * 1.5f

    override val inputs = listOf(
        Pin(id(0), "Color", PinType.VECTOR3),
        Pin(id(1), "Displacement", PinType.VECTOR3)
    )

    override val outputs = listOf<Pin>()
}