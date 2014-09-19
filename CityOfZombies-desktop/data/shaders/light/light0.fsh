#ifdef GL_ES 
  precision mediump float;
#endif

uniform sampler2D u_texture;
uniform vec3 u_ambient;

varying vec2 v_texCoord0;

void main()
{
  gl_FragColor = texture2D(u_texture, v_texCoord0);
}