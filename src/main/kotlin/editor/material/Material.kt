package editor.material

import editor.material.nodes.MaterialOutput
import imgui.ImGui
import imgui.extension.imnodes.ImNodes
import imgui.extension.imnodes.flag.ImNodesMiniMapLocation
import imgui.flag.ImGuiFocusedFlags
import imgui.flag.ImGuiMouseButton
import imgui.type.ImInt
import imgui.type.ImString
import org.reflections.Reflections
import kotlin.reflect.full.primaryConstructor

class Material {

    private var nodes: MutableList<MaterialNode> = mutableListOf(MaterialOutput(0))

    private val linkA = ImInt()
    private val linkB = ImInt()

    private val search = ImString(32)

    fun display() {
        ImNodes.beginNodeEditor()

        var link = 1
        for (i in nodes.map { it.inLinks }) {
            for (j in i) {
                link++
                ImNodes.link(link, j.value, j.key)
            }
        }

        val popup = ImGui.isMouseClicked(ImGuiMouseButton.Right) &&
                ImNodes.isEditorHovered() &&
                ImGui.isWindowFocused(ImGuiFocusedFlags.RootAndChildWindows)
        if (popup) {
            ImGui.openPopup("add node")
        }

        if (ImGui.beginPopup("add node")) {
            ImGui.text("Add node")
            // Focus du clavier sur la barre de recherche quand on ouvre le pop-up (ne s'exécute qu'une fois)
            if (popup)
                ImGui.setKeyboardFocusHere()
            ImGui.inputText("##input_search", search)
            val pos = ImGui.getMousePosOnOpeningCurrentPopup()

            // Filtre les sous classes de MaterialNode en fonction de leur aliases
            val reflections = Reflections(MaterialNode::class.java.`package`.name)
            val subclasses = reflections.getSubTypesOf(MaterialNode::class.java).map { it.kotlin }
                .filter { materialNodeClass ->
                    val materialNodeInstance = materialNodeClass.constructors.first().call(0)
                    val aliases = materialNodeInstance.aliases
                    (aliases+materialNodeInstance.name).any { sub -> sub.contains(search.get(), true) }
                }
            for (i in subclasses) {
                if (ImGui.menuItem(i.simpleName)) {
                    val new = i.primaryConstructor!!.call(nodes.size)
                    nodes.add(new)
                    ImNodes.setNodeScreenSpacePos(new.id, pos.x, pos.y)
                    search.set("")
                }
            }
            ImGui.endPopup()
        }

        for (node in nodes) {
            node.display()
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