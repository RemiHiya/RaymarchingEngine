float opUnion( float d1, float d2 ) { return min(d1,d2); }

float opSubtraction( float d1, float d2 ) { return max(-d1,d2); }

float opIntersection( float d1, float d2 ) { return max(d1,d2); }

float opSmoothUnion( float d1, float d2, float k ) {
    //float h =  max( k-abs(d1-d2), 0.0 )/k;
    //float m = h*h*h*0.5;
    //float s = m*k*(1.0/3.0);
    //return (d1<d2) ? d1-s : d2-s;
    float h = clamp( 0.5 + 0.5*(d2-d1)/k, 0.0, 1.0 );
    return mix( d2, d1, h ) - k*h*(1.0-h); }

float opSmoothSubtraction( float d1, float d2, float k ) {
    float h = clamp( 0.5 - 0.5*(d2+d1)/k, 0.0, 1.0 );
    return mix( d2, -d1, h ) + k*h*(1.0-h); }

float opSmoothIntersection( float d1, float d2, float k ) {
    float h = clamp( 0.5 - 0.5*(d2-d1)/k, 0.0, 1.0 );
    return mix( d2, d1, h ) + k*h*(1.0-h); }


float op(float d1, float d2, int type, float s) {
    if(type == 0) {
        if(s == 0) return opUnion(d1, d2);
        else return opSmoothUnion(d1, d2, s);

    } else if (type == 1) {
        if(s == 0) return opSubtraction(d1, d2);
        else return opSmoothSubtraction(d1, d2, s);

    } else if(type == 2) {
        if(s == 0) return opIntersection(d1, d2);
        else return opSmoothIntersection(d1, d2, s);

    }

    return 10000;
}

float sminCubic( float a, float b, float k ) {
    float h =  max( k-abs(a-b), 0.0 )/k;
    float m = h*h*h*0.5;
    return (a<b) ? m : 1.0-m;
}

vec3 colorOp(int type, marcher o1, marcher o2, float s) {
    if(type == 0) {
        if(s == 0) return o2.color;
        else return mix(o1.color, o2.color, sminCubic(o1.d, o2.d, s));

    } else if (type == 1) {
        if(s == 0) return o2.color;
        else return o2.color;

    } else if(type == 2) {
        if(s == 0) return o1.color;
        else return o1.color;

    }
    return vec3(0, 0, 0);
}