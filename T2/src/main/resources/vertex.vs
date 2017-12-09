#version 130

uniform mat4 projectionMatrix;
uniform mat4 worldMatrix;

attribute vec3 position;
attribute vec3 inColour;

out vec3 exColour;

void main()
{
    gl_Position = projectionMatrix * worldMatrix * vec4(position, 1.0);
    exColour = inColour;
}