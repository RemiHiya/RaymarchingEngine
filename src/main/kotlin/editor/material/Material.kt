package editor.material

import editor.material.nodes.MaterialOutput
import imgui.extension.imnodes.ImNodes
import imgui.extension.imnodes.flag.ImNodesMiniMapLocation

class Material {

    var nodes: Map<Int, MaterialNode> = hashMapOf(Pair(0, MaterialNode(0)), Pair(1, MaterialOutput(1)))

    fun display() {
        ImNodes.beginNodeEditor()
        for (node in nodes.values) {
            node.display()
        }


        ImNodes.miniMap(0.2f, ImNodesMiniMapLocation.BottomRight)
        ImNodes.endNodeEditor()
    }
}