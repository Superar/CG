#version 130

attribute vec3 position;
attribute vec3 inColour;

out vec3 exColour;

void main()
{
    gl_Position = vec4(position, 1.0);
    exColour = inColour;
}