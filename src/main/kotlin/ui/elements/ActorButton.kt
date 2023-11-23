package ui.elements

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import elements.Actor
import events.Dispatcher
import events.EventDispatcher
import events.events.ButtonActorClicked
import misc.SKIN

class ActorButton(target: Actor): TextButton(target.displayName, SKIN), Dispatcher {

    private val dispatcher = EventDispatcher()
    override fun getDispatcher() = dispatcher

    init {

        // Envoie une notification quand le bouton est cliqué
        addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                dispatcher.dispatchEvent(ButtonActorClicked(target))
            }
        })
    }
}