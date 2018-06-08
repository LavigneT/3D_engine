#version 330

in vec2 pass_texCoords;
in vec3 aNormal;
in vec3 fragPos;

layout(location = 0) out vec4 outputColor;

struct Sun {
	vec3 color;
	float ambientStrengh;
	vec3 direction;
};

uniform Sun sun;
uniform vec3 camPos;
uniform sampler2D textureImage;

const float shineExtent = 1;
const float reflectivity = 1;

vec3 getSunLight(vec3 norm, Sun sun, vec3 camPos, vec3 fragPos, float shineExtent, float reflectivity) {

	vec3 lightDir = normalize(-sun.direction);
	float preDiffuse = max(dot(norm, lightDir), 0.0);
	vec3 diffuse = preDiffuse * sun.color;

	//specular
	vec3 viewDir = normalize(camPos - fragPos);
	vec3 reflectLightDir = reflect(-lightDir, norm);
	float specFactor = pow(max(dot(viewDir,reflectLightDir), 0.0), shineExtent);
	vec3 specular = reflectivity * specFactor * sun.color;

	return (diffuse + specular);
}

void main()
{
	vec3 norm = normalize(aNormal);
	vec3 ambient = 0.3 * sun.color;
	vec3 sunInfluence = getSunLight(norm, sun, camPos, fragPos, shineExtent, reflectivity);
	outputColor = texture(textureImage, pass_texCoords) * vec4(sunInfluence, 1.0);
}
