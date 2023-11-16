package ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.Viewport
import misc.SKIN
import kotlin.math.roundToInt

class MainEditor(val vp: Viewport): Stage(vp) {

    private val skin = Skin(Gdx.files.internal(SKIN))
    private var fpsLabel: Label = Label("test", skin)

    init {
        addActor(fpsLabel)
    }


    override fun act(delta: Float) {
        super.act(delta)
        updateFPS(delta)
    }

    private fun updateFPS(dt: Float) {
        fpsLabel.setText(((1/dt*100).roundToInt().toFloat()/100).toString())
    }

    fun update() {

    }
}