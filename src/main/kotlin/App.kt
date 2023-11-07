import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Pixmap.Format
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.utils.GdxRuntimeException
import elements.Scene
import elements.SceneParser
import misc.PATH
import kotlin.math.sin


class App(private val scene: Scene) : ApplicationAdapter() {
    private var spriteBatch: SpriteBatch? = null
    private var texture: Texture? = null
    private var shaderProgram: ShaderProgram? = null
    private var time = 0f
    private val parser = SceneParser(scene)

    private fun tick(deltaTime: Float) {
        //println(1/deltaTime)
        /*
        TODO : Scene tick
         */
    }

    override fun create() {
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

        if (!shaderProgram!!.isCompiled) {
            throw GdxRuntimeException("Shader compilation failed: ${shaderProgram!!.log}")
        }

    }

    override fun render() {
        tick(Gdx.graphics.deltaTime)

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        spriteBatch!!.begin()
        shaderProgram?.bind()
        time += Gdx.graphics.deltaTime

        // Update la liste d'objets du shader
        shaderProgram?.setUniformi("SCENE_SIZE", scene.getObjects().size)
        parser.updateShaderObjects(shaderProgram)

        // Update les input du shader
        shaderProgram?.setUniformf("u_time", time)
        shaderProgram?.setUniformf("w", sin(time))
        shaderProgram?.setUniformf("u_screenSize",(Gdx.graphics.width).toFloat(),
            (Gdx.graphics.height).toFloat())
        spriteBatch!!.shader = shaderProgram
        spriteBatch!!.draw(texture, -1f, -1f)
        spriteBatch!!.end()
    }

    override fun dispose() {
        spriteBatch?.dispose()
        texture?.dispose()
        shaderProgram?.dispose()
    }

}