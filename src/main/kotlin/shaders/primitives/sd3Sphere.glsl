float sd3Sphere(vec3 p, float s, vec4 ro) {
    return length(ro.xyz - p)-s;
}