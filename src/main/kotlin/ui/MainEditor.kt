package ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.Viewport
import elements.Scene
import misc.SKIN_PATH
import kotlin.math.roundToInt

class MainEditor(val vp: Viewport, scene: Scene): Stage(vp) {

    private val skin = Skin(Gdx.files.internal(SKIN_PATH))
    private var fpsLabel: Label = Label("fps", skin)
    private val t = Table()
    private val fpsHolder = Table()
    private var inspector: Inspector? = null


    init {
        val ac: elements.Actor? = scene.getActor(1)
        if (ac != null) {
            inspector = Inspector(ac)
        }
        fpsHolder.setFillParent(true)
        fpsHolder.add(fpsLabel)
        fpsHolder.top()
        t.setFillParent(true)
        t.add(inspector)
        t.right()
        addActor(t)
        addActor(fpsHolder)
    }


    override fun act(delta: Float) {
        super.act(delta)
        updateFPS(delta)
    }

    private fun updateFPS(dt: Float) {
        val fps = (1/dt*100).roundToInt().toFloat()/100
        val ms = (dt*100000).roundToInt().toFloat()/100
        fpsLabel.setText("$fps FPS\n$ms ms")
    }

    fun update() {
        //t.top()
    }
}