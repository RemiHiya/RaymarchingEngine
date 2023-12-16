package elements.components

import editor.EditorElement
import editor.Gui
import elements.Actor
import imgui.ImGui
import imgui.type.ImString

abstract class Component: EditorElement {

    lateinit var parent: Actor
    abstract var displayName: String

    abstract fun construct()

    override fun display() {
        val name = ImString(displayName, 32)
        ImGui.text("${parent.displayName} :")
        ImGui.sameLine()
        if (Gui.textField("##", name)) {
            displayName = name.get()
        }
        ImGui.sameLine()
        Gui.helpMarker("Instance of ${this::class.simpleName} \nFrom ${this::class.qualifiedName}")
        javaClass.canonicalName
        if (ImGui.button("Reconstruct"))
            construct()
        ImGui.spacing()
    }
}