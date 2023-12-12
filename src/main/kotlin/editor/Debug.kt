package editor

import api.math.times
import api.math.toRadians
import elements.CameraActor
import imgui.ImGui
import utils.Matrix4
import utils.Vector2
import utils.Vector3
import utils.Vector4
import kotlin.math.tan

class Debug {
    companion object {
        var viewportX = 0f
        var viewportY = 0f
        var viewportPosX = 0f
        var viewportPosY = 0f
        lateinit var camera: CameraActor
        var FOV = 110f.toRadians()

        private fun Vector2.toScreen(): Vector2 {
            return (this*0.5f + Vector2(0.5f, 0.5f)) * Vector2(viewportX, viewportY) + Vector2(viewportPosX, viewportPosY)
        }
        fun Vector4.project(): Vector3 {
            val aspectRatio = viewportX/viewportY
            val near = 0.1f
            val far = 100.0f

            val tanHalfFOV = tan(FOV / 2.0f)
            val projection = Matrix4(
                1.0f / (aspectRatio * tanHalfFOV), 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f / tanHalfFOV, 0.0f, 0.0f,
                0.0f, 0.0f, -(far + near) / (far - near), -1.0f,
                0.0f, 0.0f, -(2.0f * far * near) / (far - near), 0.0f
            )

            val new = projection * (this - camera.transform.location)
            return new.toVector3()
        }
        fun Vector3.project(): Vector2 {
            val aspectRatio = viewportX/viewportY
            val tanHalfFOV = tan(FOV / 2.0f)
            val near = 0.1f
            val far = 100.0f
            val projection = Matrix4(
                1.0f / (aspectRatio * tanHalfFOV), 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f / tanHalfFOV, 0.0f, 0.0f,
                0.0f, 0.0f, -(far + near) / (far - near), -1.0f,
                0.0f, 0.0f, -2.0f * far * near / (far - near), 0.0f
            )

            val new = projection * (this.toVector4() - camera.transform.location)
            return Vector2(new.y/new.w, new.x/new.w)
        }

        fun drawPoint(pos: Vector4) {
            val t = pos.toVector3().project().toScreen()
            ImGui.getForegroundDrawList().addCircleFilled(t.x, t.y, 10f, 0xFF0000FF.toInt(), 10)
            //ImGui.getForegroundDrawList().addLine(0f, 0f, t.x, t.y, 0xFFFFFFFF.toInt(), 2f)
        }
    }
}