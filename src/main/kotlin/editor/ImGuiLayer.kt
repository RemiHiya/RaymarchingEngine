package editor

import App
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics
import elements.Actor
import elements.Scene
import imgui.ImFontConfig
import imgui.ImGui
import imgui.flag.*
import imgui.gl3.ImGuiImplGl3
import imgui.glfw.ImGuiImplGlfw
import imgui.type.ImBoolean
import misc.RESOURCE_PATH
import org.lwjgl.glfw.GLFW.*
import java.io.File


class ImGuiLayer(private val scene: Scene) {

    private var imGuiGlfw = ImGuiImplGlfw()
    private var imGuiGl3 = ImGuiImplGl3()

    private var selection: Actor? = null

    private var layout = ""
    private var reloadLayout = false

    var viewportX = 100f
    var viewportY = 100f
    var resized = false
    var focused = false

    fun init() {
        ImGui.createContext()

        // ------------------------------------------------------------
        // Initialize ImGuiIO config
        val io = ImGui.getIO()
        io.iniFilename = "imgui.ini" // Save la position des fenêtres
        io.configFlags = ImGuiConfigFlags.NavEnableKeyboard or ImGuiConfigFlags.DockingEnable
        ImGui.loadIniSettingsFromDisk(io.iniFilename)
        io.backendFlags = ImGuiBackendFlags.HasMouseCursors // Mouse cursors to display while resizing windows etc.
        io.backendPlatformName = "imgui_java_impl_glfw"
        io.configWindowsMoveFromTitleBarOnly = true

        val windowHandle = (Gdx.graphics as Lwjgl3Graphics).window.windowHandle
        io.iniFilename = null
        io.getFonts().addFontDefault()
        io.getFonts().build()
        //io.configFlags = io.configFlags or ImGuiConfigFlags.NavEnableKeyboard

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

        val fontAtlas = io.getFonts()
        val fontConfig = ImFontConfig() // Natively allocated object, should be explicitly destroyed

        // Glyphs could be added per-font as well as per config used globally like here
        fontConfig.setGlyphRanges(fontAtlas.glyphRangesDefault)

        // Add a default font, which is 'ProggyClean.ttf, 13px'
        //fontAtlas.addFontDefault()

        // Fonts merge example
        //fontConfig.mergeMode = true // When enabled, all fonts added with this config would be merged with the previously added font
        //fontConfig.pixelSnapH = true

        val customFont = fontAtlas.addFontFromFileTTF("${RESOURCE_PATH}/fonts/segoeui.ttf", 20f, fontConfig)
        io.setFontDefault(customFont)


        //fontConfig.mergeMode = false
        //fontConfig.pixelSnapH = false

        fontConfig.destroy() // After all fonts were added we don't need this config more

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
        if (reloadLayout) {
            ImGui.loadIniSettingsFromDisk(layout)
            reloadLayout = false
        }
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

    fun update(dt: Float, textureID: Int) {
        startFrame(dt)


        ImGui.newFrame()
        setupDockspace()
        menuBar()
        ImGui.showDemoWindow()

        ImGui.begin("Inspector")
        selection?.display()
        ImGui.end()

        worldSettings()

        val flags = ImGuiWindowFlags.NoScrollbar or ImGuiWindowFlags.NoScrollWithMouse

        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0f, 0f)
        ImGui.begin("Viewport", flags)
        ImGui.popStyleVar(1)
        ImGui.image(textureID, ImGui.getWindowSizeX(), ImGui.getWindowSizeY())
        resized = ImGui.getWindowSizeX() != viewportX || ImGui.getWindowSizeY() != viewportY
        viewportX = ImGui.getWindowSizeX()
        viewportY = ImGui.getWindowSizeY()
        focused = ImGui.isWindowHovered(ImGuiWindowFlags.NoTitleBar)
        ImGui.end()

        outliner(scene.getActors())


        ImGui.end()
        ImGui.render()

        endFrame()
    }

    private fun setupDockspace() {
        var windowFlags = ImGuiWindowFlags.MenuBar or ImGuiWindowFlags.NoDocking

        ImGui.setNextWindowPos(0.0f, 0.0f, ImGuiCond.Always)
        ImGui.setNextWindowSize(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0f)
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0f)
        windowFlags = windowFlags or (ImGuiWindowFlags.NoTitleBar or ImGuiWindowFlags.NoCollapse or
                ImGuiWindowFlags.NoResize or ImGuiWindowFlags.NoMove or
                ImGuiWindowFlags.NoBringToFrontOnFocus or ImGuiWindowFlags.NoNavFocus)

        ImGui.begin("Dockspace", ImBoolean(true), windowFlags)
        ImGui.popStyleVar(2)

        ImGui.dockSpace(ImGui.getID("Dockspace"))
    }

    private fun menuBar() {
        if (ImGui.beginMenuBar()) {
            if (ImGui.beginMenu("File")) {
                if (ImGui.menuItem("Ouvrir")) {
                    // Traitement pour l'option "Ouvrir"
                }
                if (ImGui.menuItem("Enregistrer")) {
                    // Traitement pour l'option "Enregistrer"
                }
                if (ImGui.menuItem("Quit", "Alt+F4")) {
                }
                ImGui.endMenu()
            }

            if (ImGui.beginMenu("Window")) {
                if (ImGui.beginMenu("Layout")) {
                    if (ImGui.beginMenu("Load")) {
                        // Récupère les fichiers présents dans le dossier layout
                        val files = File(RESOURCE_PATH + "layouts").listFiles()
                        if (files != null) {
                            if (files.isEmpty()) {
                                ImGui.menuItem("No layout found.", "", false, false)
                            } else {
                                for (i in files) {
                                    if (ImGui.menuItem(i.name)) {
                                        layout = RESOURCE_PATH + "layouts/" + i.name
                                        // Flag pour que la layout soit chargée à la prochaine frame
                                        reloadLayout = true
                                    }
                                }
                            }

                        }

                        ImGui.endMenu()
                    }
                    if (ImGui.menuItem("Option 2")) {
                        // Traitement pour l'option "Option 2" dans le sous-menu
                    }
                    ImGui.endMenu()
                }
                ImGui.endMenu()
            }




            ImGui.endMenuBar()
        }
    }

    private fun outliner(actors: Array<Actor>) {
        ImGui.begin("Outliner")
        for (actor in actors) {
            //val flags = if (actor.children.isNotEmpty()) ImGuiTreeNodeFlags.OpenOnArrow else ImGuiTreeNodeFlags.Leaf
            var flags = if (actor==selection) ImGuiTreeNodeFlags.Selected else ImGuiTreeNodeFlags.None
            flags = flags or ImGuiTreeNodeFlags.OpenOnDoubleClick or ImGuiTreeNodeFlags.OpenOnArrow
            val isOpen = ImGui.treeNodeEx(actor.displayName, flags)

            if (ImGui.isItemClicked()) {
                selection = actor
            }
            if (isOpen) {
                //renderActorTree(actor.children)
                ImGui.text(actor.displayName)
                ImGui.treePop()
            }
        }
        ImGui.end()
    }

    private fun worldSettings() {
        ImGui.begin("World Settings")
        if (ImGui.collapsingHeader("Camera", ImGuiTreeNodeFlags.DefaultOpen)) {
            UiElements.vector4Field(scene.camera.transform.location, "Location")
            UiElements.rotator4Field(scene.camera.transform.rotation, "Rotation")
        }

        if (ImGui.collapsingHeader("Viewport", ImGuiTreeNodeFlags.DefaultOpen)) {
            val value = floatArrayOf(App.getRenderScale()*100f)
            if (ImGui.sliderFloat("Render scale", value, 5f, 100f, "%.0f%%")) {
                if (App.getRenderScale() != value[0]/100f) {
                    App.setRenderScale(value[0]/100f)
                    App.resizeFrameBuffer(viewportX.toInt(), viewportY.toInt())
                }

            }
        }

        ImGui.end()
    }


}