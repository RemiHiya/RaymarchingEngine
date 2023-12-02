import api.math.getForwardVector
import api.math.getRightVector
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
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
import editor.ImGuiLayer
import elements.Scene
import elements.SceneParser
import imgui.ImGui
import misc.PATH
import misc.SKIN
import utils.RmFloat
import utils.Vector4


class App(private val scene: Scene) : ApplicationAdapter() {
    private var spriteBatch: SpriteBatch? = null
    private var texture: Texture? = null
    private lateinit var shaderProgram: ShaderProgram

    private lateinit var viewport: Viewport

    private var time = 0f
    var scale = RmFloat(0.3f)
    private lateinit var frameBuffer: FrameBuffer
    private val parser = SceneParser(scene)

    private val camera = scene.camera

    private val layer = ImGuiLayer(scene)

    private fun tick(deltaTime: Float) {

        // Déplacements de caméra
        if (layer.focused && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            // Lock la souris
            /*if (!Gdx.input.isCursorCatched)
                Gdx.input.isCursorCatched = true*/

            // Input pour rotation
            val deltaX = -Gdx.input.deltaX.toFloat()
            val deltaY = -Gdx.input.deltaY.toFloat()
            val rotationSpeed = 0.2f
            camera.transform.rotation.yaw += deltaX * rotationSpeed
            camera.transform.rotation.pitch += deltaY * rotationSpeed

            // Input pour déplacement
            val cameraSpeed = deltaTime * 3
            var dir = Vector4()
            if (Gdx.input.isKeyPressed(Input.Keys.Z))
                dir += camera.transform.rotation.getForwardVector()
            if (Gdx.input.isKeyPressed(Input.Keys.S))
                dir += camera.transform.rotation.getForwardVector() * -1f
            if (Gdx.input.isKeyPressed(Input.Keys.Q))
                dir += camera.transform.rotation.getRightVector() * -1f
            if (Gdx.input.isKeyPressed(Input.Keys.D))
                dir += camera.transform.rotation.getRightVector()
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
                dir += Vector4(0f, 0f, 1f, 0f)
            if (Gdx.input.isKeyPressed(Input.Keys.F))
                dir += Vector4(0f, 0f, -1f, 0f)

            camera.transform.location.x += dir.x*cameraSpeed
            camera.transform.location.y += dir.y*cameraSpeed
            camera.transform.location.z += dir.z*cameraSpeed
            camera.transform.location.w += dir.w*cameraSpeed

        } else {
            if (Gdx.input.isCursorCatched)
                Gdx.input.isCursorCatched = false
        }
        /*
        TODO : Scene tick
         */
    }

    override fun create() {
        layer.init()

        viewport = ScreenViewport()

        /*
        S'occupe de générer le shader
         */

        frameBuffer = FrameBuffer(
            Format.RGBA8888,
            (layer.viewportX * scale.value).toInt(),
            (layer.viewportY * scale.value).toInt(),
            false)

        spriteBatch = SpriteBatch()
        val width = layer.viewportX.toInt()
        val height = layer.viewportY.toInt()

        val pixmap = Pixmap(width, height, Format.RGBA8888)
        texture = Texture(pixmap)

        val vertexShader = """
            attribute vec4 a_position;
            void main() {
                gl_Position = a_position;
            }
        """.trimIndent()

        parser.rebuildObjects()
        var shaderCode = parser.initialize()
        shaderCode += parser.computeScene() + "\n"
        shaderCode += parser.computeMaterials() + "\n"
        shaderCode += parser.computeMapper() + "\n"
        shaderCode += Gdx.files.internal(PATH + "shaders/frag.glsl").readString()
        //println(shaderCode)

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
            frameBuffer.width.toFloat()/scale.value,
            frameBuffer.height.toFloat()/scale.value)

        spriteBatch!!.begin()
        shaderProgram.bind()
        time += Gdx.graphics.deltaTime

        // Update la liste d'objets du shader
        shaderProgram.setUniformi("SCENE_SIZE", scene.getObjects().size)
        parser.updateShaderObjects(shaderProgram)

        // Update les input du shader
        shaderProgram.setUniformf("u_time", time)
        val l = camera.transform.location
        val r = camera.transform.rotation.toRadians()
        shaderProgram.setUniformf("camera_pos", l.x, l.y, l.z)
        shaderProgram.setUniformf("w", l.w)
        shaderProgram.setUniformf("camera_rot", r.roll, r.pitch, r.yaw)
        shaderProgram.setUniformf("u_screenSize", frameBuffer.width.toFloat(), frameBuffer.height.toFloat())

        spriteBatch!!.shader = shaderProgram
        spriteBatch!!.draw(texture, -1f, -1f, 2/scale.value, 2/scale.value)
        spriteBatch!!.end()

        frameBuffer.end()

        spriteBatch!!.begin()
        spriteBatch!!.shader = null
        texture!!.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        spriteBatch!!.draw(frameBuffer.colorBufferTexture, 0f, 0f,
            layer.viewportX,
            layer.viewportY)
        spriteBatch!!.end()

        /*
        Rendu de l'éditeur
         */
        layer.update(Gdx.graphics.deltaTime, frameBuffer.colorBufferTexture.textureObjectHandle)
        if (layer.resized)
            resizeFrameBuffer(layer.viewportX.toInt(), layer.viewportY.toInt())

    }

    override fun dispose() {
        super.dispose()
        spriteBatch?.dispose()
        texture?.dispose()
        shaderProgram.dispose()
        frameBuffer.dispose()
        SKIN.dispose()
        ImGui.saveIniSettingsToDisk("imgui.ini")
        layer.dispose()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        viewport.update(width, height)
        //resizeFrameBuffer(width, height)
        ImGui.getIO().setDisplaySize(width.toFloat(), height.toFloat())
    }

    fun resizeFrameBuffer(width: Int, height: Int) {
        frameBuffer.dispose()
        frameBuffer = FrameBuffer(Format.RGBA8888, (width*scale.value).toInt(), (height*scale.value).toInt(), false)
    }

}