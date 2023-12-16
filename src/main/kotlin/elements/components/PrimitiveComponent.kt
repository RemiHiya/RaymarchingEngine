package elements.components

import editor.Debuggable
import editor.Gui
import elements.PrimitiveObject
import elements.primitives.Cube
import imgui.ImGui
import imgui.flag.ImGuiTreeNodeFlags
import imgui.type.ImFloat
import imgui.type.ImInt
import org.reflections.Reflections
import utils.OperatorType
import utils.Transform4

open class PrimitiveComponent(): Component(), Debuggable {

    // Valeur par défaut
    var primitive: PrimitiveObject = Cube(Transform4())

    constructor(primitive: PrimitiveObject) : this() {
        this.primitive = primitive
        this.selection.set(subClasses.toList().indexOf(primitive::class.java))
    }

    override var displayName = "Primitive Component"

    private var subClasses: MutableSet<Class<out PrimitiveObject>> = mutableSetOf()
    private val selection = ImInt(0)

    override fun construct() {

    }

    init {
        rebuildSubClasses()
        selection.set(subClasses.toList().indexOf(primitive::class.java))
    }

    private fun rebuildSubClasses() {
        subClasses = Reflections(PrimitiveObject::class.java.`package`.name).getSubTypesOf(PrimitiveObject::class.java)
    }


    override fun display() {
        super.display()

        ImGui.indent()
        Gui.useColumn()
        if (Gui.listField("Type", selection, subClasses.map { it.simpleName })) {
            val new = subClasses.toList()[selection.get()].constructors.first().newInstance(Transform4()) as PrimitiveObject
            new.v1 = primitive.v1
            new.v2 = primitive.v2
            new.ro = primitive.ro
            new.extra = primitive.extra
            new.operator = primitive.operator
            new.setMaterial(primitive.getMaterial())

            this.primitive = new
            parent.construct()
        }
        Gui.stopColumn()
        ImGui.unindent()

        if (ImGui.collapsingHeader("Transform", ImGuiTreeNodeFlags.DefaultOpen)) {
            ImGui.indent()
            Gui.useColumn()
            Gui.vector4Field(primitive.v1, "Location")
            Gui.rotator4Field(primitive.ro, "Rotation")
            Gui.stopColumn()
            ImGui.unindent()
        }
        ImGui.spacing()
        if (ImGui.collapsingHeader("Parameters", ImGuiTreeNodeFlags.DefaultOpen)) {
            ImGui.indent()
            Gui.useColumn()
            Gui.vector4Field(primitive.v2, "Bounds")
            val extra = ImFloat(primitive.extra)
            if (Gui.floatField(extra, "Extra")) {
                primitive.extra = extra.get()
            }
            Gui.stopColumn()
            ImGui.unindent()

            if (ImGui.treeNodeEx("Operator", ImGuiTreeNodeFlags.DefaultOpen or ImGuiTreeNodeFlags.SpanAvailWidth)) {
                ImGui.indent()
                Gui.useColumn()
                val op = ImInt(primitive.operator.operator.ordinal)
                if (Gui.enumField<OperatorType>("Type", op)) {
                    primitive.operator.operator = OperatorType.values()[op.get()]
                }
                val smooth = ImFloat(primitive.operator.smoothness)
                if (Gui.floatField(smooth, "Smoothness")) {
                    primitive.operator.smoothness = smooth.get()
                }
                Gui.stopColumn()
                ImGui.unindent()
                ImGui.treePop()
            }
        }

    }

    override fun debug(transform: Transform4) {
        primitive.debug(transform)
    }

    override fun debug() {
        debug(parent.transform)
    }

}