package editor

import imgui.ImGui
import imgui.flag.ImGuiInputTextFlags
import imgui.flag.ImGuiTreeNodeFlags
import imgui.flag.ImGuiWindowFlags
import imgui.type.ImBoolean
import imgui.type.ImString
import kotlin.reflect.KClass

class AdderWidget<T : Any>(
    val windowName: String,
    val windowOpen: ImBoolean,
    val name: ImString,
    val createCallback: (KClass<out T>) -> Unit
) {

    var selected: KClass<out T>? = null


    inline fun <reified U: T> update() {
        if (windowOpen.get()) {
            if (ImGui.begin(windowName, windowOpen, ImGuiWindowFlags.NoDocking)) {
                ImGui.text("Component name")
                ImGui.sameLine()
                ImGui.inputText("##", name, ImGuiInputTextFlags.AutoSelectAll)
                ImGui.separator()

                ImGui.text("Parent selection:")
                ImGui.indent()
                displaySubclasses(U::class.sealedSubclasses)
                ImGui.unindent()

                ImGui.separator()
                ImGui.spacing()

                if (ImGui.button("Create")) {
                    val tmp = selected
                    if (tmp != null) {
                        createCallback(tmp)
                        windowOpen.set(false)
                    }
                }
            } else {
                windowOpen.set(false)
            }
            ImGui.end()
        }
    }

    fun displaySubclasses(subclasses: List<KClass<out T>>) {
        val sortedSubclasses = subclasses.sortedByDescending { it.sealedSubclasses.isNotEmpty() }
        for (elem in sortedSubclasses) {
            val className = elem.simpleName
            val flag = if (elem == selected) ImGuiTreeNodeFlags.Selected else 0
            val type = if (elem.sealedSubclasses.isEmpty()) ImGuiTreeNodeFlags.Leaf else 0

            if (ImGui.treeNodeEx(className, type or flag or ImGuiTreeNodeFlags.SpanAvailWidth)) {
                if (ImGui.isItemClicked() && elem.sealedSubclasses.isEmpty()) {
                    selected = elem
                }

                displaySubclasses(elem.sealedSubclasses)

                ImGui.treePop()
            }
        }
    }
}