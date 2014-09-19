package com.shellGDX.model3D.light;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.shellGDX.model3D.ModelObject3D;

public class Light3D
{
  public Light3D(Vector3 position, float radius, Color color)
  {
    this.position = position;
    this.radius = radius;
    this.color = color;
  }
  
  public PointLight3D asSpotLight()
  {
    return null;
  }
  
  public ConusLight3D asConusLight()
  {
    return null;
  }
  
  protected int  num = -1;
  public boolean active = true;
  public Vector3 position;
  public float radius;
  public Color color;
  

  
  public void attachModel(ModelObject3D model, float offsetX, float offsetY)
  {
    
  }
}
