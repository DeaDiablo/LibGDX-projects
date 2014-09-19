package com.games.CityOfZombies.model;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.shellGDX.controller.PhysicsWorld2D;
import com.shellGDX.model3D.ModelObject3D;
import com.shellGDX.model3D.Scene3D;

public abstract class ModelPlate extends ModelObject3D
{
  protected Body body = null;

  public ModelPlate(Model model)
  {
    super(model);
  }

  public boolean initPhysicsObject(World world)
  {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.StaticBody;
    bodyDef.linearDamping = 20.0f;
    bodyDef.angularDamping = 20.0f;
    bodyDef.fixedRotation = true;
    bodyDef.position.set(getX(), getY());
    bodyDef.position.scl(PhysicsWorld2D.WORLD_TO_BOX);
    bodyDef.angle = MathUtils.degreesToRadians * (getRoll() + 90.0f);

    body = world.createBody(bodyDef);
    
    return true;
  }
  
  @Override
  protected void setScene(Scene3D scene)
  {
    super.setScene(scene);
    //if (PhysicsWorld2D.instance != null)
      //initPhysicsObject(PhysicsWorld2D.instance);
  }
  
  @Override
  public boolean remove()
  {
    if (body != null && PhysicsWorld2D.instance != null)
    {
      PhysicsWorld2D.instance.destroyBody(body);
      body = null;
    }
    return super.remove();
  }
}
