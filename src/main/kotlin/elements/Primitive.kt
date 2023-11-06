package elements

import utils.MarcherOperator
import utils.Vector4

abstract class Primitive(private var material: String) {

    lateinit var v1: Vector4
    lateinit var v2: Vector4
    var extra: Float = 0.0f

    var index = 0
    lateinit var operator: MarcherOperator

    private lateinit var shader: String
    lateinit var shaderCall: String

    fun getShader() = shader
    fun getMaterial() = material

    abstract fun getShaderCall(v1: String, v2: String, extra: String): String

    fun setShader(new : String): String {
        shader = new
        return new
    }

    fun setShaderCall(new: String): String {
        shaderCall = new
        return new
    }

    fun setMaterial(new: String): String {
        material = new
        return new
    }

    fun setPrimitiveParameters(v2: Vector4, extra: Float) {
        this.v2 = v2
        this.extra = extra
    }

    fun setObjectIndex(new: Int) {
        index = new
    }

    fun setObjectOperator(new: MarcherOperator) {
        operator = new
    }
}