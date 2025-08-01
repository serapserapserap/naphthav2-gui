#version 330 core
out vec4 fragColor;
in vec2 jj;
uniform vec2 res;
uniform float iTime;
uniform vec3 c1;
uniform vec3 c2;
vec2 random2( vec2 p ) {
    return fract(sin(vec2(dot(p,vec2(127.1,311.7)),dot(p,vec2(269.5,183.3))))*43758.5453);
}

void main() {
    vec3 colour = vec3(0,0,0);
    vec2 uv = gl_FragCoord.xy/res;
    uv *= 9.;

    vec2 integer = floor(uv);
    vec2 floatingPoint = fract(uv);
    float m_dist = 2.;
    for (int y= -1; y <= 1; y++) {
        for (int x= -1; x <= 1; x++) {
            vec2 neighbor = vec2(float(x),float(y));
            m_dist = min(m_dist,length(neighbor + (.75 + 0.5*sin(iTime / 300 + 3.2831*(random2(integer + neighbor))) - floatingPoint)));
        }
    }
    //c1 - 0.0,0.0,1.000
    //c2 - 0.381,0.679,1.000
    colour = mix(vec3(c2),vec3(c1),vec3(smoothstep(0.,1.,m_dist)));
    fragColor = vec4(colour,1.f);
}