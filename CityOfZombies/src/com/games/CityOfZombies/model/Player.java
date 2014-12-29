package com.games.CityOfZombies.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.SnapshotArray;
import com.games.CityOfZombies.light.ConusLight2D3D;
import com.games.CityOfZombies.light.PointLight2D3D;
import com.shellGDX.box2dLight.Light2D;
import com.shellGDX.box2dLight.RayHandler;
import com.shellGDX.controller.PhysicsWorld2D;
import com.shellGDX.model2D.CompositeObject2D;

public class Player extends CompositeObject2D implements InputProcessor
{
  public Player(TextureRegion textureRegion)
  {
    super(textureRegion, true, true);
  }

  @Override
  protected Body initPhysicObject(World world)
  {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.DynamicBody;
    bodyDef.linearDamping = 20.0f;
    bodyDef.angularDamping = 20.0f;
    bodyDef.fixedRotation = true;
    bodyDef.position.set(getX(), getY());
    bodyDef.position.scl(PhysicsWorld2D.WORLD_TO_BOX);
    bodyDef.angle = MathUtils.degreesToRadians * getRotation();

    Body body = world.createBody(bodyDef);

    CircleShape circle = new CircleShape();
    circle.setRadius(modelObject.getWidth() * 0.25f * PhysicsWorld2D.WORLD_TO_BOX);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = circle;
    fixtureDef.density = 1.0f;
    fixtureDef.friction = 1.0f;
    Fixture fixture = body.createFixture(fixtureDef);
    fixture.setUserData(this);
    circle.dispose();

    attachPhysicBody(0, 0);

    return body;
  }
  
  protected ConusLight2D3D conusLight;
  
  protected boolean initLightsObject(RayHandler lightsWorld, final SnapshotArray<Light2D> lights)
  {
    PointLight2D3D pointLight = new PointLight2D3D(lightsWorld, 512, new Color(1.0f, 1.0f, 1.0f, 1.0f), 200, 0, 0);
    pointLight.attachToBody(getBody(), 0, 0);
    pointLight.setActive(true);
    pointLight.setStaticLight(false);
    pointLight.setSoft(false);
    lights.add(pointLight);
    

    conusLight = new ConusLight2D3D(lightsWorld, 512, new Color(1.0f, 1.0f, 1.0f, 1.0f), 1500, 0, 0, getRotation() + 90.0f, 30.0f);
    //conusLight.attachToBody(getBody(), 0, 0);
    conusLight.setActive(true);
    conusLight.setStaticLight(false);
    conusLight.setSoft(true);
    conusLight.setSoftnessLength(10);
    
    lights.add(conusLight);
    
    return true;
  }
  
  //control
  protected final float speedValue = 1000.0f; 
  protected Vector2 speed          = new Vector2();
  protected Vector2 moveVec        = new Vector2(0, 0);
  protected Vector2 mousePoint     = new Vector2(0, 0);
  
  @Override
  public boolean update(float deltaTime)
  {
    if (!super.update(deltaTime))
      return false;

    conusLight.setPosition(getX(), getY());
    conusLight.setDirection(getRotation() + 90.0f);

    speed.set(moveVec);
    speed.scl(PhysicsWorld2D.WORLD_TO_BOX * speedValue);
    getBody().setLinearVelocity(speed);

    return true;
  };
  
  protected boolean keyA = false,
                    keyD = false,
                    keyW = false,
                    keyS = false;

  @Override
  public boolean keyDown(int keycode)
  {
    switch (keycode)
    {
      case Input.Keys.A:
      case Input.Keys.LEFT:
        if (!keyA)
        {
          moveVec.x -= 1.0f;
          keyA = true;
        }
        break;

      case Input.Keys.D:
      case Input.Keys.RIGHT:
        if (!keyD)
        {
          moveVec.x += 1.0f;
          keyD = true;
        }
        break;

      case Input.Keys.W:
      case Input.Keys.UP:
        if (!keyW)
        {
          moveVec.y += 1.0f;
          keyW = true;
        }
        break;

      case Input.Keys.S:
      case Input.Keys.DOWN:
        if (!keyS)
        {
          moveVec.y -= 1.0f;
          keyS = true;
        }
        break;
        
      default:
        return false;
    }
    
    return true;
  }

  @Override
  public boolean keyUp(int keycode)
  {
    switch (keycode)
    {
      case Input.Keys.A:
      case Input.Keys.LEFT:
        if (keyA)
        {
          moveVec.x += 1.0f;
          keyA = false;
        }
        break;

      case Input.Keys.D:
      case Input.Keys.RIGHT:
        if (keyD)
        {
          moveVec.x -= 1.0f;
          keyD = false;
        }
        break;

      case Input.Keys.W:
      case Input.Keys.UP:
        if (keyW)
        {
          moveVec.y -= 1.0f;
          keyW = false;
        }
        break;

      case Input.Keys.S:
      case Input.Keys.DOWN:
        if (keyS)
        {
          moveVec.y += 1.0f;
          keyS = false;
        }
        break;
        
      default:
        return false;
    }
    return true;
  }

  @Override
  public boolean keyTyped(char character)
  {
    return false;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button)
  {
    return false;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button)
  {
    return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer)
  {
    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY)
  {
    mousePoint.set(screenX, Gdx.graphics.getHeight() -  screenY);
    mousePoint.sub(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f);
    setRotation(MathUtils.atan2(mousePoint.y, mousePoint.x) * MathUtils.radiansToDegrees - 90.0f);
    return true;
  }

  @Override
  public boolean scrolled(int amount)
  {
    return false;
  }
}
