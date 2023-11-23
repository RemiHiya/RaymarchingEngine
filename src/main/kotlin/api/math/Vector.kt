package api.math

import utils.Rotator3
import utils.Vector3
import kotlin.math.cos
import kotlin.math.sin


fun Rotator3.getForwardVector(): Vector3 {
    val radians = toRadians()
    val x = cos(radians.yaw) * cos(radians.pitch)
    val y = -sin(radians.yaw) * cos(radians.pitch)
    val z = sin(radians.pitch)
    return Vector3(x, y, z)
}

fun Rotator3.getRightVector(): Vector3 {
    val r = toRadians()
    val x = cos(r.roll) * sin(r.yaw)
    val y = cos(r.roll) * cos(r.yaw)
    val z = 0f
    return Vector3(x, y, z)
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