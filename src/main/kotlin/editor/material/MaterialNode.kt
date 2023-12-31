package editor.material

import imgui.ImGui
import imgui.extension.imnodes.ImNodes
import imgui.extension.imnodes.flag.ImNodesColorStyle
import imgui.extension.imnodes.flag.ImNodesPinShape
import utils.Color
import java.lang.Integer.min

// TODO : class abstraite
open class MaterialNode(private val id: Int) {
    open val name = "Node name"
    open val color = Color(11, 109, 191)
    open val hoverColor = Color(11, 109, 191) + 50
    open val selectColor = Color(11, 109, 191) + 50

    open val inputs: List<Pin> = listOf(Pin(id(0), "test pin", PinType.FLOAT))
    open val outputs: List<Pin> = listOf(Pin(id(1), "test out", PinType.INT))

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
            displayInput(inputs[i])
            ImGui.sameLine()
            ImGui.dummy(20f, 0f)
            ImGui.sameLine()
            displayOutput(outputs[i])
        }

        for (i in minSize until inputs.size) {
            displayInput(inputs[i])
        }

        for (i in minSize until outputs.size) {
            displayOutput(outputs[i])
        }

        ImNodes.endNode()

        ImNodes.popColorStyle()
        ImNodes.popColorStyle()
        ImNodes.popColorStyle()
    }

    private fun displayInput(pin: Pin) {
        ImNodes.pushColorStyle(ImNodesColorStyle.Pin, pin.type.color.toInt())
        ImNodes.pushColorStyle(ImNodesColorStyle.PinHovered, (pin.type.color + 50).toInt())
        ImNodes.beginInputAttribute(pin.id, ImNodesPinShape.CircleFilled)
        ImGui.text(pin.name)
        ImNodes.endInputAttribute()
        ImNodes.popColorStyle()
        ImNodes.popColorStyle()
    }

    private fun displayOutput(pin: Pin) {
        ImNodes.pushColorStyle(ImNodesColorStyle.Pin, pin.type.color.toInt())
        ImNodes.pushColorStyle(ImNodesColorStyle.PinHovered, (pin.type.color + 50).toInt())
        ImNodes.beginOutputAttribute(pin.id, ImNodesPinShape.CircleFilled)
        ImGui.text(pin.name)
        ImNodes.endOutputAttribute()
        ImNodes.popColorStyle()
        ImNodes.popColorStyle()
    }
}