package api.math

import utils.Rotator3
import utils.Rotator4
import utils.Vector3
import utils.Vector4
import kotlin.math.cos
import kotlin.math.sin


fun Rotator4.getForwardVector(): Vector4 {
    val r = toRadians()
    val x = cos(r.yaw) * cos(r.w) * cos(r.pitch)
    val y = -sin(r.yaw) * cos(r.w) * cos(r.roll) + cos(r.yaw) * cos(r.w) * sin(r.pitch) * sin(r.roll)
    val z = -sin(r.yaw) * cos(r.w) * sin(r.roll) + cos(r.yaw) * cos(r.w) * cos(r.roll) * sin(r.pitch)
    val w = sin(r.w)
    return Vector4(x, y, z, w)
}

fun Rotator4.getRightVector(): Vector4 {
    val r = toRadians()
    val x = sin(r.yaw) * cos(r.pitch)
    val y = cos(r.roll) * cos(r.yaw) + sin(r.roll) * sin(r.yaw) * sin(r.pitch)
    val z = -sin(r.roll) * cos(r.yaw) + cos(r.roll) * sin(r.yaw) * sin(r.pitch)
    return Vector4(x, y, z, 0f)
}

fun Rotator4.getUpVector(): Vector4 {
    val r = toRadians()
    val x = -sin(r.pitch)
    val y = sin(r.roll) * cos(r.pitch)
    val z = cos(r.roll) * cos(r.pitch)
    return Vector4(x, y, z, 0f)
}

fun Rotator4.getWVector(): Vector4 {
    val r = toRadians()
    val x = -cos(r.yaw) * sin(r.w) * cos(r.pitch)
    val y = cos(r.roll) * sin(r.yaw) * sin(r.w) - sin(r.roll) * cos(r.yaw) * sin(r.w) * sin(r.pitch)
    val z = -sin(r.roll) * sin(r.yaw) * sin(r.w) - cos(r.roll) * cos(r.yaw) * sin(r.w) * sin(r.pitch)
    val w = cos(r.w)
    return Vector4(x, y, z, w)
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
    return Vector3(x, y, z)
    //return getForwardVector().cross(getUpVector())
}

fun Rotator3.getUpVector(): Vector3 {
    val radians = toRadians()
    val x = -cos(radians.yaw) * cos(radians.roll) * sin(radians.pitch) + sin(radians.yaw) * sin(radians.roll)
    val y = sin(radians.yaw) * cos(radians.roll) * sin(radians.pitch) + cos(radians.yaw) * sin(radians.roll)
    val z = cos(radians.pitch) * cos(radians.roll)
    return Vector3(x, y, z)
}
/*
fun Rotator3.getForwardVector(): Vector3 {
    val rot = this.toRadians()
    val x =  cos(rot.pitch) * sin(rot.yaw)
    val y = -sin(rot.pitch)
    val z =  cos(rot.pitch) * cos(rot.yaw)
    return Vector3(z, y, x)
}

fun Rotator3.getRightVector(): Vector3 {
    val rot = this.toRadians()
    val x =  cos(rot.yaw)
    val y =  0f
    val z = -sin(rot.yaw)
    return Vector3(z, y, x)
}

fun Rotator3.getUpVector(): Vector3 {
    val rot = this.toRadians()
    val x = sin(rot.pitch) * sin(rot.yaw)
    val y = cos(rot.pitch)
    val z = sin(rot.pitch) * cos(rot.yaw)
    return Vector3(x, y, z)
}*/