package elements.primitives

import elements.PrimitiveObject
import utils.Transform4

class Sphere(transform: Transform4, material: String = "vec3(0,1,0)") : PrimitiveObject(transform, material) {

    override fun getShaderCall(v1: String, v2: String, extra: String): String {
        return shaderCall.replace("v1", "$v1.xyz").replace("extra", extra)
    }

    init {
        setShader("primitives/sd3Sphere.glsl")
        setShaderCall("sd3Sphere(v1, extra, ro)") // vec3 p, float s
    }
}

class Sphere4(transform: Transform4, material: String = "vec3(0,1,0)") : PrimitiveObject(transform, material) {

    override fun getShaderCall(v1: String, v2: String, extra: String): String {
        return shaderCall.replace("v1", "$v1.xyzw").replace("extra", extra)
    }

    init {
        setShader("primitives/sd4Sphere.glsl")
        setShaderCall("sd4Sphere(v1, extra, ro)") // vec4 p, float s
    }
}