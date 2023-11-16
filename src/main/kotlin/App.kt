import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Pixmap.Format
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.GdxRuntimeException
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import elements.Scene
import elements.SceneParser
import misc.PATH
import misc.SKIN
import ui.MainEditor
import kotlin.math.sin


class App(private val scene: Scene) : ApplicationAdapter() {
    private var spriteBatch: SpriteBatch? = null
    private var texture: Texture? = null
    private lateinit var shaderProgram: ShaderProgram

    private var stage: Stage? = null
    private lateinit var viewport: Viewport
    private lateinit var editor: MainEditor

    private var time = 0f
    private val parser = SceneParser(scene)

    private fun tick(deltaTime: Float) {
        //println(1/deltaTime)
        /*
        TODO : Scene tick
         */
    }

    override fun create() {
        viewport = ScreenViewport()

        editor = MainEditor(viewport)
        editor.isDebugAll = true

        stage = Stage(viewport)

        val skin = Skin(Gdx.files.internal(SKIN))

        val leftPanel = createCollapsiblePanel("Panel Gauche", skin)
        leftPanel.setPosition(10f, 10f)

        // Panel de droite
        val rightPanel = createCollapsiblePanel("Panel Droit", skin)
        rightPanel.setPosition(Gdx.graphics.width - rightPanel.width - 10f, 10f)


        stage?.addActor(leftPanel)
        stage?.addActor(rightPanel)

        Gdx.input.inputProcessor = stage // Définissez le Stage comme processeur d'entrée


        /*
        S'occupe de générer le shader
         */
        spriteBatch = SpriteBatch()
        val width = Gdx.graphics.width
        val height = Gdx.graphics.height

        val pixmap = Pixmap(width, height, Format.RGBA8888)
        texture = Texture(pixmap)

        val vertexShader = """
            attribute vec4 a_position;
            void main() {
                gl_Position = a_position;
            }
        """.trimIndent()

        var shaderCode = parser.initialize()
        shaderCode += parser.computeScene() + "\n"
        shaderCode += parser.computeMaterials() + "\n"
        shaderCode += parser.computeMapper() + "\n"
        shaderCode += Gdx.files.internal(PATH + "shaders/frag.glsl").readString()
        println(shaderCode)

        shaderProgram = ShaderProgram(vertexShader, shaderCode)
        ShaderProgram.pedantic = false

        if (!shaderProgram.isCompiled) {
            throw GdxRuntimeException("Shader compilation failed: ${shaderProgram.log}")
        }

    }


    override fun render() {
        tick(Gdx.graphics.deltaTime)

        /*
        S'occupe du rendu
         */

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        spriteBatch!!.begin()
        shaderProgram.bind()
        time += Gdx.graphics.deltaTime

        // Update la liste d'objets du shader
        shaderProgram.setUniformi("SCENE_SIZE", scene.getObjects().size)
        parser.updateShaderObjects(shaderProgram)

        // Update les input du shader
        shaderProgram.setUniformf("u_time", time)
        shaderProgram.setUniformf("w", sin(time))
        shaderProgram.setUniformf("u_screenSize",(Gdx.graphics.width).toFloat(),
            (Gdx.graphics.height).toFloat())
        shaderProgram.setUniformf("u_resolution", .5f)

        spriteBatch!!.shader = shaderProgram
        spriteBatch!!.draw(texture, -1f, -1f)
        spriteBatch!!.end()

        /*
        Rendu de l'éditeur
         */
        //stage?.act(Gdx.graphics.deltaTime)
        //stage?.draw()
        editor.act(Gdx.graphics.deltaTime)
        editor.draw()
    }

    override fun dispose() {
        spriteBatch?.dispose()
        texture?.dispose()
        shaderProgram.dispose()
        stage?.dispose()
        editor.dispose()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        viewport.update(width, height)
        editor.vp.update(width, height)
        editor.vp.apply(true)
        editor.update()
    }



    private fun createCollapsiblePanel(title: String, skin: Skin): Table {
        val panel = Table()
        panel.background = skin.getDrawable("rect")
        panel.pad(10f).defaults().space(10f)

        val titleLabel = TextButton(title, skin)
        titleLabel.isDisabled = true
        panel.add(titleLabel).colspan(3).center().row()

        for (i in 1..3) {
            val button = TextButton("Button $i", skin)
            panel.add(button).center()
        }
        panel.pack()

        return panel
    }

}