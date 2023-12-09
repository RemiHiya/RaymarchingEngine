package elements.components

import editor.Gui
import elements.PrimitiveObject
import imgui.ImGui
import imgui.flag.ImGuiTreeNodeFlags
import imgui.type.ImFloat
import imgui.type.ImInt
import org.reflections.Reflections
import utils.OperatorType
import utils.Transform4

open class PrimitiveComponent(): Component() {

    var primitive: PrimitiveObject? = null

    constructor(primitive: PrimitiveObject) : this() {
        this.primitive = primitive
        this.selection.set(subClasses.toList().indexOf(primitive::class.java))
    }

    override var displayName = "Primitive Component"

    private var subClasses: MutableSet<Class<out PrimitiveObject>> = mutableSetOf()
    private val selection = ImInt(0)

    init {
        rebuildSubClasses()
    }

    private fun rebuildSubClasses() {
        subClasses = Reflections(PrimitiveObject::class.java.`package`.name).getSubTypesOf(PrimitiveObject::class.java)
    }


    override fun display() {
        super.display()

        Gui.useColumn()
        if (Gui.listField("Type", selection, subClasses.map { it.simpleName })) {
            val new = subClasses.toList()[selection.get()].constructors.first().newInstance(Transform4()) as PrimitiveObject
            val p = primitive
            if (p != null) {
                new.v1 = p.v1
                new.v2 = p.v2
                new.ro = p.ro
                new.extra = p.extra
                new.operator = p.operator
                new.setMaterial(p.getMaterial())

            }
            primitive = new

        }
        Gui.stopColumn()


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