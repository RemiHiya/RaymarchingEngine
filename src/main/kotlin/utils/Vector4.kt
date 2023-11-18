package utils

import kotlin.math.sqrt
import kotlin.math.max

data class Vector4(val x: Float = 0f, val y: Float = 0f, val z: Float = 0f, val w: Float = 0f) {
    operator fun plus(other: Vector4) = Vector4(x + other.x, y + other.y, z + other.z, w + other.w)
    operator fun minus(other: Vector4) = Vector4(x - other.x, y - other.y, z - other.z, w - other.w)
    operator fun times(scalar: Float) = Vector4(x * scalar, y * scalar, z * scalar, w * scalar)
    operator fun div(scalar: Float) = Vector4(x / scalar, y / scalar, z / scalar, w / scalar)

    fun length() = sqrt(x*x + y*y + z*z + w*w)
    fun normalize() = this / length()
    fun dot(other: Vector4) = x * other.x + y * other.y + z * other.z
    fun abs() = Vector4(kotlin.math.abs(x), kotlin.math.abs(y), kotlin.math.abs(z), kotlin.math.abs(w))
    fun max(other: Vector4): Vector4 {
        return Vector4(
            max(this.x, other.x),
            max(this.y, other.y),
            max(this.z, other.z),
            max(this.w, other.w)
        )
    }

    override fun toString(): String {
        return "vec4($x, $y, $z, $w)"
    }
}
