package editor

import imgui.ImGui
import imgui.flag.ImGuiInputTextFlags
import imgui.flag.ImGuiTreeNodeFlags
import imgui.flag.ImGuiWindowFlags
import imgui.type.ImBoolean
import imgui.type.ImString
import org.reflections.Reflections
import kotlin.reflect.KClass

class AdderWidget<T : Any>(
    private val windowName: String,
    private val windowOpen: ImBoolean,
    val name: ImString,
    val createCallback: (KClass<out T>) -> Unit
) {

    data class TreeNode<T: Any>(val clazz: KClass<out T>, val children: List<TreeNode<T>>)
    lateinit var classHierarchy: TreeNode<T>

    inline fun <reified U: T> build() {
        classHierarchy = buildClassHierarchy(U::class)
    }

    fun buildClassHierarchy(parentClass: KClass<out T>): TreeNode<T> {
        return TreeNode(parentClass, getDirectSubclasses(parentClass).map { subclass ->
            buildClassHierarchy(subclass)
        })
    }

    private fun getDirectSubclasses(parentClass: KClass<out T>): List<KClass<out T>> {
        val reflections = Reflections(parentClass.java.`package`.name)
        val subclasses = reflections.getSubTypesOf(parentClass.java).map { it.kotlin }
        return subclasses.filter { it.java.superclass == parentClass.java }
    }

    private var selected: KClass<out T>? = null


    fun update() {
        if (windowOpen.get()) {
            ImGui.setNextWindowFocus()
            if (ImGui.begin(windowName, windowOpen, ImGuiWindowFlags.NoDocking or ImGuiWindowFlags.NoCollapse)) {
                ImGui.text("Component name")
                ImGui.sameLine()
                ImGui.inputText("##", name, ImGuiInputTextFlags.AutoSelectAll)
                ImGui.separator()

                ImGui.text("Parent selection:")
                ImGui.indent()
                displaySubclasses(classHierarchy, true)
                ImGui.unindent()

                ImGui.separator()
                ImGui.spacing()

                if (selected == null)
                    ImGui.beginDisabled()
                if (ImGui.button("Create")) {
                    val tmp = selected
                    if (tmp != null) {
                        createCallback(tmp)
                        windowOpen.set(false)
                    }
                }
                if (selected == null)
                    ImGui.endDisabled()
            } else {
                windowOpen.set(false)
            }
            ImGui.end()
        }
    }

    private fun displaySubclasses(node: TreeNode<T>, opened: Boolean = false) {
        val className = node.clazz.simpleName
        val flag = if (node.clazz == selected) ImGuiTreeNodeFlags.Selected else 0
        val type = if (node.children.isEmpty()) ImGuiTreeNodeFlags.Leaf else ImGuiTreeNodeFlags.OpenOnDoubleClick or ImGuiTreeNodeFlags.OpenOnArrow
        val open = if (opened) ImGuiTreeNodeFlags.DefaultOpen else 0

        if (ImGui.treeNodeEx(className, type or flag or open or ImGuiTreeNodeFlags.SpanAvailWidth)) {
            if (ImGui.isItemClicked() && !node.clazz.isAbstract) {
                selected = node.clazz
            }

            node.children.forEach { childNode ->
                displaySubclasses(childNode)
            }

            ImGui.treePop()
        }
    }



}