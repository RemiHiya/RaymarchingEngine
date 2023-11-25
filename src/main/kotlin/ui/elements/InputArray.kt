package ui.elements

import misc.Property
import ui.core.Helper
import kotlin.reflect.full.findAnnotation


class InputArray(name: String, private val value: Array<*>): Dropdown("Array : $name") {

    init {
        for ((index, i) in value.withIndex()) {
            if (i != null){
                val tmp = Dropdown("Element[$index] : ${i::class.simpleName}")
                for (j in Helper().processProperties(i).values) {
                    for (k in j) {
                        Helper().mapObject(k.second, k.first)?.let { tmp.addContent(it) }
                    }
                }
                addContent(tmp)
            }
        }
    }

}