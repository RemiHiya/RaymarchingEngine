package editor.material.nodes

import editor.material.MaterialNode
import editor.material.Pin
import editor.material.PinType
import imgui.ImGui
import imgui.flag.ImGuiWindowFlags
import imgui.type.ImBoolean
import utils.Color
import utils.Vector3

private val col = Color(154, 92, 49)

class Vec3(id: Int) : MaterialNode(id) {
    private var value = Vector3()
    private var switch = ImBoolean(false)

    override val name = "Vector 3"
    override val color = col
    override val hoverColor = color * 1.5f
    override val selectColor = color * 1.5f

    override val inputs = mapOf<Int, Pin>()
    override val outputs = mutableMapOf(
        id(0) to (Pin("rgb", PinType.VECTOR3) to "vec3(${value.x},${value.y},${value.z})"),
        id(1) to (Pin("r", PinType.FLOAT) to "${value.x}"),
        id(2) to (Pin("g", PinType.FLOAT) to "${value.y}"),
        id(3) to (Pin("b", PinType.FLOAT) to "${value.z}")
    )

    override fun customDisplay() {
        val arr = floatArrayOf(value.x, value.y, value.z)
        if (ImGui.colorButton("##", arr)) {
            switch = ImBoolean(!switch.get())
        }

        if (switch.get()) {
            ImGui.setNextWindowSize(270f, 270f)
            val flags = ImGuiWindowFlags.NoDocking or ImGuiWindowFlags.NoResize or ImGuiWindowFlags.NoCollapse
            if (ImGui.begin("Color picker ##$id", switch, flags)) {
                ImGui.dummy(0f, 5f)
                ImGui.dummy(5f, 0f)
                ImGui.sameLine()
                if (ImGui.colorPicker3("##", arr)) {
                    value = Vector3(arr[0], arr[1], arr[2])
                    outputs.clear()
                    outputs.putAll(mutableMapOf(
                        id(0) to (Pin("rgb", PinType.VECTOR3) to "vec3(${value.x},${value.y},${value.z})"),
                        id(1) to (Pin("r", PinType.FLOAT) to "${value.x}"),
                        id(2) to (Pin("g", PinType.FLOAT) to "${value.y}"),
                        id(3) to (Pin("b", PinType.FLOAT) to "${value.z}")
                    ))
                }
                ImGui.end()
            }
        }
        ImGui.sameLine()
    }
}