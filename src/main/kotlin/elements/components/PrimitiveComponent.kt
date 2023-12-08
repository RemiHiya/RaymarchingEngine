package elements.components

import editor.Gui
import elements.PrimitiveObject
import imgui.ImGui
import imgui.flag.ImGuiTreeNodeFlags
import imgui.type.ImFloat
import imgui.type.ImInt
import imgui.type.ImString
import utils.OperatorType

class PrimitiveComponent(): Component() {

    var primitive: PrimitiveObject? = null

    constructor(primitive: PrimitiveObject) : this() {
        this.primitive = primitive
    }

    override var displayName = "Primitive Component"


    override fun display() {
        val name = ImString(displayName, 32)
        ImGui.text("${parent.displayName} :")
        ImGui.sameLine()
        if (Gui.textField("##", name)) {
            displayName = name.get()
        }
        ImGui.spacing()
        val p = primitive
        if (p != null) {
            if (ImGui.collapsingHeader("Transform", ImGuiTreeNodeFlags.DefaultOpen)) {
                ImGui.indent()
                Gui.useColumn()
                Gui.vector4Field(p.v1, "Location")
                Gui.vector4Field(p.v2, "Bounds")
                Gui.rotator4Field(p.ro, "Rotation")
                Gui.stopColumn()
                ImGui.unindent()
            }
            ImGui.spacing()
            if (ImGui.collapsingHeader("Operator", ImGuiTreeNodeFlags.DefaultOpen)) {
                ImGui.indent()
                Gui.useColumn()
                val op = ImInt(p.operator.operator.ordinal)
                if (Gui.enumField<OperatorType>("Operator", op)) {
                    p.operator.operator = OperatorType.values()[op.get()]
                }

                val smooth = ImFloat(p.operator.smoothness)
                if (Gui.floatField(smooth, "Smoothness")) {
                    p.operator.smoothness = smooth.get()
                }
                Gui.stopColumn()
                ImGui.unindent()
            }
        }



    }
}