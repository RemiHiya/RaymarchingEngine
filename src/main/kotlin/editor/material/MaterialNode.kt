package editor.material

import imgui.ImGui
import imgui.extension.imnodes.ImNodes
import imgui.extension.imnodes.flag.ImNodesColorStyle
import imgui.extension.imnodes.flag.ImNodesPinShape
import utils.Color
import java.lang.Integer.min

// TODO : class abstraite
open class MaterialNode(val id: Int) {
    open val name = "Node name"
    open val color = Color(11, 109, 191)
    open val hoverColor = Color(11, 109, 191) + 50
    open val selectColor = Color(11, 109, 191) + 50

    open val inputs: Map<Int, Pin> = mapOf(id(0) to Pin("test pin", PinType.VECTOR3))
    open val outputs: Map<Int, Pair<Pin, String>> = mapOf(
        id(1) to (Pin("test out", PinType.VECTOR3) to "%0, A"),
        id(2) to (Pin("test out2", PinType.VECTOR3) to "%0, B"))

    var inLinks = mutableMapOf<Int, Int>()
    var outLinks = mutableMapOf<Int, Int>()

    protected fun id(index: Int): Int {
        return ImGui.getID("pin_${name}_${id}_$index")
    }

    open fun getCode() = ""

    open fun display() {
        ImNodes.pushColorStyle(ImNodesColorStyle.TitleBar, color.toInt())
        ImNodes.pushColorStyle(ImNodesColorStyle.TitleBarHovered, hoverColor.toInt())
        ImNodes.pushColorStyle(ImNodesColorStyle.TitleBarSelected, selectColor.toInt())

        ImNodes.beginNode(id)

        ImNodes.beginNodeTitleBar()
        ImGui.text(name)
        ImNodes.endNodeTitleBar()

        val minSize = min(inputs.size, outputs.size)

        for (i in 0 until minSize) {
            displayInput(inputs.keys.toList()[i], inputs.values.toList()[i])
            ImGui.sameLine()
            ImGui.dummy(20f, 0f)
            ImGui.sameLine()
            displayOutput(outputs.keys.toList()[i], outputs.values.map { it.first }.toList()[i])
        }

        for (i in minSize until inputs.size) {
            displayInput(inputs.keys.toList()[i], inputs.values.toList()[i])
        }

        for (i in minSize until outputs.size) {
            displayOutput(outputs.keys.toList()[i], outputs.values.map { it.first }.toList()[i])
        }

        ImNodes.endNode()

        ImNodes.popColorStyle()
        ImNodes.popColorStyle()
        ImNodes.popColorStyle()
    }

    private fun displayInput(id: Int, pin: Pin) {
        ImNodes.pushColorStyle(ImNodesColorStyle.Pin, pin.type.color.toInt())
        ImNodes.pushColorStyle(ImNodesColorStyle.PinHovered, (pin.type.color + 50).toInt())
        ImNodes.beginInputAttribute(id, ImNodesPinShape.CircleFilled)
        ImGui.text(pin.name)
        ImNodes.endInputAttribute()
        ImNodes.popColorStyle()
        ImNodes.popColorStyle()
    }

    private fun displayOutput(id: Int, pin: Pin) {
        ImNodes.pushColorStyle(ImNodesColorStyle.Pin, pin.type.color.toInt())
        ImNodes.pushColorStyle(ImNodesColorStyle.PinHovered, (pin.type.color + 50).toInt())
        ImNodes.beginOutputAttribute(id, ImNodesPinShape.CircleFilled)
        ImGui.text(pin.name)
        ImNodes.endOutputAttribute()
        ImNodes.popColorStyle()
        ImNodes.popColorStyle()
    }
}