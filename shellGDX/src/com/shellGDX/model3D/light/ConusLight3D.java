package com.shellGDX.model3D.light;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

public class ConusLight3D extends Light3D
{
  public ConusLight3D(Vector3 position, Vector3 direction, float radius, float conusAngle, Color color)
  {
    super(position, radius, color);
    this.direction = direction.nor();
    this.conusAngle = conusAngle;
  }

  public Vector3 direction;
  public float conusAngle;
  
  @Override
  public ConusLight3D asConusLight()
  {
    return this;
  }
}
