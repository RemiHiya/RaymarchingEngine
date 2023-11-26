package api.math

import utils.Rotator3
import utils.Rotator4
import utils.Vector3
import utils.Vector4
import kotlin.math.cos
import kotlin.math.sin


fun Vector4.rotate(rotator: Rotator4): Vector4 {
    return rotation(rotator.toRadians()) * this
}

fun Rotator4.getForwardVector(): Vector4 {
    return Vector4(1f, 0f, 0f, 0f).rotate(this)
}

fun Rotator4.getRightVector(): Vector4 {
    return Vector4(0f, 1f, 0f, 0f).rotate(this)
}

fun Rotator4.getUpVector(): Vector4 {
    return Vector4(0f, 0f, 1f, 0f).rotate(this)
}

fun Rotator4.getWVector(): Vector4 {
    return Vector4(0f, 0f, 0f, 1f).rotate(this)
}

fun Rotator3.getForwardVector(): Vector3 {
    val radians = toRadians()
    val x = cos(radians.yaw) * cos(radians.pitch)
    val y = -sin(radians.yaw) * cos(radians.pitch)
    val z = sin(radians.pitch)
    return Vector3(x, y, z)
}

fun Rotator3.getRightVector(): Vector3 {
    val r = toRadians()
    val x = sin(r.yaw) * cos(r.pitch)
    val y = cos(r.roll) * cos(r.yaw) + sin(r.roll) * sin(r.yaw) * sin(r.pitch)
    val z = -sin(r.roll) * cos(r.yaw) + cos(r.roll) * sin(r.yaw) * sin(r.pitch)
    //return Vector3(x, y, z)
    return getForwardVector().cross(getUpVector()) * -1f
}

fun Rotator3.getUpVector(): Vector3 {
    val radians = toRadians()
    val x = -cos(radians.yaw) * cos(radians.roll) * sin(radians.pitch) + sin(radians.yaw) * sin(radians.roll)
    val y = sin(radians.yaw) * cos(radians.roll) * sin(radians.pitch) + cos(radians.yaw) * sin(radians.roll)
    val z = cos(radians.pitch) * cos(radians.roll)
    return Vector3(x, y, z)
}

