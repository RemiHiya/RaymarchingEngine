struct obj {
    vec4 v1;
    vec4 v2;
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