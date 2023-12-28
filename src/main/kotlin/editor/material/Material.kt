package editor.material

import imgui.ImGui
import imgui.extension.imnodes.ImNodes
import imgui.extension.imnodes.flag.ImNodesMiniMapLocation
import imgui.extension.imnodes.flag.ImNodesPinShape

class Material {

    var nodes = mutableListOf<MaterialNode>(MaterialNode(0), MaterialNode(1))

    fun display() {
        ImNodes.beginNodeEditor()
        for (node in nodes) {
            ImNodes.beginNode(node.id)

            ImNodes.beginNodeTitleBar()
            ImGui.text(node.name)
            ImNodes.endNodeTitleBar()

            ImNodes.beginInputAttribute(node.id, ImNodesPinShape.CircleFilled)
            ImGui.text("In")
            ImNodes.endInputAttribute()

            ImNodes.endNode()
        }




        ImNodes.miniMap(0.2f, ImNodesMiniMapLocation.BottomRight)
        ImNodes.endNodeEditor()
    }
}