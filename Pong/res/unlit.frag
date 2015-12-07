#version 130

uniform sampler2D sprite;
uniform vec3 color;

in vec3 pass_position;
in vec2 pass_texCoord;

out vec4 out_Color;

void main(void) {
	vec4 tex = texture(sprite, pass_texCoord);
	out_Color = vec4(tex.rgb * color, tex.a);
}
