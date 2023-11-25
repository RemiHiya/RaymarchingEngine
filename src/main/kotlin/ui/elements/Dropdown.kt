package ui.elements

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import misc.SKIN

open class Dropdown(private val title: String) : Table() {

    private val contentTable: Table = Table()
    private val arrowButton: ImageTextButton = ImageTextButton("v", SKIN)
    private var isContentVisible: Boolean = true

    init {
        val titleLabel = Label(title, SKIN)

        background = SKIN.getDrawable("rect")

        this.add(arrowButton).space(4f).width(arrowButton.height)
        this.add(titleLabel).expandX().center()
        this.row()
        this.add(contentTable).expand().fill().colspan(2).space(4f)

        // Gestion de la flèche pour plier, déplier la table
        arrowButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                toggleContentVisibility()
            }
        })
    }

    private fun toggleContentVisibility() {
        isContentVisible = !isContentVisible
        arrowButton.text = if (isContentVisible) "v" else ">"
        contentTable.isVisible = isContentVisible
        clearChildren()
        add(arrowButton).space(4f).width(arrowButton.height)
        add(Label(title, SKIN)).expandX().center()
        row()
        if (isContentVisible) {
            add(contentTable).expand().fill().colspan(2).space(4f)
        }
    }

    fun addContent(actor: Actor) {
        contentTable.add(actor).expand().fill()
        contentTable.row()
    }

}