package editor

import api.math.rotation
import api.math.times
import api.math.toRadians
import elements.CameraActor
import imgui.ImGui
import utils.*
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
        fun Vector4.project(center: Vector4=Vector4()): Vector3 {
            val cameraW = camera.transform.location.w
            val wDifference = (this.w) - center.w
            val factor = if (wDifference != 0f) 1f / wDifference else 0f

            val x = (this.x - center.x) * factor
            val y = (this.y - center.y) * factor
            val z = (this.z - center.z) * factor

            return Vector3(x, y, z) * (this.w-cameraW) / 2f + center.toVector3()
        }
        fun Vector3.project(): Vector2 {
            val relativePosition = this.toVector4() - camera.transform.location
            val transformed = rotation(camera.transform.rotation.toRadians()).inverse() * relativePosition
            val dist = transformed.x
            val aspectRatio = viewportX / viewportY
            val tanHalfFOV = tan(FOV / 2f)
            // TODO : Utiliser la FOV dans la projection
            val projection = Matrix4(
                0f, 1f / dist, 0f, 0f,
                0f, 0f, -1f / dist, 0f,
                0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f
            )
            val new = projection * transformed
            //if (transformed.dot(Vector4(1f)) > 0)
            //    return Vector2(-1f, -1f)
            return Vector2(new.x, new.y)
        }

        fun drawPoint(pos: Vector4) {
            val t = pos.project().project().toScreen()
            val dist = (pos - camera.transform.location).length()
            val size = -dist/10f + 1f
            ImGui.getForegroundDrawList().addCircleFilled(t.x, t.y, size*5f, 0xFF0000FF.toInt(), 10)
            //ImGui.getForegroundDrawList().addLine(0f, 0f, t.x, t.y, 0xFFFFFFFF.toInt(), 2f)
        }
        fun drawLine(a: Vector4, b: Vector4, thickness: Float = 2f, center: Vector4) {
            val a1 = a.project(center).project().toScreen()
            val b1 = b.project(center).project().toScreen()
            ImGui.getForegroundDrawList().addLine(a1.x, a1.y, b1.x, b1.y, 0xFF0000FF.toInt(), thickness)
        }

        fun drawCube(center: Vector4, bounds: Vector4, rotation: Rotator4) {
            val rom = listOf(
                Vector4(-1f, -1f, -1f, 1f), Vector4(1f, -1f, -1f, 1f),
                Vector4(-1f, 1f, -1f, 1f), Vector4(1f, 1f, -1f, 1f),
                Vector4(-1f, -1f, 1f, 1f), Vector4(1f, -1f, 1f, 1f),
                Vector4(-1f, 1f, 1f, 1f), Vector4(1f, 1f, 1f, 1f),
                Vector4(-1f, -1f, -1f, -1f), Vector4(1f, -1f, -1f, -1f),
                Vector4(-1f, 1f, -1f, -1f), Vector4(1f, 1f, -1f, -1f),
                Vector4(-1f, -1f, 1f, -1f), Vector4(1f, -1f, 1f, -1f),
                Vector4(-1f, 1f, 1f, -1f), Vector4(1f, 1f, 1f, -1f)
            )
            val anglePairs = listOf(
                Pair(0, 1), Pair(0, 2), Pair(1, 3), Pair(2, 3),
                Pair(4, 5), Pair(4, 6), Pair(5, 7), Pair(6, 7),
                Pair(0, 4), Pair(1, 5), Pair(2, 6), Pair(3, 7),
                Pair(0, 8), Pair(1, 9), Pair(2, 10), Pair(3, 11),
                Pair(4, 12), Pair(5, 13), Pair(6, 14), Pair(7, 15),
                Pair(8, 9), Pair(8, 10), Pair(9, 11), Pair(10, 11),
                Pair(12, 13), Pair(12, 14), Pair(13, 15), Pair(14, 15),
                Pair(8, 12), Pair(9, 13), Pair(10, 14), Pair(11, 15)
            )

            for ((index1, index2) in anglePairs) {
                var pos1 = rom[index1]*bounds + center
                var pos2 = rom[index2]*bounds + center
                pos1 = rotation(rotation.toRadians()) * pos1
                pos2 = rotation(rotation.toRadians()) * pos2
                drawLine(pos1, pos2, center=center)
            }
        }
    }
}