

uniform sampler2D u_texture;

varying vec2 v_texCoords;

void main() {
    vec4 texcolor = texture2D(u_texture, v_texCoords);
    //vec3 LDir = normalize(vec3(-1,-1,-1));



    gl_FragColor = vec4(texture2D(u_texture, v_texCoords).rgb,1.0);
    //gl_FragColor = vec4(v_texCoords, 0.0, 1.0);
}