package com.shellGDX.model2D;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.SnapshotArray;
import com.shellGDX.box2dLight.Light2D;
import com.shellGDX.box2dLight.RayHandler;

public abstract class CompositeObject2D extends Group2D
{  
  protected ModelObject2D modelObject = null;
  protected PhysicObject2D physicObject = null;
  protected LightObject2D lightObject = null;
  
  public CompositeObject2D(TextureRegion region, boolean physic)
  {
    this(region, physic, false);
  }
  
  public CompositeObject2D(TextureRegion region, boolean physic, boolean light)
  {
    this(new ModelObject2D(region), physic, light);
  }
  
  public CompositeObject2D(ModelObject2D model, boolean physic)
  {
    this(model, physic, false);
  }
  
  public CompositeObject2D(ModelObject2D model, boolean physic, boolean light)
  {
    super();
    
    //model
    if (model != null)
    {
      modelObject = model;
      addActor(modelObject);
    }
    
    //light
    if (light)
    {
      lightObject = new LightObject2D()
      {
        @Override
        public boolean initLightsObject(RayHandler lightsWorld, final SnapshotArray<Light2D> lights)
        {
          return initLights(lightsWorld, lights);
        }
      };
      addActor(lightObject);
    }

    //physic
    if (physic)
    {
      physicObject = new PhysicObject2D()
      {
        @Override
        public Body initPhysicObject(World physicsWorls)
        {
          return initPhysic(physicsWorls);
        }
      };
      addActor(physicObject);
    }
  }
  
  public void attachPhysicBody(float offsetX, float offsetY)
  {
    if (physicObject != null)
      physicObject.attachModel(this, offsetX, offsetY);
  }
  
  protected Body initPhysic(World physicsWorls)
  {
    return null;
  }

  protected boolean initLights(RayHandler lightsWorld, final SnapshotArray<Light2D> lights)
  {
    return false;
  }
  
  public final ModelObject2D getModelObject()
  {
    return modelObject;
  }
  
  public final PhysicObject2D getPhysicObject()
  {
    return physicObject;
  }
  
  public final LightObject2D getLightObject()
  {
    return lightObject;
  }

  public final Body getBody()
  {
    if (physicObject != null)
      return physicObject.getBody();
    return null;
  }
  
  public final SnapshotArray<Light2D> getLight2D()
  {
    if (lightObject != null)
      return lightObject.getLights();
    return null;
  }
}
