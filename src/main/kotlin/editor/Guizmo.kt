package editor

import api.math.*
import imgui.ImGui
import imgui.extension.imguizmo.ImGuizmo
import imgui.extension.imguizmo.flag.Mode
import imgui.extension.imguizmo.flag.Operation
import imgui.type.ImBoolean
import utils.Matrix4
import utils.Rotator4
import kotlin.math.tan

class Guizmo {
    companion object {
        var currentGizmoOperation = Operation.TRANSLATE
        var currentMode = Mode.LOCAL
        var USE_SNAP = ImBoolean(false)

        private val OBJECT_MATRICES = arrayOf(
            floatArrayOf(
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f
            ), floatArrayOf(
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                2f, 0f, 0f, 1f
            ), floatArrayOf(
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                2f, 0f, 2f, 1f
            ), floatArrayOf(
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 2f, 1f
            )
        )

        private val IDENTITY_MATRIX = floatArrayOf(
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f
        )


        fun update() {
            ImGuizmo.setOrthographic(false)
            ImGuizmo.setEnabled(true)
            ImGuizmo.setId(0)
            ImGuizmo.setDrawList(ImGui.getWindowDrawList())
            val windowWidth = ImGui.getWindowWidth()
            val windowHeight = ImGui.getWindowHeight()
            ImGuizmo.setRect(ImGui.getWindowPosX(), ImGui.getWindowPosY(), windowWidth, windowHeight)

            ImGuizmo.drawGrid(getView(), perspective(), IDENTITY_MATRIX, 100)
            ImGuizmo.drawCubes(getView(), perspective(), OBJECT_MATRICES[0])
            ImGuizmo.manipulate(getView(), perspective(), OBJECT_MATRICES[0], currentGizmoOperation, currentMode)
            //ImGuizmo.viewManipulate(getView(), 8f, floatArrayOf(Debug.viewportPos.x - 128, Debug.viewportPos.y), floatArrayOf(128f,128f), 0x10101010)
        }

        private fun getView(): FloatArray {
            var rot = Debug.camera.transform.rotation.toRadians()
            rot = Rotator4(-rot.pitch, rot.yaw, -rot.roll, 0f)
            val loc = Debug.camera.transform.location
            val rotationMatrix = (rotationXY(rot.yaw) * rotationXZ(rot.pitch) * rotationYZ(rot.roll))
                //.matrix.flatMap { it.toList() }

            val mat = Matrix4(
                /*rotationMatrix[0], rotationMatrix[1], rotationMatrix[2], 0f,
                rotationMatrix[4], rotationMatrix[5], rotationMatrix[6], 0f,
                rotationMatrix[8], rotationMatrix[9], rotationMatrix[10], 0f,*/
                1f,0f,0f,0f,
                0f,1f,0f,0f,
                0f,0f,1f,0f,
                loc.y, loc.z, loc.x, 1f
            ) * rotationMatrix

            val rotation = (rotationXY(rot.yaw) * rotationXZ(rot.pitch) * rotationYZ(rot.roll))
            val translation = Matrix4(
                1f,0f,0f,0f,
                0f,1f,0f,0f,
                0f,0f,1f,0f,
                loc.y, loc.z, loc.x, 1f
            )
            val result = translation * rotation


            return result.matrix.flatMap { it.toList() }.toFloatArray()
        }

        private fun perspective(): FloatArray {
            val yMax = (Debug.camera.nearClip * tan(Debug.camera.fov * Math.PI / 180f)).toFloat()
            val xMax = yMax * Debug.viewportSize.x / Debug.viewportSize.y
            return frustum(-xMax, xMax, -yMax, yMax, Debug.camera.nearClip, Debug.camera.farClip)
        }

        private fun frustum(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float): FloatArray {
            val r = FloatArray(16)
            val tmp = 2f * near
            val tmp2 = right - left
            val tmp3 = top - bottom
            val tmp4 = far - near

            r[0] = tmp / tmp2
            r[1] = 0f
            r[2] = 0.0f
            r[3] = 0.0f

            r[4] = 0.0f
            r[5] = tmp / tmp3
            r[6] = 0.0f
            r[7] = 0.0f

            r[8] = (right + left) / tmp2
            r[9] = (top + bottom) / tmp3
            r[10] = (-far - near) / tmp4
            r[11] = -1.0f

            r[12] = 0.0f
            r[13] = 0.0f
            r[14] = -tmp * far / tmp4
            r[15] = 0.0f
            return r
        }
    }
}