#version 120

uniform vec3 c1;
uniform vec3 c2;
uniform float time;
uniform float speed;
uniform float frequency;
uniform vec2 resolution;

varying vec2 v_uv;

void main() {
    vec2 screenspaceUV = gl_FragCoord.xy/resolution;
    float mixval = ((sin((screenspaceUV.y - screenspaceUV.x + time * speed) * frequency)) + 1.f) / 2.f;
    gl_FragColor = vec4(mix(c1, c2, mixval),1.f);
}