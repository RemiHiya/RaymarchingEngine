package ui.elements

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import misc.SKIN
import ui.core.InputFieldListener
import utils.Vector4

class Field(name: String, value: Vector4) : Table(), InputFieldListener {
    private val nameLabel: Label
    private val numberFields: Array<InputField>

    init {
        nameLabel = Label(name, SKIN)
        this.add(nameLabel).align(Align.left).padLeft(5f)
        numberFields = Array(4) { index ->
            val v = when (index) {
                0 -> value.x
                1 -> value.y
                2 -> value.z
                3 -> value.w
                else -> {0}
            }
            val textField = InputField(v.toString())

            textField.addListener(this)

            this.add(textField).pad(5f).width(40f)
            if (index < 3) {
                this.add() // Ajoute un espace entre les champs de saisie
            }
            return@Array textField
        }
    }

    fun getValues(): Array<Float> {
        val values = Array(4) { 0f }
        for (i in 0 until 4) {
            values[i] = numberFields[i].getValue()
        }
        return values
    }

    override fun onChanged() {

    }

}