package com.shellGDX.model3D.light;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.shellGDX.shader.ShaderInstance;
import com.shellGDX.shader.ShaderManager;

public enum LightWorld3D
{
  instance;

  protected ShaderInstance activeShader = null;
  
  protected static final int maxLights = 16;
  protected Array<Light3D> lights = new Array<Light3D>(false, maxLights);
  
  protected Light3D[] renderLights = new Light3D[maxLights];
  protected int activeLights = 0;
  
  protected Color ambientColor = new Color(0.1f, 0.1f, 0.1f, 1.0f);

  public void init()
  {
    for (int i = 0; i <= maxLights; i++)
      ShaderManager.instance.compileProgram("light" + String.valueOf(i), new LightsShader(i));
    activeShader = ShaderManager.instance.getShader("light0");
  }

  public void update()
  {
    activeLights = 0;
    for (int i = 0; i < lights.size; i++)
    {
      Light3D light = lights.get(i);
      if (light.active)
      {
        renderLights[activeLights] = light;
        activeLights++;
      }
    }
    activeShader = ShaderManager.instance.getShader("light" + String.valueOf(activeLights));
  }
  
  public ShaderInstance getActiveShader()
  {
    return activeShader;
  }
  
  public Light3D getLight(int numLight)
  {
    return lights.get(numLight);
  }
  
  public Array<Integer> addLights(Array<Light3D> lights)
  {
    Array<Integer> numLights = new Array<Integer>(false, lights.size);
    for(int i = 0; i < lights.size; i++)
      numLights.add(addLight(lights.get(i)));
    return numLights;
  }
  
  public int addLight(Light3D light)
  {
    for(int i = 0; i < lights.size; i++)
    {
      if (lights.get(i) == null)
      {
        lights.set(i, light);
        return i;
      }
    }
    lights.add(light);
    return lights.indexOf(light, true);
  }
  
  public void removeLights(Array<Light3D> lights)
  {
    lights.removeAll(lights, true);
  }
  
  
  public void removeLight(int numLight)
  {
    lights.items[numLight] = null;
  }
  
  public void clear()
  {
    lights.clear();
  }
  
  public void setAmbientColor(Color color)
  {
    ambientColor = color;
  }
  
  public Color getAmbientColor()
  {
    return ambientColor;
  }
}
