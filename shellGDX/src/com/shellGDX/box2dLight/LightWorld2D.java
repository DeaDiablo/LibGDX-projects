package com.shellGDX.box2dLight;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.shellGDX.controller.PhysicsWorld2D;

public class LightWorld2D
{
  public static float   WORLD_TO_BOX = PhysicsWorld2D.WORLD_TO_BOX;
  public static float   BOX_TO_WORLD = PhysicsWorld2D.BOX_TO_WORLD;
  
  public static RayHandler instance = null;
  
  protected static OrthographicCamera lightCamera = null;
  
  public static void init(OrthographicCamera camera)
  {
    lightCamera = camera;
    instance = new RayHandler(PhysicsWorld2D.instance);
    instance.setMeterToPixel(BOX_TO_WORLD);
    instance.setBlur(true);
    instance.setShadows(true);
    instance.setCulling(true);
    instance.setAmbientLight(0.1f, 0.1f, 0.1f, 0.1f);
  }
  
  public static void update()
  {
    if (instance == null || lightCamera == null)
      return;

    LightWorld2D.instance.setCombinedMatrix(lightCamera.combined,
                                            lightCamera.position.x,
                                            lightCamera.position.y,
                                            lightCamera.viewportWidth * lightCamera.zoom,
                                            lightCamera.viewportHeight * lightCamera.zoom);
    LightWorld2D.instance.update();
  }
  
  public static void destroy()
  {
    lightCamera = null;
    if (instance != null)
    {
      instance.dispose();
      instance = null;
    }
  }
}
