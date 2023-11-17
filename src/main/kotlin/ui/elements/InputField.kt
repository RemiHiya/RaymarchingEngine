package ui.elements

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import misc.SKIN
import ui.core.InputFieldListener
import java.lang.ref.WeakReference

class InputField(default: String): TextField(default, SKIN) {

    private var listener = WeakReference<InputFieldListener>(null)

    init {
        addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                selectAll()
            }
        })

        addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                listener.get()?.onChanged()
            }
        })

        textFieldFilter = NumericFilter()
    }

    fun getValue(): Float = text.toFloatOrNull() ?: 0f

    fun addListener(listener: InputFieldListener) {
        this.listener = WeakReference(listener)
    }

    private class NumericFilter : TextFieldFilter {
        override fun acceptChar(textField: TextField?, c: Char): Boolean {
            return Character.isDigit(c) || c == '-' || c == '.'
        }
    }
}