#version 130

uniform bool hasTexture;
uniform sampler2D sprite;
uniform vec3 color;

in vec3 pass_position;
in vec2 pass_texCoord;

out vec4 out_Color;

void main(void) {
	if (hasTexture) {
		vec4 tex = texture(sprite, pass_texCoord);
		out_Color = vec4(tex.rgb * color, tex.a);
		return;
	}
	out_Color = vec4(color, 1);
}
