package elements.primitives

import elements.PrimitiveObject
import utils.Transform4
import utils.Vector4

class Sphere(transform: Transform4) : PrimitiveObject(transform) {

    override fun collider(pos: Vector4): Float {
        return pos.toVector3().length() - extra
    }

    override fun simplifiedCollider(pos: Vector4): Float {
        return collider(pos)
    }

    override fun getShaderCall(v1: String, v2: String, extra: String): String {
        return shaderCall.replace("v1", "$v1.xyz").replace("extra", extra)
    }

    init {
        setShader("primitives/sd3Sphere.glsl")
        setShaderCall("sd3Sphere(v1, extra)") // vec3 v1, float extra
    }
}