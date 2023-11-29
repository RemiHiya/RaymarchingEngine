import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.utils.GdxRuntimeException
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import elements.SceneParser
import imgui.ImGui
import imgui.app.Application
import imgui.app.Configuration
import imgui.flag.ImGuiConfigFlags
import imgui.gl3.ImGuiImplGl3
import imgui.glfw.ImGuiImplGlfw
import imgui.type.ImString
import misc.PATH
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11
import ui.MainEditor
import utils.RmFloat
import kotlin.properties.Delegates

class EditorApp : Application() {
    private var scene = SceneTest()
    private var spriteBatch: SpriteBatch? = null
    private var texture: Texture? = null
    private lateinit var shaderProgram: ShaderProgram

    private lateinit var viewport: Viewport
    private lateinit var editor: MainEditor

    private var time = 0f
    var scale = RmFloat(0.3f)
    private lateinit var frameBuffer: FrameBuffer
    private val parser = SceneParser(scene)


    private val camera = scene.camera

    override fun configure(config: Configuration) {
        config.title = "Dear ImGui is Awesome!"
    }

    override fun init(config: Configuration?) {
        super.init(config)
        /*ImGui.createContext()
        val io = ImGui.getIO()
        io.configFlags = io.configFlags or ImGuiConfigFlags.DockingEnable or ImGuiConfigFlags.ViewportsEnable

        ImGui.styleColorsDark()*/

    }


    override fun process() {



        ImGui.text("Hello, world %d")

    }






    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(EditorApp())
        }
    }
}