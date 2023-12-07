package elements

import editor.EditorElement
import editor.Gui
import elements.components.Component
import elements.components.PrimitiveComponent
import imgui.ImGui
import imgui.flag.ImGuiInputTextFlags
import imgui.flag.ImGuiTreeNodeFlags
import imgui.type.ImString
import utils.Transform4

open class Actor: EditorElement {

    var properties: HashMap<String, Array<Any>> = HashMap()

    var transform: Transform4 = Transform4()

    var components: Array<Component> = arrayOf()
    var displayName: String = ""

    fun addComponent(component: Component): Component {
        component.parent = this
        components += component
        return component
    }

    fun getPrimitives(): Array<PrimitiveObject> {
        var tmp: Array<PrimitiveObject> = arrayOf()
        for (i in components) {
            if (i is PrimitiveComponent) {
                tmp += i.p
            }
        }
        return tmp
    }



    override fun display() {

        val name = ImString(displayName, 32)
        if (ImGui.inputText("##", name, ImGuiInputTextFlags.AutoSelectAll)) {
            displayName = name.get()
        }
        ImGui.spacing()
        if (ImGui.collapsingHeader("Transform", ImGuiTreeNodeFlags.DefaultOpen)) {
            ImGui.indent()
            Gui.useColumn()
            Gui.vector4Field(transform.location, "Location")
            Gui.rotator4Field(transform.rotation, "Rotation")
            Gui.vector4Field(transform.scale, "Scale")
            Gui.stopColumn()
            ImGui.unindent()
        }

    }


}