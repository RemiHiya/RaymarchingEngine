package editor

import SceneTest
import elements.Scene
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.system.MemoryUtil
import utils.Time

open class Window private constructor() {
    private val width = 960
    private val height = 450
    private val title = "Raymarching Engine"
    private var glfwWindow: Long = 0

    private var scene: Scene = SceneTest()

    fun run() {
        init()
        loop()

        // Clear la mémoire
        glfwFreeCallbacks(glfwWindow)
        GLFW.glfwDestroyWindow(glfwWindow)

        GLFW.glfwTerminate()
        GLFW.glfwSetErrorCallback(null)?.free()
    }

    fun init() {
        // Callback des erreurs
        GLFWErrorCallback.createPrint(System.err).set()

        // Init de GLFW
        check(GLFW.glfwInit()) { "Unable to initialize GLFW." }

        // Config de GLFW
        GLFW.glfwDefaultWindowHints()
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE)
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE)
        GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE)

        // Création de la fenêtre
        glfwWindow = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL)
        check(glfwWindow != MemoryUtil.NULL) { "Failed to create GLFW window." }

        GLFW.glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        GLFW.glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        GLFW.glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        GLFW.glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // Contexte OpenGL
        GLFW.glfwMakeContextCurrent(glfwWindow)

        // V-sync
        GLFW.glfwSwapInterval(1)

        // Affiche la fenêtre
        GLFW.glfwShowWindow(glfwWindow)

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities()
    }

    private fun loop() {
        var beginTime = Time.getTime().toFloat()
        var endTime = Time.getTime().toFloat()
        var dt = -1f

        while (!GLFW.glfwWindowShouldClose(glfwWindow)) {
            GLFW.glfwPollEvents()
            GL11.glClearColor(.1f, 0f, 0f, 1f)
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)

            if (dt >= 0)
                scene.update(dt)
            println(1f/dt)

            GLFW.glfwSwapBuffers(glfwWindow)
            endTime = Time.getTime().toFloat()
            dt = endTime - beginTime
            beginTime = endTime

        }
    }

    companion object {
        private var window: Window? = null
        fun get(): Window? {
            if (window == null) {
                window = Window()
            }
            return window
        }
    }
}
