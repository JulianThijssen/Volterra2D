#version 130

uniform sampler2D sprite;

in vec3 pass_position;
in vec2 pass_texCoord;

out vec4 out_Color;

void main(void) {
	vec4 color = texture(sprite, pass_texCoord);
	out_Color = color;
}
