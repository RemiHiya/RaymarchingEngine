package elements.components

import api.math.rotation
import api.math.times
import api.math.transformBy
import editor.Debug
import editor.Debuggable
import editor.Gui
import imgui.ImGui
import imgui.flag.ImGuiTreeNodeFlags
import imgui.type.ImFloat
import imgui.type.ImInt
import utils.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class PhysicsComponent: Component(), Debuggable {
    override var displayName = "Physics Component"
    override val singleton = true

    var points: MutableList<Pair<Vector4, Float>> = mutableListOf()
    var type: ColliderType = ColliderType.DYNAMIC
    var dynamicRebuild = false
    var gravity = Vector4(0f, 0f, -1f, 0f)
    var mass = 1f
    var linearFriction = 0.8f
    var angularFriction = 0.9f
    var center = Vector4()
    var radius = 0f
    private var objects: List<PrimitiveComponent> = listOf()
    lateinit var description: (Vector4) -> Float
    var linearVelocity = Vector4()
    var angularVelocity = Rotator4(0f, 0f, 0f, 0f)

    override fun construct() {
        objects = parent.components.filterIsInstance<PrimitiveComponent>()
        points.clear()
        discretize()
    }

    override fun update(dt: Float) {

    }

    fun getNormal(pos: Vector4, epsilon: Float = 0.0001f): Vector4 {
        val dx = description(Vector4(pos.x + epsilon, pos.y, pos.z, pos.w)) - description(pos)
        val dy = description(Vector4(pos.x, pos.y + epsilon, pos.z, pos.w)) - description(pos)
        val dz = description(Vector4(pos.x, pos.y, pos.z + epsilon, pos.w)) - description(pos)
        val dw = description(Vector4(pos.x, pos.y, pos.z, pos.w + epsilon)) - description(pos)

        return Vector4(dx, dy, dz, dw).normalize()
    }

    fun addTorque(position: Vector4, force: Vector4) {
        val c = center.transformBy(parent.transform)
        val lever = position - c
        val torque = Vector4(
            lever.y * force.z - lever.z * force.y,
            lever.z * force.x - lever.x * force.z,
            lever.x * force.y - lever.y * force.x,
            lever.w * force.x - lever.x * force.w
        )
        angularVelocity += Rotator4(torque * calculateMomentOfInertiaSphere())
    }

    fun calculateMomentOfInertiaSphere() = (2f / 5f) * mass * radius * radius


    /**
     * Prend en entrée [objects] les objets présents, en détermine le centre moyen et les bounds.
     * Appelle la fonction [generate].
     */
    private fun discretize() {
        if (objects.isEmpty()) {
            points.clear()
            return
        }
        description = getDesc()
        center = Vector4()
        objects.forEach { obj -> center += obj.primitive.v1 }
        center /= objects.size.toFloat()
        radius = objects.maxOf {
            obj -> (obj.primitive.v1 - center).length() + abs(obj.primitive.simplifiedCollider(Vector4()))
        }
        generate(center, radius)
        if (points.isNotEmpty())
            center = points.map { it.first }.reduce(Vector4::plus) / points.size.toFloat()
    }

    /**
     * Génère le nuage de point [points] représentant la suface de la SDF associée.
     * Chaque point est associé à une distance signée.
     * @param center Le centre autour duquel générer le nuage de points.
     * @param distance La distance maximale par rapport au centre pour générer des points.
     * @param step Le pas utilisé lors de la génération des points (par défaut: 0.5f).
     * @param threshold Le seuil pour déterminer si un point est sur la surface (par défaut: 0.1f).
     */
    private fun generate(center: Vector4, distance: Float, step: Float = 1f, threshold: Float = 0.1f) {
        val steps = (distance / step).toInt() * 2
        var x = -steps
        while (x <= steps) {
            var y = -steps
            while (y <= steps) {
                var z = -steps
                while (z <= steps) {
                    var w = -steps
                    while (w <= steps) {
                        val point = center * 2f + Vector4(x*step, y*step, z*step, w*step)
                        val dist = description(point/2f)
                        if (abs(dist) > step) {
                            w += (step + abs(dist)/step).toInt()
                            continue
                        }
                        if (abs(dist) < threshold)
                            points += Pair(point, dist)
                        w++
                    }
                    z++
                }
                y++
            }
            x++
        }
    }

    /**
     * Calcule la description de la scene en tant que SDF, en fonction de [objects].
     * @return la fonction de distance signée calculant la distance entre un Vector4 et les objets.
     */
    private fun getDesc(): (Vector4) -> Float {
        val objs = objects.sortedBy { it.primitive.operator.operator }
        return { pos: Vector4 ->
            var dist = 1000f
            for (i in objs.map { it.primitive }) {
                val tmp = rotation(i.ro.toRadians())
                dist = when (i.operator.operator) {
                    OperatorType.UNION -> {
                        min(dist, i.collider(tmp*(pos-i.v1)))
                    }
                    OperatorType.SUBTRACTION -> {
                        max(dist, -i.collider(tmp*(pos-i.v1)))
                    }
                    OperatorType.INTERSECTION -> {
                        max(dist, i.collider(tmp*(pos-i.v1)))
                    }
                }
            }
            dist
        }
    }


    override fun display() {
        super.display()
        if (ImGui.collapsingHeader("Options", ImGuiTreeNodeFlags.DefaultOpen)) {
            ImGui.indent()
            Gui.useColumn()
            val op = ImInt(type.ordinal)
            if (Gui.enumField<ColliderType>("Collider", op)) {
                type = ColliderType.values()[op.get()]
            }
            Gui.vector4Field(gravity, "Gravity")
            val m = ImFloat(mass)
            if (Gui.floatField(m, "Mass")) {
                mass = m.get()
            }
            Gui.stopColumn()
            ImGui.unindent()
        }
        ImGui.spacing()

        if (ImGui.collapsingHeader("Runtime", ImGuiTreeNodeFlags.DefaultOpen)) {
            ImGui.indent()
            Gui.useColumn()
            Gui.vector4Field(linearVelocity, "Linear velocity")
            Gui.rotator4Field(angularVelocity, "Angular velocity")
            Gui.stopColumn()
            ImGui.unindent()
        }

    }

    override fun debug() {
        points.forEach {
            Debug.drawPoint(it.first.transformBy(Transform4(parent.transform.location*2f, parent.transform.rotation)))
        }
    }
}