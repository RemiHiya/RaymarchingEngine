package utils

import kotlin.math.sqrt

data class Vector3(val x: Float=0f, val y: Float=0f, val z: Float=0f) {
    constructor(vec: Vector4) : this(vec.x, vec.y, vec.z)

    operator fun plus(other: Vector3) = Vector3(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Vector3) = Vector3(x - other.x, y - other.y, z - other.z)
    operator fun times(scalar: Float) = Vector3(x * scalar, y * scalar, z * scalar)
    operator fun times(other: Vector3) = Vector3(x * other.x, y * other.y, z * other.z)
    operator fun div(scalar: Float) = Vector3(x / scalar, y / scalar, z / scalar)

    fun length() = sqrt(x * x + y * y + z * z)
    fun normalize() = this / length()
    infix fun dot(other: Vector3) = x * other.x + y * other.y + z * other.z
    infix fun cross(other: Vector3) = Vector3(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x)
    fun abs() = Vector3(kotlin.math.abs(x), kotlin.math.abs(y), kotlin.math.abs(z))

    fun toVector4() = Vector4(x, y, z, 0f)
}
