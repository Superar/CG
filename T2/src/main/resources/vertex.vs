#version 130

uniform mat4 projectionMatrix;

attribute vec3 position;
attribute vec3 inColour;

out vec3 exColour;

void main()
{
    gl_Position = projectionMatrix * vec4(position, 1.0);
    exColour = inColour;
}