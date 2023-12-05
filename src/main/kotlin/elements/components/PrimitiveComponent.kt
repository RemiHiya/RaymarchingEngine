package elements.components

import editor.Gui
import elements.PrimitiveObject
import imgui.ImGui
import imgui.flag.ImGuiTreeNodeFlags
import imgui.type.ImString

class PrimitiveComponent(val p: PrimitiveObject): Component() {

    override var displayName = "Primitive Component"


    override fun display() {
        val name = ImString(displayName, 32)
        ImGui.text("${parent.displayName} :")
        ImGui.sameLine()
        if (Gui.textField("##", name)) {
            displayName = name.get()
        }
        ImGui.spacing()
        if (ImGui.collapsingHeader("Transform", ImGuiTreeNodeFlags.DefaultOpen)) {
            Gui.vector4Field(p.v1, "Location")
            Gui.vector4Field(p.v2, "Bounds")
            Gui.rotator4Field(p.ro, "Rotation")
        }

    }
}