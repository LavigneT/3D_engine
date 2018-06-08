#version 430

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoord;

uniform mat4 transform;

out vec2 pass_textureCoord;

void main() {

	pass_textureCoord = textureCoord;

	vec4 worldPosition = transform * vec4(position, 1);

	gl_Position = worldPosition;
}
