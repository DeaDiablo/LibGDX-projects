package com.shellGDX.model3D.light;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

public class PointLight3D extends Light3D
{
  public PointLight3D(Vector3 position, float radius, Color color)
  {
    super(position, radius, color);
  }

  @Override
  public PointLight3D asSpotLight()
  {
    return this;
  }
}
