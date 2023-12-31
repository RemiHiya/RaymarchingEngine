package api

import elements.Actor
import elements.PrimitiveObject
import elements.components.PrimitiveComponent

fun Actor.getPrimitives(): Array<PrimitiveObject> {
    var tmp: Array<PrimitiveObject> = arrayOf()
    for (i in components) {
        if (i is PrimitiveComponent) {
            tmp += i.primitive
        }
    }
    return tmp.sortedWith(compareBy({ it.operator.operator }, { it.operator.smoothness })).toTypedArray()
}