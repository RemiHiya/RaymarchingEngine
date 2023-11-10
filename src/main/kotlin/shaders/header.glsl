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

uniform float u_time;

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
    t=sin(u_time);
    mat4 zw = mat4(cos(t), sin(t), 0, 0,
    -sin(t), cos(t), 0, 0,
    0, 0, 1, 0,
    0, 0, 0, 1);

    return pos;
    return xy*yz*xz*xw*yw*zw * pos;
}