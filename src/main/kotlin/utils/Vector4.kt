package utils

import kotlin.math.sqrt
import kotlin.math.abs

data class Vector4(var x: Float = 0f, var y: Float = 0f, var z: Float = 0f, var w: Float = 0f) {
    constructor(vec: Vector3) : this(vec.x, vec.y, vec.z, 0f)

    operator fun plus(other: Vector4) = Vector4(x + other.x, y + other.y, z + other.z, w + other.w)
    operator fun minus(other: Vector4) = Vector4(x - other.x, y - other.y, z - other.z, w - other.w)
    operator fun times(scalar: Float) = Vector4(x * scalar, y * scalar, z * scalar, w * scalar)
    operator fun times(other: Vector4) = Vector4(x * other.x, y * other.y, z * other.z, w * other.w)
    operator fun div(scalar: Float) = Vector4(x / scalar, y / scalar, z / scalar, w / scalar)

    fun length() = sqrt(x*x + y*y + z*z + w*w)
    fun normalize() = this / length()
    infix fun dot(other: Vector4) = x * other.x + y * other.y + z * other.z
    fun abs() = Vector4(abs(x), abs(y), abs(z), abs(w))

    override fun toString(): String {
        return "vec4($x, $y, $z, $w)"
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Vector4) {
            x==other.x && y==other.y && z==other.z && w==other.w
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        result = 31 * result + w.hashCode()
        return result
    }

    fun toVector3() = Vector3(x, y, z)
}
