

uniform sampler2D u_texture;

varying vec2 v_texCoords;
varying vec3 v_normal;

void main() {
    vec4 texcolor = texture2D(u_texture, v_texCoords);
    //vec3 LDir = normalize(vec3(-1,-1,-1));

    vec3 up = vec3(0,1,0);

    float tuning = 1.0;
    if( dot(up,v_normal) < 0.2) {
        tuning = 0.6;
    }


    gl_FragColor = texcolor * tuning;
    //gl_FragColor = vec4(v_texCoords, 0.0, 1.0);
}