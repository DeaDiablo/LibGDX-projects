package com.shellGDX.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public abstract class ShaderInstance implements Shader
{
  private static final String VERTEX_SHADER_EXTENSION = ".vsh";
  private static final String FRAGMENT_SHADER_EXTENSION = ".fsh";
  
  protected ShaderProgram program;
  protected String vertexShader = null;
  protected String fragmentShader = null;
  
  public ShaderInstance()
  {
  }
  
  public ShaderInstance(String shaderFile)
  {
    this.vertexShader = Gdx.files.internal(shaderFile + VERTEX_SHADER_EXTENSION).readString();
    this.fragmentShader = Gdx.files.internal(shaderFile + FRAGMENT_SHADER_EXTENSION).readString();
  }
  
  public ShaderInstance(String vertexShader, String fragmentShader)
  {
    this.vertexShader = vertexShader;
    this.fragmentShader = fragmentShader;
  }
  
  public ShaderInstance(FileHandle vertexShader, FileHandle fragmentShader)
  {
    this.vertexShader = vertexShader.readString();
    this.vertexShader = fragmentShader.readString();
  }
  
  public boolean isCompiled()
  {
    return program.isCompiled();
  }
}
