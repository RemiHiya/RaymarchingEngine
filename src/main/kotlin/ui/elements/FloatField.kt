package ui.elements

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import events.Dispatcher
import events.EventDispatcher
import events.events.FloatValueUpdated
import misc.SKIN
import utils.RmFloat

class FloatField(private val value: RmFloat): TextField(value.value.toString(), SKIN), Dispatcher {


    private val dispatcher = EventDispatcher()
    private var mem = value.value.toString()

    init {
        addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                selectAll()
                isDisabled = false
                mem = text
            }

            override fun keyDown(event: InputEvent, keycode: Int): Boolean {
                if (keycode == Input.Keys.ESCAPE) {
                    clearSelection()
                    isDisabled = true
                    if (text == "") setText(mem)
                    dispatcher.dispatchEvent(FloatValueUpdated(value.value))
                } else if (keycode == Input.Keys.ENTER) {
                    if (text == "") setText(mem)
                    mem = text
                    value.value = mem.toFloat()
                    selectAll()
                    dispatcher.dispatchEvent(FloatValueUpdated(value.value))
                }
                return super.keyDown(event, keycode)
            }
        })

        textFieldFilter = NumericFilter()
    }

    fun getValue(): Float = text.toFloatOrNull() ?: 0f

    private class NumericFilter : TextFieldFilter {
        override fun acceptChar(textField: TextField?, c: Char): Boolean {
            return Character.isDigit(c) || c == '-' || c == '.'
        }
    }

    override fun getDispatcher() = dispatcher
}