#version 330

in vec2 v_texCoord;
in vec2 v_sourceUv;
out vec4 fragColor;

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

layout(std140) uniform HandMaskParameters {
    vec4 HandColor;
};

void main() {
    vec3 before = texture(Sampler0, v_sourceUv).rgb;
    vec3 after = texture(Sampler1, v_sourceUv).rgb;
    vec3 d = abs(after - before);
    float changed = max(max(d.r, d.g), d.b);
    fragColor = changed > HandColor.w ? vec4(HandColor.rgb, 1.0) : vec4(0.0);
}
