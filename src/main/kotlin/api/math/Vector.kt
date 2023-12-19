package api.math

import utils.Rotator3
import utils.Rotator4
import utils.Vector3
import utils.Vector4
import kotlin.math.*


fun max(a: Vector4, b: Vector4): Vector4 {
    return Vector4(
        max(a.x, b.x),
        max(a.y, b.y),
        max(a.z, b.z),
        max(a.w, b.w)
    )
}

fun min(a: Vector4, b: Vector4): Vector4 {
    return Vector4(
        min(a.x, b.x),
        min(a.y, b.y),
        min(a.z, b.z),
        min(a.w, b.w)
    )
}

fun max(a: Vector3, b: Vector3): Vector3 {
    return Vector3(
        max(a.x, b.x),
        max(a.y, b.y),
        max(a.z, b.z)
    )
}

fun min(a: Vector3, b: Vector3): Vector3 {
    return Vector3(
        min(a.x, b.x),
        min(a.y, b.y),
        min(a.z, b.z)
    )
}

fun Vector4.mirrorByNormal(n: Vector4): Vector4 {
    val normal = n.normalize()
    return this - normal * (2f * this.dot(normal))
}

fun distance(a: Vector4, b: Vector4): Float {
    return (a-b).length()
}

fun distanceSquared(a: Vector4, b: Vector4): Float {
    return (b.x-a.x).pow(2) + (b.y-a.y).pow(2) + (b.z-a.z).pow(2) + (b.w-a.w).pow(2)
}

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

