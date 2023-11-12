#define MIN_DIST 1
#define SHADOW_FALLOFF .02
#define DRAW_DIST 500


float rand(vec2 coord) {
    return fract(sin(dot(coord.xy, vec2(12.9898,78.233))) * 43758.5453);
} // Fonction pseudo random


in vec2 TexCoord;
uniform vec2 u_screenSize;
uniform float u_resolution;
uniform vec3 camera_pos = vec3(0, 0, 0);
uniform vec3 camera_rot = vec3(0, 0, 0);
uniform float w = 0;
uniform vec3 light = normalize(vec3(0, 2, 1));
out vec4 outputColor;
out vec4 test;

float FOV = 110;

vec3 computeDirection(vec2 coords, vec3 rotation) {
    float r = u_resolution;
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
    const float h = 0.00001; // replace by an appropriate value
    const vec2 k = vec2(1,-1);
    vec3 pos = p.xyz;
    return normalize( k.xyy*map( vec4(pos + k.xyy*h, p.w) ).d +
        k.yyx*map( vec4(pos + k.yyx*h, p.w) ).d +
        k.yxy*map( vec4(pos + k.yxy*h, p.w) ).d +
        k.xxx*map( vec4(pos + k.xxx*h, p.w) ).d );
}

float calcAO( in vec3 pos, in vec3 nor )
{
    float occ = 0.0;
    float sca = 1.0;
    for( int i=0; i<5; i++ )
    {
        float h = 0.01 + 0.12*float(i)/4.0;
        float d = map( vec4(pos + h*nor, w) ).d;
        occ += (h-d)*sca;
        sca *= 0.95;
        if( occ>0.35 ) break;
    }
    return clamp( 1.0 - 3.0*occ, 0.0, 1.0 ) * (0.5+0.5*nor.y);
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
        col = vec3(0.30, 0.36, 0.60) - (direction.z * -0.2);
    } else {

        // Ombrage simple
        float NoL = 0.2 + 0.8 * max(dot(n, light), 0); // 0.2 et 0.8 pour remap
        vec3 LDirectional = vec3(0.9, 0.9, 0.8) * NoL;
        vec3 LAmbient = vec3(0.03, 0.04, 0.1);
        vec3 diffuse = objectSurfaceColour * (LDirectional + LAmbient);

        diffuse *= calcAO(pos, n);
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