package elements.primitives

import elements.PrimitiveObject
import utils.Transform4
import utils.Vector4

class Sphere4(transform: Transform4) : PrimitiveObject(transform) {

    override fun collider(pos: Vector4): Float {
        return pos.length() - extra
    }

    override fun simplifiedCollider(pos: Vector4): Float {
        return collider(pos)
    }

    override fun getShaderCall(v1: String, v2: String, extra: String): String {
        return shaderCall.replace("v1", "$v1.xyzw").replace("extra", extra)
    }

    init {
        setShader("primitives/sd4Sphere.glsl")
        setShaderCall("sd4Sphere(v1, extra)") // vec4 v1, float extra
    }
}