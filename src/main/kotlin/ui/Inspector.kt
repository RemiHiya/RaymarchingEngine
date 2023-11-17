package ui

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.DragListener
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import misc.SKIN
import ui.elements.Field
import utils.Vector4

class Inspector(title: String): Table() {
    private val contentTable: Table = Table()
    private val titleLabel: TextButton
    private var collapsed: Boolean = false

    private var selection = -1

    init {
        titleLabel = TextButton(title, SKIN)
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
        setContent(Field("Position", Vector4(1f,2f,3f,4f)))
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

    fun setTitleLabel(title: String) {
        titleLabel.setText(title)
    }

    fun setResizable(drawable: Drawable) {
        // Add code for resizing functionality
        // You may need to implement a resize handle and handle touch events for resizing
    }

    fun unSelect() { selection = -1 }

    fun select(x: Int) { selection = x }
}