float sd3Sphere(vec3 p, float s, vec3 ro) {
    return length(ro - p)-s;
}