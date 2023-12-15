package utils

import kotlin.math.PI

data class Rotator3(var roll: Float, var pitch: Float, var yaw: Float) {
    fun toRadians() = Rotator3(
        roll*PI.toFloat()/180,
        pitch*PI.toFloat()/180,
        yaw*PI.toFloat()/180)

    operator fun plus(other: Rotator3) = Rotator3(roll+other.roll, pitch+other.pitch, yaw+other.yaw)
    operator fun minus(other: Rotator3) = Rotator3(roll-other.roll, pitch-other.pitch, yaw-other.yaw)
    operator fun rem(mod: Float) = Rotator3(roll%mod, pitch%mod, yaw%mod)
    fun toRotator4() = Rotator4(roll, pitch, yaw, 0f)
}

data class Rotator4(var roll: Float, var pitch: Float, var yaw: Float, var w: Float) {
    fun toRadians() = Rotator4(
        roll*PI.toFloat()/180,
        pitch*PI.toFloat()/180,
        yaw*PI.toFloat()/180,
        w*PI.toFloat()/180)

    fun toRotator3() = Rotator3(roll, pitch, yaw)
    operator fun rem(mod: Float) = Rotator4(roll%mod, pitch%mod, yaw%mod, w%mod)
    operator fun plus(other: Rotator4) = Rotator4(roll+other.roll, pitch+other.pitch, yaw+other.yaw, w+other.w)
    operator fun minus(other: Rotator4) = Rotator4(roll-other.roll, pitch-other.pitch, yaw-other.yaw, w-other.w)
    operator fun times(b: Float) = Rotator4(roll*b, pitch*b, yaw*b, w*b)
    operator fun times(b: Vector4) = Rotator4(roll*b.x, pitch*b.y, yaw*b.z, w*b.w)
}