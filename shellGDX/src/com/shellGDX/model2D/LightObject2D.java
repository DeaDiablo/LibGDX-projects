package com.shellGDX.model2D;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.SnapshotArray;
import com.shellGDX.GameLog;
import com.shellGDX.box2dLight.ConeLight;
import com.shellGDX.box2dLight.Light2D;
import com.shellGDX.box2dLight.LightWorld2D;
import com.shellGDX.box2dLight.RayHandler;

public abstract class LightObject2D extends Actor
{
  protected SnapshotArray<Light2D> lights = new SnapshotArray<Light2D>(false, 1, Light2D.class);
  
  public LightObject2D()
  {
    this(0, 0, 0);
  }

  public LightObject2D(float x, float y)
  {
    this(x, y, 0);
  }

  public LightObject2D(float x, float y, float angle)
  {
    super();
    setPosition(x, y);
    setRotation(angle);
  }
  
  public final SnapshotArray<Light2D> getLights()
  {
    return lights;
  }
  
  protected Vector2 newPosition = new Vector2();
  protected float   newAngle = 0.0f;

  @Override
  public void act(float delta)
  {
    super.act(delta);

    if (lights.size > 0)
    {      
      Light2D[] lights2D = lights.begin();
      for (int i = 0, n = lights.size; i < n; i++)
      {
        Light2D light = lights2D[i];
        
        light.setActive(isVisible());
        
        if (light.getBody() == null)
        {
          newAngle = getRotation();
          newPosition.set(getX(), getY());
          Actor parent = getParent();
          while(parent != null)
          {
            newPosition.x += parent.getX();
            newPosition.y += parent.getY();
            newAngle += parent.getRotation();
            parent = parent.getParent();
          }

          light.setPosition(newPosition.x, newPosition.y);

          if (light instanceof ConeLight)
            ((ConeLight)light).setDirection(newAngle);
        }
      }
      lights.end();
     
    }
  }
  
  protected abstract boolean initLightsObject(RayHandler lightsWorld, final SnapshotArray<Light2D> lights);
  
  @Override
  protected void setStage(Stage stage)
  {
    if (LightWorld2D.instance != null)
    {
      if (lights.size > 0)
      {
        LightWorld2D.instance.removeLights(lights);
        lights.clear();
      }
      
      if (stage == null)
        return;

      if (initLightsObject(LightWorld2D.instance, lights))
      {
        LightWorld2D.instance.addLights(lights);
        super.setStage(stage);
        return;
      }
    }

    GameLog.instance.writeError("Failed to initialize a light2D object!");
  }

  @Override
  public boolean remove()
  {
    if (LightWorld2D.instance != null && lights != null)
    {
      LightWorld2D.instance.removeLights(lights);
      lights.clear();
    }
    return super.remove();
  }
}
