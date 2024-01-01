package editor.material

import editor.material.nodes.Add
import editor.material.nodes.MaterialOutput
import editor.material.nodes.Subtract
import editor.material.nodes.Vec3
import imgui.ImGui
import imgui.extension.imnodes.ImNodes
import imgui.extension.imnodes.flag.ImNodesMiniMapLocation
import imgui.flag.ImGuiMouseButton
import imgui.type.ImInt

class Material {

    private var nodes: List<MaterialNode> = mutableListOf(MaterialOutput(0), Vec3(1), Vec3(2), Add(3), Subtract(4))

    private val linkA = ImInt()
    private val linkB = ImInt()

    fun display() {
        ImNodes.beginNodeEditor()
        for (node in nodes) {
            node.display()
        }

        var link = 1
        for (i in nodes.map { it.inLinks }) {
            for (j in i) {
                link++
                ImNodes.link(link, j.value, j.key)
            }
        }

        if (ImGui.isMouseClicked(ImGuiMouseButton.Right)) {
            // Menu contextuel
        }


        ImNodes.miniMap(0.2f, ImNodesMiniMapLocation.BottomRight)
        ImNodes.endNodeEditor()


        if (ImNodes.isLinkCreated(linkA, linkB)) {
            val tmp = isInput(linkA.get())
            val source = if (!tmp) linkA.get() else linkB.get()
            val target = if (tmp) linkA.get() else linkB.get()

            val nodeA = nodes.find { it.outputs.contains(source) }!!
            val nodeB = nodes.find { it.inputs.contains(target) }!!

            if (nodeA.outputs[source]!!.first.type == nodeB.inputs[target]!!.type) {
                nodeA.outLinks[source] = target
                nodeB.inLinks[target] = source
                println(generateCode())
            }
        }
    }

    private fun isInput(target: Int): Boolean {
        return target in nodes.flatMap { it.inputs.keys.toList() }
    }

    fun generateCode(): String {
        val outputNode = nodes.find { it is MaterialOutput } as? MaterialOutput
            ?: return "// 404 Output Node not found."
        return getInputCode(outputNode.inputs.toList()[0].first)
    }

    private fun getInputCode(inputId: Int): String {
        val outputId = (nodes.find { inputId in it.inLinks } ?: return "").inLinks[inputId]
        val outNode = (nodes.find { outputId in it.outputs } ?: return "")
        val outCode = (outNode.outputs[outputId]?: return "").second
        val pattern = "%(\\d+)".toRegex()

        return pattern.replace(outCode) { matchResult ->
            val num = matchResult.groupValues[1].toInt()
            getInputCode(outNode.inputs.toList()[num].first)
        }

    }

}