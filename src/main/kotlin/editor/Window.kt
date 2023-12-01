package editor

import SceneTest
import elements.Scene
import elements.SceneParser
import misc.MAX_OBJECTS
import misc.PATH
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import org.lwjgl.system.MemoryUtil
import utils.Time
import java.io.File


open class Window private constructor() {
    private val width = 960
    private val height = 450
    private val title = "Raymarching Engine"
    private var glfwWindow: Long = 0

    private var scene: Scene = SceneTest()
    private val camera = scene.camera
    private val parser = SceneParser(scene)
    private var shaderProgram = 0

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
        //GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE)

        // Création de la fenêtre
        glfwWindow = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL)
        check(glfwWindow != MemoryUtil.NULL) { "Failed to create GLFW window." }

        GLFW.glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback)
        GLFW.glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback)
        GLFW.glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback)
        GLFW.glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback)

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


        val vertexShader = """
            #version 330 core
            layout (location=0) in vec3 aPos;
            layout (location=1) in vec4 aColor;
            out vec4 fColor;
            void main() {       
                fColor = aColor;
                gl_Position = vec4(aPos, 1.0);
            }
        """.trimIndent()



        parser.rebuildObjects()
        var shaderCode = parser.initialize()
        shaderCode += parser.computeScene() + "\n"
        shaderCode += parser.computeMaterials() + "\n"
        shaderCode += parser.computeMapper() + "\n"
        shaderCode += File(PATH + "shaders/frag.glsl").readText()


        val vertexID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER)
        GL20.glShaderSource(vertexID, vertexShader)
        GL20.glCompileShader(vertexID)
        var success = GL20.glGetShaderi(vertexID, GL20.GL_COMPILE_STATUS)
        if (success == GL20.GL_FALSE) {
            val len = GL20.glGetShaderi(vertexID, GL20.GL_INFO_LOG_LENGTH)
            System.err.println("ERROR: Vertex shader compilation failed.")
            System.err.println(GL20.glGetShaderInfoLog(vertexID, len))
            assert(false) { "" }
        }

        val fragmentID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER)
        GL20.glShaderSource(fragmentID, shaderCode)
        GL20.glCompileShader(fragmentID)
        success = GL20.glGetShaderi(fragmentID, GL20.GL_COMPILE_STATUS)
        if (success == GL20.GL_FALSE) {
            val len = GL20.glGetShaderi(fragmentID, GL20.GL_INFO_LOG_LENGTH)
            System.err.println("ERROR: Fragment shader compilation failed.")
            System.err.println(GL20.glGetShaderInfoLog(fragmentID, len))
            assert(false) { "" }
        }

        shaderProgram = GL20.glCreateProgram()
        GL20.glAttachShader(shaderProgram, vertexID)
        GL20.glAttachShader(shaderProgram, fragmentID)
        GL20.glLinkProgram(shaderProgram)
        success = GL20.glGetProgrami(shaderProgram, GL20.GL_LINK_STATUS)
        if (success == GL20.GL_FALSE) {
            val len: Int = GL20.glGetProgrami(shaderProgram, GL20.GL_INFO_LOG_LENGTH)
            System.err.println("ERROR: Linking of shaders failed.")
            System.err.println(GL20.glGetProgramInfoLog(shaderProgram, len))
            assert(false) { "" }
        }


    }

    private fun loop() {
        var beginTime = Time.getTime().toFloat()
        var endTime: Float
        var dt = -1f
        var time = 0f

        val vertexArray = floatArrayOf(
            // position               // color
            1f, -1f, 0.0f,        1.0f, 0.0f, 0.0f, 1.0f,  // Bottom right 0
            -1f, 1f, 0.0f,        0.0f, 1.0f, 0.0f, 1.0f,  // Top left     1
            1f, 1f, 0.0f,         1.0f, 1.0f, 0.0f, 1.0f,  // Top right    2
            -1f, -1f, 0.0f,       0.0f, 0.0f, 0.0f, 1.0f   // Bottom left  3
        )

        val elementArray = intArrayOf(
            2, 1, 0,  // Top right triangle
            0, 1, 3 // bottom left triangle
        )

        val vaoID = GL30.glGenVertexArrays()
        GL30.glBindVertexArray(vaoID)
        val vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.size)
        vertexBuffer.put(vertexArray).flip()

        val vboID = GL20.glGenBuffers()
        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, vboID)
        GL20.glBufferData(GL20.GL_ARRAY_BUFFER, vertexBuffer, GL20.GL_STATIC_DRAW)

        val elementBuffer = BufferUtils.createIntBuffer(elementArray.size)
        elementBuffer.put(elementArray).flip()

        val eboID = GL20.glGenBuffers()
        GL20.glBindBuffer(GL20.GL_ELEMENT_ARRAY_BUFFER, eboID)
        GL20.glBufferData(GL20.GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL20.GL_STATIC_DRAW)

        val positionsSize = 3
        val colorSize = 4
        val floatSizeBytes = 4
        val vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes
        GL20.glVertexAttribPointer(0, positionsSize, GL20.GL_FLOAT, false, vertexSizeBytes, 0)
        GL20.glEnableVertexAttribArray(0)

        //val test = positionsSize * floatSizeBytes
        GL20.glVertexAttribPointer(1, colorSize, GL20.GL_FLOAT, false, vertexSizeBytes, (positionsSize * floatSizeBytes).toLong())
        GL20.glEnableVertexAttribArray(1)


        GL20.glUseProgram(shaderProgram);
        // Bind the VAO that we're using
        GL30.glBindVertexArray(vaoID);


        while (!GLFW.glfwWindowShouldClose(glfwWindow)) {
            GLFW.glfwPollEvents()
            GL11.glClearColor(.1f, 0f, 0f, 1f)
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)

            fun loc(loc: String) = GL20.glGetUniformLocation(shaderProgram, loc)

            GL20.glUniform1i(GL20.glGetUniformLocation(shaderProgram, "SCENE_SIZE"), MAX_OBJECTS)
            parser.updateShaderObjects(shaderProgram)
            // Update les input du shader
            GL20.glUniform1f(loc("u_time"), time)
            val l = camera.transform.location
            val r = camera.transform.rotation.toRadians()
            GL20.glUniform3f(loc("camera_pos"), 0f, l.y, 0f)
            GL20.glUniform1f(loc("w"), l.w)
            GL20.glUniform3f(loc("camera_rot"), r.roll, r.pitch, r.yaw)
            val width = IntArray(1)
            val height = IntArray(1)
            GLFW.glfwGetFramebufferSize(glfwWindow, width, height)
            GL20.glUniform2f(loc("u_screenSize"), width[0].toFloat(), height[0].toFloat())


            GL20.glDrawElements(GL20.GL_TRIANGLES, elementArray.size, GL20.GL_UNSIGNED_INT, 0)





            if (dt >= 0)
                scene.update(dt)

            GLFW.glfwSwapBuffers(glfwWindow)
            endTime = Time.getTime().toFloat()
            dt = endTime - beginTime
            beginTime = endTime
            time += dt

        }
        // Unbind everything
        GL20.glDisableVertexAttribArray(0)
        GL20.glDisableVertexAttribArray(1)

        GL30.glBindVertexArray(0)
        GL20.glUseProgram(0)
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
