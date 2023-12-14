package editor

import api.math.rotation
import api.math.times
import api.math.toRadians
import elements.CameraActor
import imgui.ImGui
import utils.*
import kotlin.math.atan

class Debug {
    companion object {
        var viewportX = 0f
        var viewportY = 0f
        var viewportPosX = 0f
        var viewportPosY = 0f
        lateinit var camera: CameraActor
        private var objects: MutableList<() -> Unit> = mutableListOf()

        fun debugAll() {
            objects.forEach { obj -> obj.invoke() }
            objects.clear()
        }

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
            val ro = Rotator4(camera.transform.rotation.roll, camera.transform.rotation.pitch, camera.transform.rotation.yaw, 0f)
            val transformed = rotation(ro.toRadians()).inverse() * relativePosition

            val near = camera.nearClip
            val far = camera.farClip
            val fov = camera.fov.toRadians()
            val aspectRatio = viewportX / (viewportY)
            val atanHalfFOV = atan(fov / 2f)
            val scaleY = 1f / atanHalfFOV
            val scaleX = (1f/ atanHalfFOV) / aspectRatio
            val scaleZ = -(far + near) / (far - near)
            val offsetZ = -2f * far * near / (far - near)

            val dist = transformed.x - (offsetZ * scaleZ)
            val projection = Matrix4(
                0f, 1f / dist*scaleX, 0f, 0f,
                0f, 0f, -1f / dist*scaleY, 0f,
                0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f
            )
            val new = projection * transformed
            if (transformed.dot(Vector4(1f)) < 0)
                return Vector2(-1f, -1f)
            return Vector2(new.x, new.y+.07f)
        }

        fun drawPoint(pos: Vector4) {
            objects.add {
                val t = pos.project().project().toScreen()
                val dist = (pos - camera.transform.location).length()
                val size = -dist/10f + 1f
                ImGui.getForegroundDrawList().addCircleFilled(t.x, t.y, size*5f, 0xFF0000FF.toInt(), 10)
            }

        }
        fun drawLine(a: Vector4, b: Vector4, thickness: Float = 2f, center: Vector4) {
            objects.add {
                val a1 = a.project(center).project().toScreen()
                val b1 = b.project(center).project().toScreen()
                ImGui.getForegroundDrawList().addLine(a1.x, a1.y, b1.x, b1.y, 0xFF0000FF.toInt(), thickness)
            }
        }
        fun drawLine(a: Vector3, b: Vector3, thickness: Float = 2f) {
            objects.add {
                val a1 = a.project().toScreen()
                val b1 = b.project().toScreen()
                ImGui.getForegroundDrawList().addLine(a1.x, a1.y, b1.x, b1.y, 0xFF0000FF.toInt(), thickness)
            }
        }

        fun drawHyperCube(center: Vector4, bounds: Vector4, rotation: Rotator4) {
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
                var pos1 = rom[index1] * bounds + center
                var pos2 = rom[index2] * bounds + center
                pos1 = rotation(rotation.toRadians()) * pos1
                pos2 = rotation(rotation.toRadians()) * pos2
                drawLine(pos1, pos2, center=center)
            }
        }
        fun drawCube(center: Vector3, bounds: Vector3, rotation: Rotator3) {
            val rom = listOf(
                Vector3(-1f, -1f, -1f), Vector3(1f, -1f, -1f),
                Vector3(-1f, 1f, -1f), Vector3(1f, 1f, -1f),
                Vector3(-1f, -1f, 1f), Vector3(1f, -1f, 1f),
                Vector3(-1f, 1f, 1f), Vector3(1f, 1f, 1f)
            )
            val anglePairs = listOf(
                Pair(0, 1), Pair(0, 2), Pair(1, 3), Pair(2, 3),
                Pair(4, 5), Pair(4, 6), Pair(5, 7), Pair(6, 7),
                Pair(0, 4), Pair(1, 5), Pair(2, 6), Pair(3, 7)
            )

            for ((index1, index2) in anglePairs) {
                var pos1 = (rom[index1] * bounds).toVector4()
                var pos2 = (rom[index2] * bounds).toVector4()
                val ro = Rotator4(rotation.roll, rotation.pitch, rotation.yaw, 0f)
                pos1 = rotation(ro.toRadians()) * pos1 + center.toVector4()
                pos2 = rotation(ro.toRadians()) * pos2 + center.toVector4()
                drawLine(pos1.toVector3(), pos2.toVector3())
            }
        }
    }
}