package elements

import editor.EditorElement
import editor.UiElements
import imgui.ImGui
import imgui.type.ImFloat
import imgui.type.ImString
import utils.Transform4
import misc.Property
import utils.Vector4

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
        ImGui.text(displayName)
        UiElements.transform4Field(transform)
        UiElements.arrayField(primitiveObjects)
    }


}