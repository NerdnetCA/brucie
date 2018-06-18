attribute vec4 a_Position;

uniform mat4 u_matViewProj;

void main() {

    gl_Position = u_matViewProj * a_Position;
}