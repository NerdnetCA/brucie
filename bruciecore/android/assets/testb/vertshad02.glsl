
attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord;

uniform mat4 u_worldTrans;
uniform mat4 u_projViewTrans;

varying vec2 v_texCoords;

void main() {
    v_texCoords = a_texCoord;
    gl_Position = u_projViewTrans * u_worldTrans * vec4(a_position, 1.0);
}

