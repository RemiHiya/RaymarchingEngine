package editor

import api.math.rotation
import api.math.times
import api.math.toRadians
import elements.CameraActor
import imgui.ImGui
import utils.*
import kotlin.math.atan
import kotlin.math.pow

class Debug {
    companion object {
        var viewportSize = Vector2()
        var viewportPos = Vector2()
        lateinit var camera: CameraActor
        private var objects: MutableList<() -> Unit> = mutableListOf()

        fun debugAll() {
            objects.forEach { it.invoke() }
            objects.clear()
        }

        private fun Vector2.toScreen(): Vector2 {
            return (this*0.5f + Vector2(0.5f, 0.5f)) * Vector2(viewportSize.x, viewportSize.y) + Vector2(viewportPos.x, viewportPos.y)
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
            val aspectRatio = viewportSize.x / viewportSize.y
            val atanHalfFOV = atan(fov / 2f)
            val scaleY = 1f / atanHalfFOV
            val scaleX = (1f/ atanHalfFOV) / aspectRatio
            val scaleZ = -(near-far) / (near-far)
            val offsetZ = -2f * far * near / (near-far)

            val dist = transformed.x + (offsetZ * scaleZ)
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
                ImGui.getWindowDrawList().addCircleFilled(t.x, t.y, 5f, 0xFF0000FF.toInt(), 10)
            }
        }
        fun drawPoint(pos: Vector3) {
            objects.add {
                val t = pos.project().toScreen()
                val dist = (pos - camera.transform.location.toVector3()).length()
                val size = -dist/10f + 1f
                ImGui.getWindowDrawList().addCircleFilled(t.x, t.y, 5f, 0xFF0000FF.toInt(), 10)
            }
        }
        fun drawScreenLine(a: Vector2, b: Vector2, thickness: Float) {
            ImGui.getWindowDrawList().addLine(a.x, a.y, b.x, b.y, 0xFF0000FF.toInt(), thickness)
        }
        fun drawLine(a: Vector4, b: Vector4, thickness: Float = 2f, center: Vector4) {
            objects.add {
                val a1 = a.project(center).project().toScreen()
                val b1 = b.project(center).project().toScreen()
                val dist = ((a+b)/2f - camera.transform.location).toVector3().length()
                val size = -(dist/2f).pow(7f) / 1f + 1
                drawScreenLine(a1, b1, thickness*size)
            }
        }
        fun drawLine(a: Vector3, b: Vector3, thickness: Float = 5f) {
            objects.add {
                val a1 = a.project().toScreen()
                val b1 = b.project().toScreen()
                val dist = ((a+b)/2f - camera.transform.location.toVector3()).length()
                val size = -(dist/2f).pow(7f) / 1f + 1
                drawScreenLine(a1, b1, thickness*size)
            }
        }

        fun drawHyperCube(center: Vector4, bounds: Vector4, rotation: Rotator4) {
            DebugShapes.draw4D(DebugShapes.hyperCube, center, bounds, rotation)
            { pos1, pos2 -> drawLine(pos1, pos2, center = center) }
        }
        fun drawCube(center: Vector3, bounds: Vector3, rotation: Rotator3) {
            DebugShapes.draw3D(DebugShapes.cube, center, bounds, rotation)
            { pos1, pos2 -> drawLine(pos1, pos2) }
        }
        fun drawSphere(center: Vector3, bounds: Vector3, rotation: Rotator3) {
            DebugShapes.draw3D(DebugShapes.sphere, center, bounds, rotation)
            { pos1, pos2 -> drawLine(pos1, pos2) }
        }
        fun drawHyperSphere(center: Vector4, bounds: Vector4, rotation: Rotator4) {
            DebugShapes.draw4D(DebugShapes.hyperSphere, center, bounds, rotation)
            { pos1, pos2 -> drawLine(pos1, pos2, center=center) }
        }
    }
}