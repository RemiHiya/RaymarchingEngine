package elements.primitives

import elements.Primitive
import elements.PrimitiveObject
import utils.Transform4
import utils.Vector3
import utils.Vector4

class Cube(transform: Transform4, material: String = "") : PrimitiveObject(transform, material) {

    override fun getShaderCall(v1: String, v2: String, extra: String): String {
        val a = "$v1.xyz"
        val b = "$v2.xyz"
        return shaderCall.replace("v1", a).replace("v2", b)
    }

    init {
        setShader("primitives/sd3Box.glsl")
        setShaderCall("sd3Box(v1, v2, ro)") // vec3 p, vec3 b
    }
}

class Cube4(transform: Transform4, material: String = "") : PrimitiveObject(transform, material) {

    override fun getShaderCall(v1: String, v2: String, extra: String): String {
        val a = "$v1.xyzw"
        val b = "$v2.xyzw"
        return shaderCall.replace("v1", a).replace("v2", b)
    }

    init {
        setShader("primitives/sd4Box.glsl")
        setShaderCall("sd4Box(v1, v2, ro)") // vec3 p, vec3 b
    }

}