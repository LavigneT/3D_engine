#version 330

in vec2 pass_textureCoord;

layout(location = 0) out vec4 outputColor;

uniform sampler2D writing;

void main()
{
	outputColor = texture(writing, pass_textureCoord);
}
