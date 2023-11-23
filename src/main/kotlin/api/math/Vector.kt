package api.math

import utils.Rotator3
import utils.Vector3
import java.lang.Math.toRadians
import kotlin.math.cos
import kotlin.math.sin

fun Rotator3.getForwardVector(): Vector3 {
    val rot = this.toRadians()
    val x =  cos(rot.pitch) * sin(rot.yaw)
    val y = -sin(rot.pitch)
    val z =  cos(rot.pitch) * cos(rot.yaw)
    return Vector3(x, y, z)
}

fun Rotator3.getRightVector(): Vector3 {
    val rot = this.toRadians()
    val x =  cos(rot.yaw)
    val y =  0f
    val z = -sin(rot.yaw)
    return Vector3(x, y, z)
}

fun Rotator3.getUpVector(): Vector3 {
    val rot = this.toRadians()
    val x = sin(rot.pitch) * sin(rot.yaw)
    val y = cos(rot.pitch)
    val z = sin(rot.pitch) * cos(rot.yaw)
    return Vector3(x, y, z)
}