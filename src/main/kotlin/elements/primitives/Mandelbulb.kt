package elements.primitives

import elements.Primitive
import elements.PrimitiveObject
import utils.Transform4
import utils.Vector3
import utils.Vector4

class Mandelbulb(transform: Transform4) : PrimitiveObject(transform) {

    override fun getShaderCall(v1: String, v2: String, extra: String): String {
        val a = "$v1.xyz"
        return shaderCall.replace("v1", a).replace("extra", extra)
    }

    init {
        setShader("primitives/sd3mandelbulb.glsl")
        setShaderCall("sd3mandelbulb(v1, extra)") // vec3 p, float s
    }
}
