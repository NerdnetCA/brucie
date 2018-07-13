
attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord;

uniform mat4 u_worldTrans;
uniform mat4 u_projViewTrans;

varying vec2 v_texCoords;
varying vec3 v_normal;

void main() {
    v_texCoords = a_texCoord;
    v_normal = a_normal;
    gl_Position = u_projViewTrans * u_worldTrans * vec4(a_position, 1.0);
}

