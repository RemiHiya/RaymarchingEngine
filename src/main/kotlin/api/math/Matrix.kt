package api.math

import utils.Matrix4
import utils.Rotator4
import utils.Vector4
import kotlin.math.cos
import kotlin.math.sin


operator fun Matrix4.times(other: Matrix4): Matrix4 {
    val result = Matrix4(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
    for (i in 0..3) {
        for (j in 0..3) {
            result.matrix[i][j] =
                matrix[i][0] * other.matrix[0][j] +
                        matrix[i][1] * other.matrix[1][j] +
                        matrix[i][2] * other.matrix[2][j] +
                        matrix[i][3] * other.matrix[3][j]
        }
    }
    return result
}

operator fun Matrix4.times(vector: Vector4): Vector4 {
    val result = FloatArray(4)
    for (i in 0..3) {
        result[i] =
            matrix[i][0] * vector.x +
                    matrix[i][1] * vector.y +
                    matrix[i][2] * vector.z +
                    matrix[i][3] * vector.w
    }
    return Vector4(result[0], result[1], result[2], result[3])
}

fun rotationXY(angle: Float): Matrix4 {
    val cosA = cos(angle)
    val sinA = sin(angle)

    return Matrix4(
        cosA, sinA, 0f, 0f,
        -sinA, cosA, 0f, 0f,
        0f, 0f, 1f, 0f,
        0f, 0f, 0f, 1f
    )
}

fun rotationXZ(angle: Float): Matrix4 {
    val cosA = cos(angle)
    val sinA = sin(angle)

    return Matrix4(
        cosA, 0f, -sinA, 0f,
        0f, 1f, 0f, 0f,
        sinA, 0f, cosA, 0f,
        0f, 0f, 0f, 1f
    )
}

fun rotationYZ(angle: Float): Matrix4 {
    val cosA = cos(angle)
    val sinA = sin(angle)

    return Matrix4(
        1f, 0f, 0f, 0f,
        0f, cosA, sinA, 0f,
        0f, -sinA, cosA, 0f,
        0f, 0f, 0f, 1f
    )
}

fun rotationXW(angle: Float): Matrix4 {
    val cosA = cos(angle)
    val sinA = sin(angle)

    return Matrix4(
        cosA, 0f, 0f, -sinA,
        0f, 1f, 0f, 0f,
        0f, 0f, 1f, 0f,
        sinA, 0f, 0f, cosA
    )
}

fun rotationYW(angle: Float): Matrix4 {
    val cosA = cos(angle)
    val sinA = sin(angle)

    return Matrix4(
        1f, 0f, 0f, 0f,
        0f, cosA, 0f, -sinA,
        0f, 0f, 1f, 0f,
        0f, sinA, 0f, cosA
    )
}

fun rotationZW(angle: Float): Matrix4 {
    val cosA = cos(angle)
    val sinA = sin(angle)

    return Matrix4(
        1f, 0f, 0f, 0f,
        0f, 1f, 0f, 0f,
        0f, 0f, cosA, -sinA,
        0f, 0f, sinA, cosA
    )
}

fun rotation(rotator: Rotator4): Matrix4 {
    val rotationXY = rotationXY(rotator.yaw)
    val rotationXZ = rotationXZ(rotator.pitch)
    val rotationYZ = rotationYZ(rotator.roll)
    val rotationXW = rotationXW(rotator.w)
    val rotationYW = rotationYW(rotator.w)
    val rotationZW = rotationZW(rotator.w)
    return rotationXY * rotationXZ * rotationYZ * rotationXW * rotationYW * rotationZW
}
