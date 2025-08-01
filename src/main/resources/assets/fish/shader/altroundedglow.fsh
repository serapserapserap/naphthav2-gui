#version 120

varying vec2 v_uv;

uniform vec4 fillColor;
uniform vec4 glowColor;
uniform float radius;
uniform float glowSize;
uniform vec2 resolution;

void main() {
    vec2 pixelPos = v_uv * resolution;
    vec2 inset = glowSize * (resolution / min(resolution.x, resolution.y));
    vec2 offset = pixelPos - clamp(pixelPos, vec2(inset.x + radius, inset.y + radius), resolution - vec2(inset.x + radius, inset.y + radius));
    float dist = length(offset);
    float rectAlpha = smoothstep(radius, radius - 1.0, dist);
    float glowStart = radius;
    float glowEnd = radius + glowSize / 2;
    float glowAlpha = max(0.f, smoothstep(glowEnd, glowStart, dist) - .025f);
    gl_FragColor = vec4(mix(glowColor.rgb, fillColor.rgb, rectAlpha), max(glowColor.a * glowAlpha, fillColor.a * rectAlpha));

}