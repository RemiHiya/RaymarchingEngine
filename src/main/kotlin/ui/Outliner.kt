package ui

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.DragListener
import elements.Actor
import elements.Scene
import events.Dispatcher
import events.EventDispatcher
import events.EventListener
import events.events.ButtonActorClicked
import events.events.SelectionChanged
import ui.elements.ActorButton

class Outliner(scene: Scene): Table(), Dispatcher {

    private val contentTable = Table()
    private val dispatcher = EventDispatcher()
    override fun getDispatcher() = dispatcher

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
        val button = ActorButton(elem)
        // Récupère la notification de quand le bouton est cliqué
        button.getDispatcher().addListener(object : EventListener<ButtonActorClicked> {
            override fun onEventReceived(event: ButtonActorClicked) {
                dispatcher.dispatchEvent(SelectionChanged(event.target))
            }
        })
        contentTable.add(button).colspan(2).expandX().fillX().padBottom(0f)
        contentTable.row().pad(2f)
    }
}