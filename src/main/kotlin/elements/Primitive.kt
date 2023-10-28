package elements

import utils.Vector4
import java.util.Vector

abstract class Primitive(private var material: String) {

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



}