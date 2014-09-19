package com.shellGDX.model3D;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.SnapshotArray;
import com.shellGDX.GameLog;
import com.shellGDX.box2dLight.LightWorld2D;
import com.shellGDX.model3D.light.Light3D;
import com.shellGDX.model3D.light.LightWorld3D;

public abstract class LightObject3D extends ModelObject3D
{
  protected SnapshotArray<Light3D> lights = new SnapshotArray<Light3D>(false, 1, Light3D.class);
  
  public LightObject3D()
  {
    super();
  }
  
  protected Vector3 newPosition = new Vector3();
  protected float   newAngle = 0.0f;

  @Override
  public boolean update(float delta)
  {
    if (!super.update(delta))
      return false;

    if (lights.size > 0)
    {      
      Light3D[] lights3D = lights.begin();
      for (int i = 0, n = lights.size; i < n; i++)
      {
        Light3D light = lights3D[i];
        
        light.active = isVisible();
        
        /*if (light.getBody() == null)
        {
          newAngle = getRotation();
          newPosition.set(getX(), getY(), getZ());
          Actor parent = getParent();
          while(parent != null)
          {
            newPosition.x += parent.getX();
            newPosition.y += parent.getY();
            newPosition.y += parent.getY();
            newAngle += parent.getRotation();
            parent = parent.getParent();
          }

          light.position.set(newPosition.x, newPosition.y);

          if (light instanceof ConusLight3D)
            ((ConusLight3D)light).setDirection(newAngle);
        }*/
      }
      lights.end();
    }
    
    return true;
  }
  
  protected abstract boolean initLightsObject(LightWorld3D lightsWorld, final SnapshotArray<Light3D> lights);
  

  @Override
  protected void setScene(Scene3D scene3d)
  {
    if (LightWorld2D.instance != null)
    {
      if (lights.size > 0)
      {
        LightWorld3D.instance.removeLights(lights);
        lights.clear();
      }
      
      if (scene == null)
        return;

      if (initLightsObject(LightWorld3D.instance, lights))
      {
        LightWorld3D.instance.addLights(lights);
        super.setScene(scene);
        return;
      }
    }

    GameLog.instance.writeError("Failed to initialize a light3D object!");
  }

  @Override
  public boolean remove()
  {
    if (LightWorld3D.instance != null && lights != null)
    {
      LightWorld3D.instance.removeLights(lights);
      lights.clear();
    }
    return super.remove();
  }
}
