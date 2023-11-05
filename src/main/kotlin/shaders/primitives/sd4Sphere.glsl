float sd4Sphere(vec4 p, float s, vec4 ro) {
    return length(ro - p)-s;
}