package ui

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.DragListener
import misc.Property
import misc.SKIN
import ui.core.InputFieldListener
import ui.elements.Dropdown
import ui.elements.Transform4Field
import ui.elements.Vector4Field
import utils.Transform4
import utils.Vector4
import kotlin.reflect.full.findAnnotation


class Inspector(selection: elements.Actor): Dropdown("Inspector : ${selection.displayName}"), InputFieldListener {
    private val title = "Inspector : ${selection.displayName}"
    private val contentTable: Table = Table()
    private val titleLabel: TextButton = TextButton(title, SKIN)
    private var collapsed: Boolean = false

    private val fields = processProperties(selection)

    init {
        /*titleLabel.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                toggleCollapsed()
            }
        })*/

        //this.defaults().space(6f)
        //this.add(titleLabel).fillX().expandX().colspan(2)
        //this.row()
        //this.add(contentTable).fill().expand().colspan(2)

        this.addListener(object : DragListener() {
            override fun drag(event: InputEvent, x: Float, y: Float, pointer: Int) {
                super.drag(event, x, y, pointer)
                // Handle drag to move the ElementInspector
                moveBy(x - width / 2, y - height / 2)
            }
        })

        for (i in fields) {
            for (j in i.value) {
                when (val tmp = j.second) {
                    is Vector4 -> addContent(Vector4Field(j.first, tmp))
                    is Transform4 -> addContent(Transform4Field(tmp))
                }
            }
        }
    }

    override fun onChanged() {

    }

    private fun <T : Any> processProperties(obj: T): Map<String, Array<Pair<String, Any>>> {
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