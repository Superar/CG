#version 130

flat in vec3 exColour;

void main()
{
    gl_FragColor = vec4(exColour, 1.0);
}