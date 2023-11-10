#version 330 core
#define MAX_OBJECTS 128
uniform int SCENE_SIZE;
struct obj {
    vec4 v1;
    vec4 v2;
    vec4 rot;
    float extra;
    int shader;
    int material;
    int operator;
    float smoothness;
}; uniform obj objects[MAX_OBJECTS];

struct marcher {
    float d;
    vec3 color;
};

int instructions = 0;

vec4 rot(vec4 pos, vec4 rot) {
    float t = rot.w;
    mat4 xy = mat4(1, 0, 0, 0,
    0, 1, 0, 0,
    0, 0, cos(t), -sin(t),
    0, 0, sin(t), cos(t));

    t = rot.x;
    mat4 yz = mat4(cos(t), 0, 0, sin(t),
    0, 1, 0, 0,
    0, 0, 1, 0,
    -sin(t), 0, 0, cos(t));

    t = rot.y;
    mat4 xz = mat4(1, 0, 0, 0,
    0, cos(t), 0, -sin(t),
    0, 0, 1, 0,
    0, sin(t), cos(t), 1);

    t = rot.z;
    mat4 xw = mat4(1, 0, 0, 0,
    0, cos(t), sin(t), 0,
    0, -sin(t), cos(t), 0,
    0, 0, 0, 1);

    t = rot.y;
    mat4 yw = mat4(cos(t), 0, -sin(t), 0,
    0, 1, 0, 0,
    sin(t), 0, cos(t), 0,
    0, 0, 0, 1);

    t = rot.x;
    mat4 zw = mat4(cos(t), sin(t), 0, 0,
    -sin(t), cos(t), 0, 0,
    0, 0, 1, 0,
    0, 0, 0, 1);


    return xy*yz*xz*xw*yw*zw * pos;
}float opUnion( float d1, float d2 ) { return min(d1,d2); }

float opSubtraction( float d1, float d2 ) { return max(-d1,d2); }

float opIntersection( float d1, float d2 ) { return max(d1,d2); }

float opSmoothUnion( float d1, float d2, float k ) {
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
}float sd3Box(vec3 p, vec3 b, vec4 ro) {
    vec3 q = abs(p-ro.xyz) - b;
    return length(max(q,0.0)) + min(max(q.x,max(q.y,q.z)),0.0);
}
vec3 material0(){return vec3(.3);}vec3 material1(){return vec3(0,1,0);}
marcher map(vec4 ro) {
    float m=10000;vec3 color; vec3 c;
    for(int i=0; i<SCENE_SIZE; i++){
        float d;
        if(objects[i].material == 0) {c = material0();}
        else if(objects[i].material == 1) {c = material1();}
        if(objects[i].shader == 0) {
            d = sd3Box(rot(objects[i].v1, objects[i].rot).xyz, objects[i].v2.xyz, ro);
        }
        color = colorOp(objects[i].operator, marcher(m,color), marcher(d,c), objects[i].smoothness);m = op(d, m, objects[i].operator, objects[i].smoothness);}return marcher(m, color);}
#define MIN_DIST 1
#define SHADOW_FALLOFF .02
#define DRAW_DIST 500


float rand(vec2 coord) {
    return fract(sin(dot(coord.xy, vec2(12.9898,78.233))) * 43758.5453);
} // Fonction pseudo random


in vec2 TexCoord;
uniform vec2 u_screenSize;
uniform vec3 camera_pos = vec3(0, 0, 0);
uniform vec3 camera_rot = vec3(0, 0, 0);
uniform float w = 0;
uniform vec3 light = normalize(vec3(0, 2, 1));
out vec4 outputColor;
out vec4 test;

float FOV = 110;

vec3 computeDirection(vec2 coords, vec3 rotation) {
    vec2 normalizedCoords = (2.0 * coords - u_screenSize) / u_screenSize;
    float aspectRatio = u_screenSize.x / u_screenSize.y;
    float near = 0.1;
    float far = 100.0;
    float fov = radians(FOV);
    float yScale = 1.0 / tan(fov / 2.0);
    float xScale = yScale * aspectRatio;
    float depth = (2.0 * near * far) / (far + near - normalizedCoords.y * (far - near));
    //vec3 direction = normalize(vec3(normalizedCoords.x*xScale*depth, normalizedCoords.y*yScale*depth, -depth));
    vec3 direction = normalize(vec3(depth, normalizedCoords.x*xScale*depth, normalizedCoords.y*yScale*depth));

    mat3 yawMatrix = mat3(
    cos(rotation.z), -sin(rotation.z), 0.0,
    sin(rotation.z), cos(rotation.z), 0.0,
    0.0, 0.0, 1.0);
    mat3 pitchMatrix = mat3(
    cos(rotation.y), 0.0, sin(rotation.y),
    0.0, 1.0, 0.0,
    -sin(rotation.y), 0.0, cos(rotation.y));
    mat3 rollMatrix = mat3(
    1.0, 0.0, 0.0,
    0.0, cos(rotation.x), -sin(rotation.x),
    0.0, sin(rotation.x), cos(rotation.x));
    direction = yawMatrix * pitchMatrix * rollMatrix * direction;

    return direction;
}

marcher castRay(vec4 ro, vec3 rd) {
    float epsilon = 0.001;
    float maxDistance = 100.0;
    float distance = 0.0;

    vec2 result;
    result.y = -1;

    for (int i = 0; i < 10000; i++) {
        instructions++;
        vec3 p = ro.xyz + rd * distance;
        marcher objectDistance = map(vec4(p, ro.w));
        distance += objectDistance.d;
        if (abs(objectDistance.d) < epsilon) {
            return marcher(distance, objectDistance.color);
        }
        else if(distance >= maxDistance) {
            return marcher(-1, vec3(0,0,0));
        }
    }

    return marcher(-1, vec3(0,0,0));
}


vec3 GetSurfaceNormal(in vec4 p) {
    const float h = 0.01; // replace by an appropriate value
    const vec2 k = vec2(1,-1);
    vec3 pos = p.xyz;
    return normalize( k.xyy*map( vec4(pos + k.xyy*h, p.w) ).d +
    k.yyx*map( vec4(pos + k.yyx*h, p.w) ).d +
    k.yxy*map( vec4(pos + k.yxy*h, p.w) ).d +
    k.xxx*map( vec4(pos + k.xxx*h, p.w) ).d );
}

float softshadow(in vec4 ro, in vec3 rd, float w) {
    float mint = 0.03;
    float maxt = 10;
    float res = 1.0;
    float t = mint;
    for( int i=0; i<256 && t<maxt; i++ ) {
        float h = map(vec4(ro.xyz + t*rd, ro.w)).d;
        res = min( res, h/(w*t) );
        t += clamp(h, 0.005, 0.50);
        if( res<-1.0 || t>maxt ) break;
    }
    res = max(res,-1.0);
    return 0.25*(1.0+res)*(1.0+res)*(2.0-res);
}

vec3 render(vec2 coords) {
    vec3 col;
    vec3 direction = computeDirection(coords, camera_rot);
    marcher m = castRay(vec4(camera_pos, w), direction);
    float t = m.d;
    vec3 objectSurfaceColour = m.color;

    vec3 pos = camera_pos + direction*t;
    vec3 n = GetSurfaceNormal(vec4(pos, w));


    if(t == -1 || t > DRAW_DIST) {
        // Skybox
        col = vec3(0.30, 0.36, 0.60) - (direction.y * .2);
    } else {


        // Ombrage simple
        float NoL = 0.2 + 0.8 * max(dot(n, light), 0); // 0.2 et 0.8 pour remap
        vec3 LDirectional = vec3(0.9, 0.9, 0.8) * NoL;
        vec3 LAmbient = vec3(0.03, 0.04, 0.1);
        vec3 diffuse = objectSurfaceColour * (LDirectional + LAmbient);
        col = vec3(diffuse);
        /*
        // Ombres projetées
        float shadow = 0.0;
        float shadowRayCount = 2.0;
        for(float i = 0.0; i < shadowRayCount; i++) {
            vec3 shadowRayOrigin = pos + n * 0.01;
            float r = rand(vec2(direction.xy+i)) * 2.0 - 1.0;
            vec3 shadowRayDir = light + vec3(1.0 * SHADOW_FALLOFF) * r;
            float shadowRayIntersection = castRay(shadowRayOrigin, shadowRayDir);
            if (shadowRayIntersection != -1.0)
            {
                shadow = 1/max(1, (shadowRayCount-1)*shadowRayIntersection);
            }
        }
        vec3 s = shadow * (LDirectional-LAmbient);
        col = mix(col, col*.0, s);*/
        //col = col * (1-s) + col * 0.0 * s;
        //col = LDirectional;

        float softshadow = 1-softshadow(vec4(pos, w), light, .3);
        col = mix(col, objectSurfaceColour*LAmbient, softshadow);

    }

    col = pow(col, vec3(0.4545)); // Gamma correction
    return col;
}


void main() {
    //objects
    vec3 col;

    // Anti aliasing
    float AA_size = 2.0;
    float count = 0.0;
    for (float aaY = 0.0; aaY < AA_size; aaY++)
    {
        for (float aaX = 0.0; aaX < AA_size; aaX++)
        {
            col += render(gl_FragCoord.xy + vec2(aaX, aaY) / AA_size);
            count += 1.0;
        }
    }
    col /= count;


    outputColor = vec4(col, 1.0);
    //outputColor = vec4(vec3(instructions, 0, 0)/500, 1.0);
}
