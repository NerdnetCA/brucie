attribute vec3 a_position;
attribute vec4 a_color;

uniform mat4 u_worldTrans;
uniform mat4 u_projViewTrans;

varying vec4 v_color;

void main() {
 v_color = a_color;
 gl_Position = u_projViewTrans * u_worldTrans * vec4(a_position, 1.0);
 vec3 ndc = gl_Position.xyz / gl_Position.w ; // perspective divide.

 float zDist = 1.0 - ndc.z; // 1 is close to camera, 0 is far
 gl_PointSize = 500.0 * zDist; // between 0 and 50 now.
}