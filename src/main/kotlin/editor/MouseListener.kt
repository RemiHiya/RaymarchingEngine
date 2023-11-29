package editor

import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_RELEASE


class MouseListener private constructor() {
    private var scrollX = 0.0
    private var scrollY: Double = 0.0
    private var xPos = 0.0
    private var yPos: Double = 0.0
    private var lastY: Double = 0.0
    private var lastX: Double = 0.0
    private val mouseButtonPressed = BooleanArray(3)
    private var isDragging = false

    init {
        scrollX = 0.0
        this.scrollY = 0.0
        xPos = 0.0
        this.yPos = 0.0
        this.lastX = 0.0
        this.lastY = 0.0
    }


    companion object {
        private var instance: MouseListener? = null
        fun get(): MouseListener {
            if (instance == null) {
                instance = MouseListener()
            }
            return instance!!
        }

        fun mouseButtonCallback(window: Long, button: Int, action: Int, mods: Int) {
            if (action == GLFW_PRESS) {
                if (button < get().mouseButtonPressed.size) {
                    get().mouseButtonPressed[button] = true
                }
            } else if (action == GLFW_RELEASE) {
                if (button < get().mouseButtonPressed.size) {
                    get().mouseButtonPressed[button] = false
                    get().isDragging = false
                }
            }
        }

        fun mousePosCallback(window: Long, xpos: Double, ypos: Double) {
            get().lastX = get().xPos
            get().lastY = get().yPos
            get().xPos = xpos
            get().yPos = ypos
            get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2]
        }

        fun mouseScrollCallback(window: Long, xOffset: Double, yOffset: Double) {
            get().scrollX = xOffset
            get().scrollY = yOffset
        }

        fun endFrame() {
            get().scrollX = 0.0
            get().scrollY = 0.0
            get().lastX = get().xPos
            get().lastY = get().yPos
        }

        fun getX(): Float {
            return get().xPos.toFloat()
        }

        fun getY(): Float {
            return get().yPos.toFloat()
        }

        fun getDx(): Float {
            return (get().lastX - get().xPos).toFloat()
        }

        fun getDy(): Float {
            return (get().lastY - get().yPos).toFloat()
        }

        fun getScrollX(): Float {
            return get().scrollX.toFloat()
        }

        fun getScrollY(): Float {
            return get().scrollY.toFloat()
        }

        fun isDragging(): Boolean {
            return get().isDragging
        }

        fun mouseButtonDown(button: Int): Boolean {
            return if (button < get().mouseButtonPressed.size) {
                get().mouseButtonPressed[button]
            } else {
                false
            }
        }


    }
}