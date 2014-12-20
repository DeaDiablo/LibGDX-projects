package com.games.CityOfZombies.light;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.shellGDX.box2dLight.PointLight;
import com.shellGDX.box2dLight.RayHandler;
import com.shellGDX.model3D.light.Light3D;
import com.shellGDX.model3D.light.LightWorld3D;
import com.shellGDX.model3D.light.PointLight3D;

public class PointLight2D3D extends PointLight implements Light2D3D
{
  PointLight3D light3D = null;
  
  public PointLight2D3D(RayHandler rayHandler, int rays)
  {
    super(rayHandler, rays);
    light3D = new PointLight3D(new Vector3(getX(), getY(), 200.0f), getDistance() * 0.5f, getColor());
    LightWorld3D.instance.addLight(light3D);
    lights2D3D.add(this);
  }

  public PointLight2D3D(RayHandler rayHandler, int rays, Color color, float distance, float x, float y)
  {
    super(rayHandler, rays, color, distance, x, y);
    light3D = new PointLight3D(new Vector3(x, y, 200.0f), distance * 0.5f, color);
    LightWorld3D.instance.addLight(light3D);
    lights2D3D.add(this);
  }
  
  public Light3D asLight3D()
  {
    return light3D;
  }

  @Override
  public void update(float deltaTime)
  {
    light3D.active = isActive();
    light3D.position.x = getX();
    light3D.position.y = getY();
    light3D.radius = getDistance() * 0.5f;
  }

  @Override
  public PointLight2D3D asPointLight2D3D()
  {
    return this;
  }

  @Override
  public ConusLight2D3D asConusLight2D3D()
  {
    return null;
  }
}
