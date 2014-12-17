package com.games.CityOfZombies.model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.games.CityOfZombies.light.ShadowFilter;
import com.shellGDX.controller.PhysicsWorld2D;
import com.shellGDX.model2D.PhysicObject2D;

public class Circle extends PhysicObject2D
{
  protected float radius = 0.0f;
  
  public Circle(float radius)
  {
    super();
    this.radius = radius;
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

    CircleShape circle = new CircleShape();
    circle.setRadius(radius * PhysicsWorld2D.WORLD_TO_BOX);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = circle;
    fixtureDef.density = 1.0f;
    fixtureDef.friction = 0.0f;
    Fixture fixture = body.createFixture(fixtureDef);
    fixture.setFilterData(new ShadowFilter());

    circle.dispose();
    return body;
  }
}
