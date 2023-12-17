package elements

import editor.Debuggable
import editor.EditorElement
import editor.Gui
import elements.components.Component
import imgui.ImGui
import imgui.flag.ImGuiInputTextFlags
import imgui.flag.ImGuiTreeNodeFlags
import imgui.type.ImString
import utils.Transform4

open class Actor: EditorElement, Debuggable {

    var properties: HashMap<String, Array<Any>> = HashMap()

    var transform: Transform4 = Transform4()

    var components: Array<Component> = arrayOf()
    var displayName: String = ""

    init {
        this.construct()
    }

    open fun construct() {
        components.forEach { it.construct() }
    }

    open fun update(dt: Float) {
        components.forEach { it.update(dt) }
    }

    fun addComponent(component: Component): Component? {
        if (component.singleton) {
            if (components.any { it::class == component::class })
                return null
        }
        component.parent = this
        components += component
        construct()
        return component
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

    override fun debug() {
        components.forEach { (it as? Debuggable)?.debug(transform) }
    }


}