#version 330

in vec2 mapCoord_FS;

layout(location = 0) out vec4 outputColor;

uniform sampler2D mapTexture;


void main()
{
	outputColor = texture(mapTexture, mapCoord_FS);
}
