package elements

import editor.EditorElement
import editor.UiElements
import imgui.ImGui
import imgui.flag.ImGuiInputTextFlags
import imgui.flag.ImGuiTreeNodeFlags
import imgui.type.ImString
import utils.Transform4

open class Actor: EditorElement {

    var properties: HashMap<String, Array<Any>> = HashMap()

    var transform: Transform4 = Transform4()

    var primitiveObjects: Array<PrimitiveObject> = arrayOf()
    var displayName: String = ""



    fun addPrimitive(p: PrimitiveObject) {
        primitiveObjects += p
    }
    fun getPrimitives() = primitiveObjects



    override fun display() {

        val name = ImString(displayName, 16)
        if (ImGui.inputText("##", name, ImGuiInputTextFlags.AutoSelectAll)) {
            displayName = name.get()
        }
        //ImGui.text(displayName)
        if (ImGui.collapsingHeader("Transform", ImGuiTreeNodeFlags.DefaultOpen)) {
            UiElements.vector4Field(transform.location, "Location")
            UiElements.rotator4Field(transform.rotation, "Rotation")
            UiElements.vector4Field(transform.scale, "Scale")
        }

        UiElements.arrayField(primitiveObjects)
    }


}