import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Pixmap.Format
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.utils.GdxRuntimeException
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import elements.Scene
import elements.SceneParser
import misc.PATH
import ui.MainEditor
import kotlin.math.sin


class App(private val scene: Scene) : ApplicationAdapter() {
    private var spriteBatch: SpriteBatch? = null
    private var texture: Texture? = null
    private lateinit var shaderProgram: ShaderProgram

    private lateinit var viewport: Viewport
    private lateinit var editor: MainEditor

    private var time = 0f
    private var scale = 0.3f
    private lateinit var frameBuffer: FrameBuffer
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
        //editor.isDebugAll = true
        Gdx.input.inputProcessor = editor // Définissez le Stage comme processeur d'entrée


        /*
        S'occupe de générer le shader
         */

        frameBuffer = FrameBuffer(
            Format.RGBA8888,
            (Gdx.graphics.width * scale).toInt(),
            (Gdx.graphics.height * scale).toInt(),
            false)

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

        frameBuffer.begin()
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        spriteBatch!!.projectionMatrix.setToOrtho2D(0f, 0f,
            frameBuffer.width.toFloat()/scale,
            frameBuffer.height.toFloat()/scale)

        spriteBatch!!.begin()
        shaderProgram.bind()
        time += Gdx.graphics.deltaTime

        // Update la liste d'objets du shader
        shaderProgram.setUniformi("SCENE_SIZE", scene.getObjects().size)
        parser.updateShaderObjects(shaderProgram)

        // Update les input du shader
        shaderProgram.setUniformf("u_time", time)
        shaderProgram.setUniformf("w", sin(time))
        shaderProgram.setUniformf("u_screenSize",
            frameBuffer.width.toFloat(),
            frameBuffer.height.toFloat())

        spriteBatch!!.shader = shaderProgram
        spriteBatch!!.draw(texture, -1f, -1f, 2/scale, 2/scale)
        spriteBatch!!.end()

        frameBuffer.end()

        spriteBatch!!.begin()
        spriteBatch!!.shader = null
        texture!!.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        spriteBatch!!.draw(frameBuffer.colorBufferTexture, 0f, 0f,
            Gdx.graphics.width.toFloat(),
            Gdx.graphics.height.toFloat())
        spriteBatch!!.end()

        /*
        Rendu de l'éditeur
         */
        editor.act(Gdx.graphics.deltaTime)
        editor.draw()
    }

    override fun dispose() {
        spriteBatch?.dispose()
        texture?.dispose()
        shaderProgram.dispose()
        editor.dispose()
        frameBuffer.dispose()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        viewport.update(width, height)
        resizeFrameBuffer(width, height)
        editor.vp.update(width, height)
        editor.vp.apply(true)
        editor.update()
    }

    private fun resizeFrameBuffer(width: Int, height: Int) {
        frameBuffer.dispose()
        frameBuffer = FrameBuffer(
            Format.RGBA8888,
            (width * scale).toInt(),
            (height * scale).toInt(),
            false)

    }

}