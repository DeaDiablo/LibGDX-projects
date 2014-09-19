package com.shellGDX.model3D.light;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.math.Matrix3;
import com.shellGDX.shader.ShaderInstance;

public class LightsShader extends ShaderInstance
{
  protected RenderContext context;
  protected Camera        camera;
  
  protected int numLights;
  protected float[] locationLights = null;
  protected float[] colorLights    = null;
  protected float[] directionLights   = null;
  protected float[] anglesLights   = null;
  
  protected float[] ambientColor = null;
  
  //uniforms
  protected int u_viewProjectionMatrix;
  protected int u_worldMatrix;
  protected int u_normalMatrix;
  protected int u_texture;
  
  protected int u_locations;
  protected int u_colors;
  protected int u_direction;
  protected int u_angles;
  protected int u_ambient;
  
  //light3d world
  protected Light3D[] renderLights;
  
  public LightsShader(int numLights)
  {
    super();
    this.numLights = numLights;

    locationLights = new float[4 * numLights];
    colorLights = new float[3 * numLights];
    directionLights = new float[3 * numLights];
    anglesLights = new float[2 * numLights];
    
    ambientColor = new float[3];
    
    if (numLights > 0)
    {
      vertexShader = "attribute vec3 a_position;\n" +
                     "attribute vec3 a_normal;\n" +
                     "attribute vec2 a_texCoord0;\n\n" +
                     "uniform mat4 u_worldMatrix;\n" +
                     "uniform mat4 u_viewProjectionMatrix;\n" +
                     "uniform mat3 u_normalMatrix;\n\n" +
                     "varying vec3 v_position;\n" +
                     "varying vec3 v_normal;\n" +
                     "varying vec2 v_texCoord0;\n\n" +
                     "void main()\n" +
                     "{\n" +
                     "  v_texCoord0 = a_texCoord0;\n" +
                     "  gl_Position = u_viewProjectionMatrix * u_worldMatrix * vec4(a_position, 1.0);\n" +
                     "  v_position = (u_worldMatrix * vec4(a_position, 1.0)).xyz;\n" +
                     "  v_normal = normalize(u_normalMatrix * a_normal);\n" +
                     "}\n";
      
      fragmentShader = "#ifdef GL_ES;\n" +
                       "  precision mediump float;\n" +
                       "#endif\n\n" +
                       "uniform sampler2D u_texture;\n" +
                       "uniform vec3 u_ambient;\n\n" +
                       "varying vec3 v_position;\n" +
                       "varying vec3 v_normal;\n" +
                       "varying vec2 v_texCoord0;\n\n" +
                       "uniform vec4 u_locations["+numLights+"];\n" +
                       "uniform vec3 u_colors["+numLights+"];\n" +
                       "uniform vec3 u_direction["+numLights+"];\n" +
                       "uniform vec2 u_angles["+numLights+"];\n\n" +
                       "void main()\n" +
                       "{\n" +
                       "  vec3 diffuse = vec3(0.0, 0.0, 0.0);\n" +
                       "  vec3 direction;\n" +
                       "  vec3 distance;\n" +
                       "  float angle;\n" +
                       "  float conusAngle;\n\n";
     for (int i = 0; i < numLights; i++)
     {
       fragmentShader += "  direction = u_locations[" + i + "].xyz - v_position;\n" +
                         "  distance = max(0.0, (u_locations[" + i + "].w - length(direction))) / u_locations[" + i + "].w;\n" +
                         "  direction = normalize(direction);\n" +
                         "  angle = max(0.0, dot(v_normal, direction));\n" +
                         "  conusAngle = (dot(-direction, u_direction[" + i + "]) - u_angles[" + i + "].x) / (1.0 - u_angles[" + i + "].y);\n" +
                         "  conusAngle = max(0.0, conusAngle);\n" +
                         "  diffuse += u_colors[" + i + "] * angle * conusAngle * distance;\n\n";
     }

     fragmentShader += "  gl_FragColor = texture2D(u_texture, v_texCoord0) * vec4(u_ambient + diffuse, 1.0);\n" +
                       "}";
    }
    else
    {
      vertexShader = "attribute vec3 a_position;\n" +
                     "attribute vec3 a_normal;\n" +
                     "attribute vec2 a_texCoord0;\n\n" +
                     "uniform mat4 u_worldMatrix;\n" +
                     "uniform mat4 u_viewProjectionMatrix;\n\n" +
                     "varying vec2 v_texCoord0;\n\n" +
                     "void main()\n" +
                     "{\n" +
                     "  v_texCoord0 = a_texCoord0;\n" +
                     "  gl_Position = u_viewProjectionMatrix * u_worldMatrix * vec4(a_position, 1.0);\n" +
                     "}\n";

      fragmentShader = "#ifdef GL_ES;\n" +
                       "  precision mediump float;\n" +
                       "#endif\n\n" +
                       "uniform sampler2D u_texture;\n" +
                       "uniform vec3 u_ambient;\n\n" +
                       "varying vec2 v_texCoord0;\n\n" +
                       "void main()\n" +
                       "{\n" +
                       "  gl_FragColor = texture2D(u_texture, v_texCoord0) * vec4(u_ambient, 1.0f);\n" +
                       "}";
    }
  }

  @Override
  public void init()
  {
    renderLights = LightWorld3D.instance.renderLights;
    
    u_viewProjectionMatrix = program.getUniformLocation("u_viewProjectionMatrix");
    u_worldMatrix = program.getUniformLocation("u_worldMatrix");
    u_normalMatrix = program.getUniformLocation("u_normalMatrix");
    u_texture = program.getUniformLocation("u_texture");
    if (numLights > 0)
    {
      u_locations = program.getUniformLocation("u_locations[0]");
      u_colors = program.getUniformLocation("u_colors[0]");
      u_direction = program.getUniformLocation("u_direction[0]");
      u_angles = program.getUniformLocation("u_angles[0]");
    }
    u_ambient = program.getUniformLocation("u_ambient");
  }

  @Override
  public void dispose()
  {
    program.dispose();
  }

  @Override
  public void begin(Camera camera, RenderContext context)
  {
    this.context = context;
    this.camera = camera;
    Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
    program.begin();
    program.setUniformMatrix(u_viewProjectionMatrix, camera.combined);
    
    Color ambient = LightWorld3D.instance.getAmbientColor();
    ambientColor[0] = ambient.r;
    ambientColor[1] = ambient.g;
    ambientColor[2] = ambient.b;
    
    program.setUniform3fv(u_ambient, ambientColor, 0, 3);
  }
  
  protected Matrix3 normalMatrix = new Matrix3();
  
  @Override
  public void render(Renderable renderable)
  {
    TextureAttribute attr = (TextureAttribute)renderable.material.get(TextureAttribute.Diffuse);
    program.setUniformi(u_texture, context.textureBinder.bind(attr.textureDescription));
    program.setUniformMatrix(u_worldMatrix, renderable.worldTransform);
    
    normalMatrix.set(renderable.worldTransform).inv().transpose();
    program.setUniformMatrix(u_normalMatrix, normalMatrix);
    
    if (numLights > 0)
    {
      for(int i = 0; i < numLights; i++)
      {
        locationLights[i * 4 + 0] = renderLights[i].position.x;
        locationLights[i * 4 + 1] = renderLights[i].position.y;
        locationLights[i * 4 + 2] = renderLights[i].position.z;
        locationLights[i * 4 + 3] = renderLights[i].radius;
        
        colorLights[i * 3 + 0] = renderLights[i].color.r;
        colorLights[i * 3 + 1] = renderLights[i].color.g;
        colorLights[i * 3 + 2] = renderLights[i].color.b;

        if (renderLights[i].asConusLight() != null)
        {
          ConusLight3D conusLight3D = renderLights[i].asConusLight();
          directionLights[i * 3 + 0] = conusLight3D.direction.x;
          directionLights[i * 3 + 1] = conusLight3D.direction.y;
          directionLights[i * 3 + 2] = conusLight3D.direction.z;

          anglesLights[i * 2 + 0] = conusLight3D.conusAngle / 90.0f;
          anglesLights[i * 2 + 1] = conusLight3D.conusAngle / 90.0f;
        }
        else
        {
          directionLights[i * 3 + 0] = 0.0f;
          directionLights[i * 3 + 1] = 0.0f;
          directionLights[i * 3 + 2] = 0.0f;

          anglesLights[i * 2 + 0] = -1.0f;
          anglesLights[i * 2 + 1] = 0.0f;
        }
      }

      program.setUniform4fv(u_locations, locationLights, 0, 4 * numLights);
      program.setUniform3fv(u_colors, colorLights, 0, 3 * numLights);
      program.setUniform3fv(u_direction, directionLights, 0, 3 * numLights);
      program.setUniform2fv(u_angles, anglesLights, 0, 2 * numLights);
    }
    
    renderable.mesh.render(program,
                           renderable.primitiveType,
                           renderable.meshPartOffset,
                           renderable.meshPartSize);
  }
  
  @Override
  public void end()
  {
    program.end();
  }
  
  @Override
  public boolean canRender(Renderable instance) {
    return true;
  }

  @Override
  public int compareTo(Shader other)
  {
    return 0;
  }
}
