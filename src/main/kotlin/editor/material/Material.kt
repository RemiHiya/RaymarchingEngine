package editor.material

import editor.material.nodes.MaterialOutput
import imgui.ImGui
import imgui.extension.imnodes.ImNodes
import imgui.extension.imnodes.flag.ImNodesMiniMapLocation
import imgui.flag.ImGuiMouseButton
import imgui.type.ImInt

class Material {

    private var nodes: List<MaterialNode> = mutableListOf(MaterialNode(0), MaterialOutput(1))

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

            if (nodeA.outputs[source]!!.type == nodeB.inputs[target]!!.type) {
                nodeA.outLinks[source] = target
                nodeB.inLinks[target] = source
            }
        }
    }

    private fun isInput(target: Int): Boolean {
        return target in nodes.flatMap { it.inputs.keys.toList() }
    }
}