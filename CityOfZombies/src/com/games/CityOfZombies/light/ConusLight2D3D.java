package com.games.CityOfZombies.light;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.shellGDX.box2dLight.ConeLight;
import com.shellGDX.box2dLight.RayHandler;
import com.shellGDX.model3D.light.ConusLight3D;
import com.shellGDX.model3D.light.Light3D;
import com.shellGDX.model3D.light.LightWorld3D;

public class ConusLight2D3D extends ConeLight implements Light2D3D
{
  ConusLight3D light3D = null;
  
  public ConusLight2D3D(RayHandler rayHandler, int rays, Color color,
                        float distance, float x, float y, float directionDegree,
                        float coneDegree)
  {
    super(rayHandler, rays, color, distance, x, y, directionDegree, coneDegree);
    light3D = new ConusLight3D(new Vector3(x, y, 120.0f),
                               new Vector3(MathUtils.cos(directionDegree * MathUtils.degRad),
                                           MathUtils.sin(directionDegree * MathUtils.degRad),
                                           0.0f),
                               distance * 0.75f,
                               96.0f - coneDegree,
                               color);
    LightWorld3D.instance.addLight(light3D);
    lights2D3D.add(this);
  }
  
  public Light3D asLight3D()
  {
    return light3D;
  }

  @Override
  public void setPosition(float x, float y)
  {
    super.setPosition(x, y);
    if (light3D != null)
      light3D.position.set(x, y, 120.0f);
  }
  
  @Override
  public void setDirection(float direction)
  {
    super.setDirection(direction);
    if (light3D != null)
      light3D.direction.set(MathUtils.cos(direction * MathUtils.degRad),
                            MathUtils.sin(direction * MathUtils.degRad),
                            0.0f);
  }

  @Override
  public void update(float deltaTime)
  {
    if (light3D == null)
      return;
    
    light3D.active = isActive();
    light3D.position.x = getX();
    light3D.position.y = getY();
    light3D.conusAngle = 96.0f - getConeDegree();
    light3D.radius = getDistance() * 0.75f;
    
    if (body != null && !body.isFixedRotation())
    {
      float angle = body.getAngle();
      light3D.direction.x = MathUtils.cos(angle);
      light3D.direction.y = MathUtils.sin(angle);
    }
  }

  @Override
  public ConusLight2D3D asConusLight2D3D()
  {
    return this;
  }

  @Override
  public PointLight2D3D asPointLight2D3D()
  {
    return null;
  }
}
