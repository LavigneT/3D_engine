#version 330

in vec2 mapCoord_FS;
in vec3 worldposition;

layout(location = 0) out vec4 outputColor;

const float shineExtent = 0;
const float reflectivity = 0;

struct Sun {
	vec3 color;
	float ambientStrengh;
	vec3 direction;
};

uniform sampler2D mapTexture;
uniform sampler2D normalMap;
uniform vec3 cameraPosition;
uniform Sun sun;



vec3 getSunLight(vec3 norm, Sun sun, vec3 cameraPosition, vec3 worldposition, float shineExtent, float reflectivity) {

	vec3 lightDir = normalize(-sun.direction);
	float preDiffuse = max(dot(norm, lightDir), 0.0);
	vec3 diffuse = preDiffuse * sun.color;

	//specular
	vec3 viewDir = normalize(cameraPosition - worldposition);
	vec3 reflectLightDir = reflect(-lightDir, norm);
	float specFactor = pow(max(dot(viewDir,reflectLightDir), 0.0), shineExtent);
	vec3 specular = reflectivity * specFactor * sun.color;

	return (diffuse + specular);
}

void main()
{
	vec3 norm = texture(normalMap, mapCoord_FS).rgb;
	norm = 2 * (norm) + vec3(-1, -1, -1);
	vec3 output = getSunLight(norm, sun, cameraPosition, worldposition, shineExtent, reflectivity);
	outputColor = texture(mapTexture, mapCoord_FS) * vec4(output, 1.0);
}
