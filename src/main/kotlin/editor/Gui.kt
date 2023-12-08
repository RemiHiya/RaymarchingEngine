package editor

import imgui.ImGui
import imgui.flag.ImGuiInputTextFlags
import imgui.flag.ImGuiTreeNodeFlags
import imgui.type.ImFloat
import imgui.type.ImInt
import imgui.type.ImString
import utils.Rotator4
import utils.Transform4
import utils.Vector4
import kotlin.reflect.KProperty1

class Gui {

    companion object {

        fun useColumn() {
            ImGui.alignTextToFramePadding()
            ImGui.columns(2, "global_columns", true)
            ImGui.setColumnOffset(1, ImGui.getColumnOffset(1).coerceAtLeast(50f))
        }
        fun useColumn(id: String) {
            ImGui.alignTextToFramePadding()
            ImGui.columns(2, id, true)
            ImGui.setColumnOffset(1, ImGui.getColumnOffset(1).coerceAtLeast(50f))
        }
        fun stopColumn() {
            ImGui.columns(1)
        }
        fun startInput(name: String) {
            ImGui.nextColumn()
            ImGui.pushID(name)
            ImGui.pushItemWidth(-1f)
            //ImGui.setColumnOffset(-1, labelWidth)
        }
        fun endInput() {
            ImGui.popItemWidth()
            ImGui.popID()
            ImGui.nextColumn()
        }

        fun helpMarker(desc: String) {
            ImGui.textDisabled("(?)")
            if (ImGui.isItemHovered())
                ImGui.setTooltip(desc)
        }
        fun floatField(value: ImFloat, name: String): Boolean {
            ImGui.text(name)
            startInput(name)
            val out = ImGui.inputFloat("##", value)
            endInput()
            return out
        }
        fun textField(label: String, value: ImString): Boolean {
            return ImGui.inputText(label, value, ImGuiInputTextFlags.AutoSelectAll or ImGuiInputTextFlags.EnterReturnsTrue)
        }
        fun stringField(value: ImString, name: String): Boolean {
            ImGui.text(name)
            startInput(name)
            val out = ImGui.inputText("##", value)
            endInput()
            return out
        }
        fun vector4Field(value: Vector4, name: String) {
            ImGui.text(name)
            startInput(name)
            val tmp = floatArrayOf(value.x, value.y, value.z, value.w)
            if (ImGui.inputFloat4("##", tmp)) {
                value.x = tmp[0]
                value.y = tmp[1]
                value.z = tmp[2]
                value.w = tmp[3]
            }
            endInput()
        }
        fun rotator4Field(value: Rotator4, name: String) {
            ImGui.text(name)
            startInput(name)
            val tmp = floatArrayOf(value.roll, value.pitch, value.yaw, value.w)
            if (ImGui.inputFloat4("##", tmp)) {
                value.roll = tmp[0]
                value.pitch = tmp[1]
                value.yaw = tmp[2]
                value.w = tmp[3]
            }
            endInput()
        }
        fun transform4Field(value: Transform4, name: String) {
            val isTransformOpen = ImGui.treeNodeEx(name, ImGuiTreeNodeFlags.DefaultOpen)

            if (isTransformOpen) {
                vector4Field(value.location, "Location")
                rotator4Field(value.rotation, "Rotation")
                vector4Field(value.scale, "Scale")
                ImGui.treePop()
            }
        }

        fun structInput(value: Any, name: String) {
            val isNodeOpen = ImGui.treeNodeEx(name)

            if (isNodeOpen) {
                val properties = value::class.members.filterIsInstance<KProperty1<Any, *>>()
                for (property in properties) {
                    val p = property.getter.call(value) ?: continue
                    if (p is Vector4)
                        vector4Field(p, property.name)
                    if (p is Rotator4)
                        rotator4Field(p, property.name)

                }
                ImGui.treePop()
            }
        }

        inline fun <reified T: Any> arrayField(value: Array<T>) {
            if (ImGui.treeNodeEx("${T::class.simpleName} Array", ImGuiTreeNodeFlags.DefaultOpen)) {
                for (i in value.indices) {
                    switcherInput(value[i], "Array[$i]")
                }
                ImGui.treePop()
            }
        }

        fun switcherInput(value: Any, name: String) {
            when (value) {
                is Vector4 -> vector4Field(value, name)
                is Transform4 -> transform4Field(value, name)
                is Rotator4 -> rotator4Field(value, name)
                //is Array<*> -> arrayField(value)
                else -> structInput(value, name)
            }
        }

        inline fun <reified T : Enum<T>> enumField(label: String, selection: ImInt): Boolean {
            val enumValues = enumValues<T>()
            val selectedIndex = selection.get()

            ImGui.text(label)
            startInput(label)

            if (ImGui.beginCombo("##", enumValues[selectedIndex].name)) {
                for (i in enumValues.indices) {
                    val isSelected = selectedIndex == i
                    if (ImGui.selectable(enumValues[i].name, isSelected)) {
                        selection.set(i)
                        ImGui.endCombo()
                        endInput()
                        return true
                    }
                    if (isSelected) {
                        ImGui.setItemDefaultFocus()
                    }
                }
                ImGui.endCombo()
            }
            endInput()
            return false
        }
    }

}
