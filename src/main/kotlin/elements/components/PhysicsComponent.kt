package elements.components

import api.math.rotation
import api.math.times
import api.math.transformBy
import editor.Debug
import editor.Debuggable
import editor.Gui
import imgui.type.ImInt
import utils.ColliderType
import utils.OperatorType
import utils.Transform4
import utils.Vector4
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class PhysicsComponent: Component(), Debuggable {
    override var displayName = "Physics Component"

    var points: MutableList<Vector4> = mutableListOf()
    var type: ColliderType = ColliderType.DYNAMIC
    var dynamicRebuild = false
    private var objects: List<PrimitiveComponent> = listOf()
    private lateinit var description: (Vector4) -> Float

    override fun construct() {
        objects = parent.components.filterIsInstance<PrimitiveComponent>()
        points.clear()
        discretize()
    }

    /**
     * Prend en entrée [objects] les objets présents, en détermine le centre moyen et les bounds.
     * Appelle la fonction [generate].
     */
    fun discretize() {
        description = getDesc()
        var center = Vector4()
        objects.forEach { obj -> center += obj.primitive.v1 }
        center /= objects.size.toFloat()
        val dist = objects.maxOf { obj -> (obj.primitive.v1 - center).length() +
                abs(obj.primitive.simplifiedCollider(Vector4())) }
        generate(center, dist)
    }

    /**
     * Génère le nuage de point représentant la suface de la SDF associée.
     * @param center Le centre autour duquel générer le nuage de points.
     * @param distance La distance maximale par rapport au centre pour générer des points.
     * @param step Le pas utilisé lors de la génération des points (par défaut: 0.5f).
     * @param threshold Le seuil pour déterminer si un point est sur la surface (par défaut: 0.1f).
     */
    private fun generate(center: Vector4, distance: Float, step: Float = .5f, threshold: Float = 0.1f) {
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
                            points += point
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
        val op = ImInt(type.ordinal)
        if (Gui.enumField<ColliderType>("Collider", op)) {
            type = ColliderType.values()[op.get()]
        }
    }

    override fun debug() {
        points.forEach {
            Debug.drawPoint(Transform4(it).transformBy(Transform4(parent.transform.location*2f)).location)
        }
    }
}