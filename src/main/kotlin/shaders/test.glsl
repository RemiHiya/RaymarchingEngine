#version 330 core
#define MAX_OBJECTS 2
struct obj {
    vec4 v1;
    vec4 v2;
    float extra;
    int shader;
    int material;
};
obj objects[MAX_OBJECTS];
float sd3Sphere(vec3 p, float s, vec3 ro) {
    return length(ro - p)-s;
}float sd3Box(vec3 p, vec3 b, vec3 ro) {
    vec3 q = abs(p-ro) - b;
    return length(max(q,0.0)) + min(max(q.x,max(q.y,q.z)),0.0);
}
float map(vec3 ro) {
    float m=10000;
    for(int i=0; i<objects.length(); i++){
        float d;
        if(objects[i].shader == 0) {
            d = sd3Sphere(objects[i].v1.xyz, objects[i].extra, ro);
        }
        else if(objects[i].shader == 1) {
            d = sd3Box(objects[i].v1.xyz, objects[i].v2.xyz, ro);
        }
        m = min(d, m);
    }
    return m;}
#define MIN_DIST 1
#define SHADOW_FALLOFF .02
#define DRAW_DIST 500

uniform int SCENE_SIZE;


float rand(vec2 coord) {
    return fract(sin(dot(coord.xy, vec2(12.9898,78.233))) * 43758.5453);
} // Fonction pseudo random

float opSmoothUnion( float d1, float d2, float k ) {
    float h = clamp( 0.5 + 0.5*(d2-d1)/k, 0.0, 1.0 );
    return mix( d2, d1, h ) - k*h*(1.0-h);
}


in vec2 TexCoord;
uniform vec2 u_screenSize;
uniform float u_time;
uniform vec3 camera_pos = vec3(0, 0, 0);
uniform vec3 light = normalize(vec3(.5, 1, 0));
out vec4 outputColor;

float FOV = 90;

vec3 computeDirection(vec2 coords) {
    vec2 normalizedCoords = (2.0 * coords - u_screenSize) / u_screenSize;
    float aspectRatio = u_screenSize.x / u_screenSize.y;
    float near = 0.1;
    float far = 100.0;
    float fov = radians(FOV);
    float yScale = 1.0 / tan(fov / 2.0);
    float xScale = yScale * aspectRatio;
    float depth = (2.0 * near * far) / (far + near - normalizedCoords.y * (far - near));
    return normalize(vec3(normalizedCoords.x * xScale * depth, normalizedCoords.y * yScale * depth, -depth));
}

float castRay(vec3 ro, vec3 rd) {
    float epsilon = 0.001;
    float maxDistance = 100.0;
    float distance = 0.0;

    for (int i = 0; i < 10000; i++) {
        vec3 p = ro + rd * distance;
        float objectDistance = map(p);
        distance += objectDistance;
        if (abs(objectDistance) < epsilon) {
            return distance;
        }
        else if(distance >= maxDistance) {
            return -1;
        }
    }

    return -1;
}

vec3 GetSurfaceNormal(vec3 p) {
    float d0 = map(p);
    const vec2 epsilon = vec2(.0001,0);
    vec3 d1 = vec3(
    map(p-epsilon.xyy),
    map(p-epsilon.yxy),
    map(p-epsilon.yyx));
    return normalize(d0 - d1);
}


vec3 render(vec2 coords) {
    vec3 col;
    vec3 direction = computeDirection(coords);
    float t = castRay(vec3(0, 0, 0), direction);
    vec3 pos = camera_pos + direction*t;
    vec3 n = GetSurfaceNormal(pos);


    if(t == -1 || t > DRAW_DIST) {
        // Skybox
        col = vec3(0.30, 0.36, 0.60) - (direction.y * .2);
    } else {
        vec3 objectSurfaceColour = vec3(0.4, 0.8, 0.1);

        // Ombrage simple
        float NoL = 0.2 + 0.8 * max(dot(n, light), 0); // 0.2 et 0.8 pour remap
        vec3 LDirectional = vec3(0.9, 0.9, 0.8) * NoL;
        vec3 LAmbient = vec3(0.03, 0.04, 0.1);
        vec3 diffuse = objectSurfaceColour * (LDirectional + LAmbient);
        col = vec3(diffuse);

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
        col = mix(col, col*.0, s);
        //col = col * (1-s) + col * 0.0 * s;
        //col = LDirectional;

    }

    col = pow(col, vec3(0.4545)); // Gamma correction
    return col;
}


void main() {
    objects[0] = obj(vec4(0.0, 0.0, -2.0, 0),vec4(4, 4, 4, 1),1,0,1);
    objects[1] = obj(vec4(0.0, -4.0, -5.0, 0),vec4(4, 4, 4, 1),1,1,1);
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
}

