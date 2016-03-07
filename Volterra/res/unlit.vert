#version 130

uniform mat4 projMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

in vec3 position;
in vec2 texCoord;

out vec3 pass_position;
out vec2 pass_texCoord;

void main(void) {
	pass_position = position;
	pass_texCoord = texCoord;
	
	gl_Position = projMatrix * viewMatrix * modelMatrix * vec4(position, 1);
}
