package ui

import App
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.Viewport
import elements.Scene
import events.EventListener
import events.events.FloatValueUpdated
import events.events.SelectionChanged
import misc.SKIN
import ui.elements.FloatField
import ui.elements.InputField
import utils.RmFloat
import kotlin.math.roundToInt

class MainEditor(val vp: Viewport, scene: Scene, app: App): Stage(vp) {

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

        // Récupère la notification de quand un nouvel Actor est sélectionné depuis l'outliner
        outliner.getDispatcher().addListener(object : EventListener<SelectionChanged> {
            override fun onEventReceived(event: SelectionChanged) {
                inspectorHolder.removeActor(inspector)
                inspector = Inspector(event.new)
                inspectorHolder.add(inspector)
            }
        })

        // Ajoute les différents panneaux à l'UI principale
        fpsHolder.setFillParent(true)
        val scaleField = FloatField(app.scale)
        scaleField.getDispatcher().addListener(object : EventListener<FloatValueUpdated> {
            override fun onEventReceived(event: FloatValueUpdated) {
                app.resizeFrameBuffer(Gdx.graphics.width, Gdx.graphics.height)
            }
        })
        fpsHolder.add(scaleField)
        fpsHolder.row()
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