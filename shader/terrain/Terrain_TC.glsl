#version 430

layout(vertices = 16) out;

const int AB = 2;
const int BC = 3;
const int CD = 0;
const int DA = 1;

in vec2 mapCoord_TC[];
out vec2 mapCoord_TE[];

void main()
{

	if(gl_InvocationID == 0)
	{
			gl_TessLevelOuter[AB] = 4;
			gl_TessLevelOuter[BC] = 4;
			gl_TessLevelOuter[CD] = 4;
			gl_TessLevelOuter[DA] = 4;
	
			gl_TessLevelInner[0] = 4;
			gl_TessLevelInner[1] = 4;
	}
	
	mapCoord_TE[gl_InvocationID] = mapCoord_TC[gl_InvocationID];
	gl_out[gl_InvocationID].gl_Position = gl_in[gl_InvocationID].gl_Position;
}
