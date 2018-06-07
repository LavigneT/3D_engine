#version 430

layout(triangles) in;
layout(triangle_strip, max_vertices = 3) out;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

in vec2 mapCoord_GS[];
out vec2 mapCoord_FS;

void main() {
	
	for (int i = 0; i < gl_in.length(); ++i)
		{
			vec4 position = gl_in[i].gl_Position;
			gl_Position = projectionMatrix * viewMatrix * position;
			mapCoord_FS = mapCoord_GS[i];
			EmitVertex();
		}

		EndPrimitive();
}
