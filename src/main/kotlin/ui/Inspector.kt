package ui

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.DragListener
import misc.SKIN
import ui.core.InputFieldListener
import ui.elements.Vector4Field
import utils.Vector4

class Inspector(private val selection: elements.Actor): Table(), InputFieldListener {

    private val title = "Inspector"
    private val contentTable: Table = Table()
    private val titleLabel: TextButton = TextButton(title, SKIN)
    private var collapsed: Boolean = false

    val field = Vector4Field("Position", selection.transform.location)

    init {
        titleLabel.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                toggleCollapsed()
            }
        })

        this.defaults().space(6f)
        this.add(titleLabel).fillX().expandX().colspan(2)
        this.row()
        this.add(contentTable).fill().expand().colspan(2)

        this.addListener(object : DragListener() {
            override fun drag(event: InputEvent, x: Float, y: Float, pointer: Int) {
                super.drag(event, x, y, pointer)
                // Handle drag to move the ElementInspector
                moveBy(x - width / 2, y - height / 2)
            }
        })
        field.addListener(this)

        setContent(field)
    }

    private fun toggleCollapsed() {
        collapsed = !collapsed
        contentTable.isVisible = !collapsed
    }

    fun setCollapsed(collapsed: Boolean) {
        this.collapsed = collapsed
        contentTable.isVisible = !collapsed
    }

    fun setContent(actor: Actor) {
        contentTable.clear()
        contentTable.add(actor).fill().expand()
    }

    override fun onChanged() {
        val tmp = field.getValues()
        selection.transform.location = Vector4(tmp[0], tmp[1], tmp[2], tmp[3])
        selection.getPrimitives()[0].v1 = Vector4(tmp[0], tmp[1], tmp[2], tmp[3])
    }
}