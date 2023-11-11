float sd3Box(vec3 p, vec3 b, vec4 ro) {
    //p = (p-ro.xyz)* rotateX(sin(u_time));
    vec3 q = abs(p) - b;
    return length(max(q,0.0)) + min(max(q.x,max(q.y,q.z)),0.0);
}