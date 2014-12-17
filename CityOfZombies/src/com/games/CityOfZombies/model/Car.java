package com.games.CityOfZombies.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.SnapshotArray;
import com.games.CityOfZombies.light.ConusLight2D3D;
import com.shellGDX.box2dLight.Light2D;
import com.shellGDX.box2dLight.RayHandler;
import com.shellGDX.controller.PhysicsWorld2D;
import com.shellGDX.model2D.CompositeObject2D;

public class Car extends CompositeObject2D
{
  public Car(TextureRegion textureRegion)
  {
    super(textureRegion, true, true);
  }
  
  @Override
  public Body initPhysicObject(World world)
  {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.DynamicBody;
    bodyDef.linearDamping = 20.0f;
    bodyDef.angularDamping = 20.0f;
    bodyDef.fixedRotation = false;
    bodyDef.position.set(getX(), getY());
    bodyDef.position.scl(PhysicsWorld2D.WORLD_TO_BOX);
    bodyDef.angle = MathUtils.degreesToRadians * getRotation();

    Body body = world.createBody(bodyDef);

    PolygonShape polygon = new PolygonShape();
    polygon.setAsBox(modelObject.getWidth() * 0.5f * PhysicsWorld2D.WORLD_TO_BOX,
                     modelObject.getHeight() * 0.5f * PhysicsWorld2D.WORLD_TO_BOX);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = polygon;
    fixtureDef.density = 1.0f;
    fixtureDef.friction = 1.0f;
    Fixture fixture = body.createFixture(fixtureDef);
    fixture.setUserData(this);
    polygon.dispose();
    
    attachPhysicBody(0, 0);

    return body;
  }
  
  @Override
  public boolean initLightsObject(RayHandler lightsWorld, final SnapshotArray<Light2D> lights)
  {
    ConusLight2D3D left_headlight = new ConusLight2D3D(lightsWorld, 256, new Color(0.5f, 0.5f, 0.5f, 0.5f), 1500, 0, 0, getRotation(), 30);
    left_headlight.attachToBody(getBody(), 300, -80);
    left_headlight.setActive(true);
    left_headlight.setStaticLight(false);
    lights.add(left_headlight);
    
    ConusLight2D3D right_headlight = new ConusLight2D3D(lightsWorld, 256, new Color(0.5f, 0.5f, 0.5f, 0.5f), 1500, 0, 0, getRotation(), 30);
    right_headlight.attachToBody(getBody(), 300, 80);
    right_headlight.setActive(true);
    right_headlight.setStaticLight(false);
    lights.add(right_headlight);
  
    return true;
  }
}
