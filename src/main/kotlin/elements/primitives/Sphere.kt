package elements.primitives

import elements.PrimitiveObject
import utils.Transform

class Sphere(transform: Transform, material: String) : PrimitiveObject(transform, material) {

    override fun getShaderCall(v1: String, v2: String, extra: String): String {
        return shaderCall.replace("v1", "$v1.xyz").replace("extra", extra)
    }

    init {
        setShader("primitives/sd3Sphere.glsl")
        setShaderCall("sd3Sphere(v1, extra, ro)") // vec3 p, float s
    }
}