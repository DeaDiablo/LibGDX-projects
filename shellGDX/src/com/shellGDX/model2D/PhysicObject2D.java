package com.shellGDX.model2D;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.shellGDX.GameLog;
import com.shellGDX.controller.PhysicsWorld2D;

public abstract class PhysicObject2D extends Actor
{
  protected Actor    modelObject   = null;
  protected float    offsetX       = 0,
                     offsetY       = 0;
  
  protected Body     body          = null;

  public PhysicObject2D()
  {
    this(0, 0, 0, 1, 1);
  }

  public PhysicObject2D(float x, float y)
  {
    this(x, y, 0, 1, 1);
  }

  public PhysicObject2D(float x, float y, float angle)
  {
    this(x, y, angle, 1, 1);
  }

  public PhysicObject2D(float x, float y, float angle, float scaleX, float scaleY)
  {
    super();
    setPosition(x, y);
    setRotation(angle);
    setScale(scaleX, scaleY);
  }
  
  public void attachModel(Actor model, float offsetX, float offsetY)
  {
    modelObject = model;
    this.offsetX = offsetX;
    this.offsetY = offsetY;
  }
  
  public final Body getBody()
  {
    return body;
  }

  protected Vector2 newPosition = new Vector2();
  protected float   newAngle = 0.0f;

  @Override
  public void act(float delta)
  {
    super.act(delta);

    if (body != null)
    {
      if (modelObject != null)
      {
        body.setActive(modelObject.isVisible());
        newPosition = body.getPosition();
        modelObject.setPosition(newPosition.x * PhysicsWorld2D.BOX_TO_WORLD,
                                newPosition.y * PhysicsWorld2D.BOX_TO_WORLD);
        if (!body.isFixedRotation())
          modelObject.setRotation(body.getAngle() * MathUtils.radDeg);
        return;
      }
      
      body.setActive(isVisible());

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
      newPosition.scl(PhysicsWorld2D.WORLD_TO_BOX);
      body.setTransform(newPosition, body.isFixedRotation() ? body.getAngle() : newAngle * MathUtils.degRad);
    }
  }
  
  protected abstract Body initPhysicObject(World physicsWorls);
  
  @Override
  protected void setStage(Stage stage)
  {
    if (PhysicsWorld2D.instance != null)
    {
      if (body != null)
      {
        PhysicsWorld2D.instance.destroyBody(body);
        body = null;
      }
      
      if (stage == null)
        return;

      body = initPhysicObject(PhysicsWorld2D.instance);
      if (body != null)
      {
        super.setStage(stage);
        return;
      }
    }

    GameLog.instance.writeError("Failed to initialize a physical object!");
  }

  @Override
  public boolean remove()
  {
    if (PhysicsWorld2D.instance != null && body != null)
    {
      PhysicsWorld2D.instance.destroyBody(body);
      body = null;
    }
    return super.remove();
  }
}
