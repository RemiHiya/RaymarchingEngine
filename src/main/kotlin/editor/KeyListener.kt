package editor

import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_RELEASE


class KeyListener() {
    private val keyPressed = BooleanArray(350)



    companion object {
        private var instance: KeyListener? = null
        fun get(): KeyListener {
            if (instance == null) {
                instance = KeyListener()
            }
            return instance!!
        }

        fun keyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
            if (action == GLFW_PRESS) {
                get().keyPressed[key] = true
            } else if (action == GLFW_RELEASE) {
                get().keyPressed[key] = false
            }
        }

        fun isKeyPressed(keyCode: Int): Boolean {
            return get().keyPressed[keyCode]
        }

    }

}