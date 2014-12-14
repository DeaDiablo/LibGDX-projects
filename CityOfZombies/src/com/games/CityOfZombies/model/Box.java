package com.games.CityOfZombies.model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.games.CityOfZombies.light.ShadowFilter;
import com.shellGDX.controller.PhysicsWorld2D;
import com.shellGDX.model2D.PhysicObject2D;

public class Box extends PhysicObject2D
{
  protected float width = 0.0f;
  protected float height = 0.0f;
  
  public Box(float width, float height)
  {
    super();
    this.width = width;
    this.height = height;
  }

  @Override
  protected Body initPhysicObject(World physicsWorld)
  {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.StaticBody;
    bodyDef.linearDamping = 20.0f;
    bodyDef.angularDamping = 20.0f;
    bodyDef.fixedRotation = true;
    bodyDef.position.set(getX(), getY());
    bodyDef.position.scl(PhysicsWorld2D.WORLD_TO_BOX);
    bodyDef.angle = MathUtils.degreesToRadians * (getRotation() + 90.0f);

    body = physicsWorld.createBody(bodyDef);

    PolygonShape box = new PolygonShape();
    box.setAsBox(width * PhysicsWorld2D.WORLD_TO_BOX, height * PhysicsWorld2D.WORLD_TO_BOX);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = box;
    fixtureDef.density = 1.0f;
    fixtureDef.friction = 0.0f;
    Fixture fixture = body.createFixture(fixtureDef);
    fixture.setFilterData(new ShadowFilter());

    box.dispose();
    return body;
  }
}
