package editor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics
import imgui.ImGui
import imgui.ImGuiIO
import imgui.flag.ImGuiBackendFlags
import imgui.flag.ImGuiConfigFlags
import imgui.flag.ImGuiKey
import imgui.gl3.ImGuiImplGl3
import imgui.glfw.ImGuiImplGlfw
import org.lwjgl.glfw.GLFW.GLFW_KEY_A
import org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE
import org.lwjgl.glfw.GLFW.GLFW_KEY_C
import org.lwjgl.glfw.GLFW.GLFW_KEY_DELETE
import org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN
import org.lwjgl.glfw.GLFW.GLFW_KEY_END
import org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER
import org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE
import org.lwjgl.glfw.GLFW.GLFW_KEY_HOME
import org.lwjgl.glfw.GLFW.GLFW_KEY_INSERT
import org.lwjgl.glfw.GLFW.GLFW_KEY_KP_ENTER
import org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT
import org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_DOWN
import org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_UP
import org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT
import org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE
import org.lwjgl.glfw.GLFW.GLFW_KEY_TAB
import org.lwjgl.glfw.GLFW.GLFW_KEY_UP
import org.lwjgl.glfw.GLFW.GLFW_KEY_V
import org.lwjgl.glfw.GLFW.GLFW_KEY_X
import org.lwjgl.glfw.GLFW.GLFW_KEY_Y
import org.lwjgl.glfw.GLFW.GLFW_KEY_Z


class ImGuiLayer {

    private var imGuiGlfw = ImGuiImplGlfw()
    private var imGuiGl3 = ImGuiImplGl3()

    fun init() {
        ImGui.createContext()

        // ------------------------------------------------------------
        // Initialize ImGuiIO config
        val io: ImGuiIO = ImGui.getIO()
        io.iniFilename = "imgui.ini" // Save la position des fenêtres
        io.configFlags = ImGuiConfigFlags.NavEnableKeyboard // Navigation with keyboard
        io.backendFlags = ImGuiBackendFlags.HasMouseCursors // Mouse cursors to display while resizing windows etc.
        io.backendPlatformName = "imgui_java_impl_glfw"

        val windowHandle = (Gdx.graphics as Lwjgl3Graphics).window.windowHandle
        io.iniFilename = null
        io.getFonts().addFontDefault()
        io.getFonts().build()
        io.configFlags = ImGuiConfigFlags.NavEnableKeyboard

        // ------------------------------------------------------------
        // Keyboard mapping. ImGui will use those indices to peek into the io.KeysDown[] array.
        val keyMap = IntArray(ImGuiKey.COUNT)
        keyMap[ImGuiKey.Tab] = GLFW_KEY_TAB
        keyMap[ImGuiKey.LeftArrow] = GLFW_KEY_LEFT
        keyMap[ImGuiKey.RightArrow] = GLFW_KEY_RIGHT
        keyMap[ImGuiKey.UpArrow] = GLFW_KEY_UP
        keyMap[ImGuiKey.DownArrow] = GLFW_KEY_DOWN
        keyMap[ImGuiKey.PageUp] = GLFW_KEY_PAGE_UP
        keyMap[ImGuiKey.PageDown] = GLFW_KEY_PAGE_DOWN
        keyMap[ImGuiKey.Home] = GLFW_KEY_HOME
        keyMap[ImGuiKey.End] = GLFW_KEY_END
        keyMap[ImGuiKey.Insert] = GLFW_KEY_INSERT
        keyMap[ImGuiKey.Delete] = GLFW_KEY_DELETE
        keyMap[ImGuiKey.Backspace] = GLFW_KEY_BACKSPACE
        keyMap[ImGuiKey.Space] = GLFW_KEY_SPACE
        keyMap[ImGuiKey.Enter] = GLFW_KEY_ENTER
        keyMap[ImGuiKey.Escape] = GLFW_KEY_ESCAPE
        keyMap[ImGuiKey.KeyPadEnter] = GLFW_KEY_KP_ENTER
        keyMap[ImGuiKey.A] = GLFW_KEY_A
        keyMap[ImGuiKey.C] = GLFW_KEY_C
        keyMap[ImGuiKey.V] = GLFW_KEY_V
        keyMap[ImGuiKey.X] = GLFW_KEY_X
        keyMap[ImGuiKey.Y] = GLFW_KEY_Y
        keyMap[ImGuiKey.Z] = GLFW_KEY_Z
        io.setKeyMap(keyMap)


        // ------------------------------------------------------------
        // Fonts configuration
        // Read: https://raw.githubusercontent.com/ocornut/imgui/master/docs/FONTS.txt

        //val fontAtlas = io.getFonts()
        //val fontConfig = ImFontConfig() // Natively allocated object, should be explicitly destroyed

        // Glyphs could be added per-font as well as per config used globally like here
        //fontConfig.setGlyphRanges(fontAtlas.glyphRangesDefault)

        // Add a default font, which is 'ProggyClean.ttf, 13px'
        //fontAtlas.addFontDefault()

        // Fonts merge example
        //fontConfig.mergeMode = true // When enabled, all fonts added with this config would be merged with the previously added font
        //fontConfig.pixelSnapH = true

        //fontAtlas.addFontFromFileTTF("${PATH}resources/fonts/segoeui.ttf", 32f, fontConfig)


        //fontConfig.mergeMode = false
        //fontConfig.pixelSnapH = false

        //fontConfig.destroy(); // After all fonts were added we don't need this config more

        // ------------------------------------------------------------
        // Use freetype instead of stb_truetype to build a fonts texture
        //ImGuiFreeType.buildFontAtlas(fontAtlas, ImGuiFreeType.RasterizerFlags.LightHinting);

        // Method initializes LWJGL3 renderer.
        // This method SHOULD be called after you've initialized your ImGui configuration (fonts and so on).
        // ImGui context should be created as well.
        imGuiGlfw.init(windowHandle, true)
        imGuiGl3.init("#version 330 core")
    }

    private fun startFrame(dt: Float) {
        val io = ImGui.getIO()
        io.setDisplaySize(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        io.setDisplayFramebufferScale(1f, 1f)
        io.setMousePos(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
        io.deltaTime = dt
        imGuiGlfw.newFrame()
    }

    private fun endFrame() {
        imGuiGl3.renderDrawData(ImGui.getDrawData())
    }

    fun dispose() {
        imGuiGl3.dispose()
        ImGui.destroyContext()
    }

    fun update(dt: Float) {
        startFrame(dt)

        ImGui.newFrame()
        ImGui.showDemoWindow()
        ImGui.end()
        ImGui.render()

        endFrame()
    }


}