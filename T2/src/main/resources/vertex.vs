#version 130

uniform mat4 projectionMatrix;
uniform mat4 worldMatrix;
uniform mat4 viewMatrix;

attribute vec3 position;
attribute vec3 inColour;

flat out vec3 exColour;

void main()
{
    gl_Position = projectionMatrix * viewMatrix * worldMatrix * vec4(position, 1.0);
    exColour = inColour;
}