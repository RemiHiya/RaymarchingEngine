package elements.primitives

import elements.PrimitiveObject
import utils.Transform4
import utils.Vector4

class Mandelbulb(transform: Transform4) : PrimitiveObject(transform) {

    override fun collider(pos: Vector4): Float {
        return pos.toVector3().length() - extra
    }

    override fun simplifiedCollider(pos: Vector4): Float {
        return collider(pos)
    }

    override fun getShaderCall(v1: String, v2: String, extra: String): String {
        val a = "$v1.xyz"
        return shaderCall.replace("v1", a).replace("extra", extra)
    }

    init {
        setShader("primitives/sd3mandelbulb.glsl")
        setShaderCall("sd3mandelbulb(v1, extra)") // vec3 p, float s
    }
}
