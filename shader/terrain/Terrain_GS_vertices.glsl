#version 430

layout(triangles) in;
layout(line_strip, max_vertices = 3) out;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main() {

	for (int i = 0; i < gl_in.length(); ++i)
		{
			vec4 position = gl_in[i].gl_Position;
			gl_Position = projectionMatrix *  viewMatrix *  position;
			EmitVertex();
		}

		vec4 position = gl_in[0].gl_Position;
		gl_Position = projectionMatrix * viewMatrix * position;
	    EmitVertex();

		EndPrimitive();
}