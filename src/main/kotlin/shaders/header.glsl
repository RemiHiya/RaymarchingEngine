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
    mat4 rotationMatrix = mat4(1.0);

    // Rotation autour de l'axe X
    float cosX = cos(rot.x);
    float sinX = sin(rot.x);
    mat4 rotationX = mat4(
    1.0, 0.0, 0.0, 0.0,
    0.0, cosX, -sinX, 0.0,
    0.0, sinX, cosX, 0.0,
    0.0, 0.0, 0.0, 1.0
    );

    // Rotation autour de l'axe Y
    float cosY = cos(rot.y);
    float sinY = sin(rot.y);
    mat4 rotationY = mat4(
    cosY, 0.0, -sinY, 0.0,
    0.0, 1.0, 0.0, 0.0,
    sinY, 0.0, cosY, 0.0,
    0.0, 0.0, 0.0, 1.0
    );

    // Rotation autour de l'axe Z
    float cosZ = cos(rot.z);
    float sinZ = sin(rot.z);
    mat4 rotationZ = mat4(
    cosZ, sinZ, 0.0, 0.0,
    -sinZ, cosZ, 0.0, 0.0,
    0.0, 0.0, 1.0, 0.0,
    0.0, 0.0, 0.0, 1.0
    );

    // Rotation autour de l'axe W
    float cosW = cos(rot.w);
    float sinW = sin(rot.w);
    mat4 rotationW = mat4(
    cosW, 0.0, 0.0, -sinW,
    0.0, 1.0, 0.0, 0.0,
    0.0, 0.0, 1.0, 0.0,
    sinW, 0.0, 0.0, cosW
    );

    mat4 rotationW2 = mat4(
    1.0, 0.0, 0.0, 0.0,
    0.0, 1.0, 0.0, 0.0,
    0.0, 0.0, cosW, -sinW,
    0.0, 0.0, sinW, cosW
    );

    mat4 rotationW3 = mat4(
    1.0, 0.0, 0.0, 0.0,
    0.0, cosW, 0.0, -sinW,
    0.0, 0.0, 1.0, 0.0,
    0.0, sinW, 0.0, cosW
    );


    // Combinez les rotations
    rotationMatrix = rotationX * rotationY * rotationZ * rotationW * rotationW2 * rotationW3;

    // Appliquez la rotation à la position
    return rotationMatrix * pos;
}





/*
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

    //return pos;
    return xy*yz*xz*xw*yw*zw * pos;
}*/