package ui.core

import com.badlogic.gdx.scenes.scene2d.ui.Table
import misc.Property
import ui.elements.InputArray
import ui.elements.Rotator4Field
import ui.elements.Transform4Field
import ui.elements.Vector4Field
import utils.Rotator4
import utils.Transform4
import utils.Vector4
import kotlin.reflect.full.findAnnotation

class Helper {

    // Renvoie le widget correspondant à un type de donnée
    fun mapObject(elem: Any, name: String = ""): Table? {
        return when (elem) {
            is Vector4 -> Vector4Field(name, elem)
            is Transform4 -> Transform4Field(elem)
            is Rotator4 -> Rotator4Field(name, elem)
            is Array<*> -> InputArray(name, elem)
            else -> null
        }
    }

    fun <T : Any> processProperties(obj: T): Map<String, Array<Pair<String, Any>>> {
        val propertyMap = mutableMapOf<String, MutableList<Pair<String, Any>>>()
        obj::class.members.forEach { property ->
            val propertyAnnotation = property.findAnnotation<Property>()

            if (propertyAnnotation != null) {
                val categoryName = propertyAnnotation.category
                val displayName = propertyAnnotation.name
                val propertyValue = property.call(obj)

                if (propertyValue != null) {
                    val propertyPair = Pair(displayName, propertyValue)
                    propertyMap.computeIfAbsent(categoryName) { mutableListOf() }.add(propertyPair)
                }
            }
        }
        return propertyMap.mapValues { it.value.toTypedArray() }
    }
}