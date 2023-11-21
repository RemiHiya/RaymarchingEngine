package ui

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.DragListener
import elements.Actor
import elements.Scene
import misc.SKIN

class Outliner(scene: Scene): Table() {

    private val contentTable = Table()

    init {

        contentTable.row().pad(2f)

        for (i in scene.getActors()) {
            addItem(i)
        }
        addItem(scene.camera)
        add(contentTable).fill().expand()

        addListener(object : DragListener() {
            override fun drag(event: InputEvent, x: Float, y: Float, pointer: Int) {
                super.drag(event, x, y, pointer)
                moveBy(x - width / 2, y - height / 2)
            }
        })
    }


    private fun addItem(elem: Actor) {
        val button = TextButton(elem.displayName, SKIN)
        contentTable.add(button).colspan(2).expandX().fillX().padBottom(0f)
        contentTable.row().pad(2f)
        /*
        TODO : Ajouter le listener
         */
    }
}