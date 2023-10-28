float sd3Box(vec3 p, vec3 b, vec3 ro) {
    vec3 q = abs(p-ro) - b;
    return length(max(q,0.0)) + min(max(q.x,max(q.y,q.z)),0.0);
}