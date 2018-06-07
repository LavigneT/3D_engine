#version 430

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec2 texCoord;

uniform mat4 projectionMatrix;
uniform mat4 worldMatrix;
uniform mat4 viewMatrix;

out vec2 pass_texCoords;
out vec3 aNormal;
out vec3 fragPos;

void main() {

	vec4 worldPosition =  worldMatrix * vec4(position, 1.0);
	vec4 relativeToCamera = viewMatrix * worldPosition;
	gl_Position =  projectionMatrix * relativeToCamera ;

	//for fragmentShader use
	pass_texCoords = texCoord;
	aNormal = (worldMatrix * vec4(normal,0.0)).xyz;
	fragPos = vec3(worldPosition);
}
