float sd4Box(in vec4 p, in vec4 b, vec4 ro) {
    vec4 d = abs(p-ro) - b;
    return min( max(max(d.x,d.y),max(d.z,d.w)),0.0) + length(max(d,0.0));
}