package com.shellGDX.shader;

import java.util.HashMap;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public enum ShaderManager
{
  instance;

  protected HashMap<String, ShaderInstance> shaders = new HashMap<String, ShaderInstance>();

  public void clear()
  {
    for (ShaderInstance shader : shaders.values())
      shader.dispose();
    shaders.clear();
  }

  public void compileProgram(String nameShader, ShaderInstance shader)
  {
    if (shaders.containsKey(nameShader))
      return;

    shader.program = new ShaderProgram(shader.vertexShader, shader.fragmentShader);
    if (!shader.program.isCompiled())
      throw new GdxRuntimeException(shader.program.getLog());
    shader.vertexShader = null;
    shader.fragmentShader = null;
    shader.init();
    shaders.put(nameShader, shader);
  }
  
  protected void unloadProgram(String nameShader)
  {
    ShaderInstance shader = shaders.get(nameShader);
    if (shader != null)
      shader.dispose();
  }

  public ShaderInstance getShader(String nameShader)
  {
    return shaders.get(nameShader);
  }
}
