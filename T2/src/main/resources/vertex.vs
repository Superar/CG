#version 130



attribute vec3 position;
attribute vec3 inColour;
attribute vec3 vertexNormal;

out vec3 exColour;
out vec3 mvVertexNormal;
out vec3 mvVertexPos;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 worldMatrix;

void main()
{
    vec4 mvPos = modelViewMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * mvPos;

    //gl_Position = projectionMatrix * viewMatrix * worldMatrix * vec4(position, 1.0);

    exColour = inColour;

    mvVertexNormal = normalize(modelViewMatrix * vec4(vertexNormal, 0.0)).xyz;
    mvVertexPos = mvPos.xyz;
}