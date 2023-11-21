package ui

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.Viewport
import elements.Scene
import misc.SKIN
import kotlin.math.roundToInt

class MainEditor(val vp: Viewport, scene: Scene): Stage(vp) {

    private val inspectorHolder = Table()
    private val outlinerHolder = Table()
    private val fpsHolder = Table()

    private var outliner = Outliner(scene)
    private var fpsLabel: Label = Label("fps", SKIN)
    private var inspector: Inspector? = null


    init {
        val ac: elements.Actor? = scene.camera
        if (ac != null) {
            inspector = Inspector(ac)
        }
        fpsHolder.setFillParent(true)
        fpsHolder.add(fpsLabel)
        fpsHolder.top()

        outlinerHolder.setFillParent(true)
        outlinerHolder.add(outliner)
        outlinerHolder.left()

        inspectorHolder.setFillParent(true)
        inspectorHolder.add(inspector)
        inspectorHolder.right()

        addActor(inspectorHolder)
        addActor(fpsHolder)
        addActor(outlinerHolder)
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