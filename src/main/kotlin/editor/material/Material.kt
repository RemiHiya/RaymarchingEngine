package editor.material

import editor.material.nodes.MaterialOutput
import imgui.ImGui
import imgui.extension.imnodes.ImNodes
import imgui.extension.imnodes.flag.ImNodesMiniMapLocation
import imgui.flag.ImGuiMouseButton
import imgui.type.ImInt


class Material {

    private var nodes: Map<Int, MaterialNode> = hashMapOf(Pair(0, MaterialNode(0)), Pair(1, MaterialOutput(1)))

    private val linkA = ImInt(-362346692)
    private val linkB = ImInt(-1344809183)

    fun display() {
        ImNodes.beginNodeEditor()
        for (node in nodes.values) {
            node.display()
        }

        var link = 1
        for (i in nodes.values.map { it.inLinks }) {
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

            nodes.values.find { it.outputs.contains(source) }!!.outLinks[source] = target
            nodes.values.find { it.inputs.contains(target) }!!.inLinks[target] = source
        }
    }

    private fun isInput(target: Int): Boolean {
        return target in nodes.values.flatMap { it.inputs.keys.toList() }
    }
}