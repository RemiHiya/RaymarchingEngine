package editor

import imgui.ImGui
import imgui.flag.ImGuiTreeNodeFlags
import utils.Rotator4
import utils.Transform4
import utils.Vector4
import kotlin.reflect.KProperty1

class UiElements {

    companion object {
        fun vector4Field(value: Vector4, name: String) {
            val tmp = floatArrayOf(value.x, value.y, value.z, value.w)
            if (ImGui.inputFloat4(name, tmp)) {
                value.x = tmp[0]
                value.y = tmp[1]
                value.z = tmp[2]
                value.w = tmp[3]
            }
        }
        fun rotator4Field(value: Rotator4, name: String) {
            val tmp = floatArrayOf(value.roll, value.pitch, value.yaw, value.w)
            if (ImGui.inputFloat4(name, tmp)) {
                value.roll = tmp[0]
                value.pitch = tmp[1]
                value.yaw = tmp[2]
                value.w = tmp[3]
            }
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
    }

}
