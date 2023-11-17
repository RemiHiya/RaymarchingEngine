package ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.Viewport
import misc.SKIN_PATH
import kotlin.math.roundToInt

class MainEditor(val vp: Viewport): Stage(vp) {

    private val skin = Skin(Gdx.files.internal(SKIN_PATH))
    private var fpsLabel: Label = Label("fps", skin)
    private val t = Table()
    private val inspector = Inspector("test")

    init {
        t.setFillParent(true)
        t.add(inspector)
        t.right()
        addActor(t)
    }


    override fun act(delta: Float) {
        super.act(delta)
        updateFPS(delta)
    }

    private fun updateFPS(dt: Float) {
        fpsLabel.setText(((1/dt*100).roundToInt().toFloat()/100).toString())
    }

    fun update() {
        //t.top()
    }
}