package com.shellGDX.controller;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.shellGDX.box2dLight.RayHandler;

public class PhysicsWorld2D
{
  public static float   WORLD_TO_BOX = 0.01f;
  public static float   BOX_TO_WORLD = 100f;

  public static boolean fixedTime    = false;
  public static World   instance     = null;

  public static void init(Vector2 gravity, boolean doSleep)
  {
    destroy();
    instance = new World(gravity, doSleep);
    RayHandler.useDiffuseLight(true);
    RayHandler.setGammaCorrection(true);
  }
  
  public static void destroy()
  {
    if (instance != null)
    {
      instance.dispose();
      instance = null;
    }
  }

  public static void update(float deltaTime)
  {
    if (instance != null)
      instance.step(fixedTime ? 1 / 60.0f : deltaTime, 6, 2);
  }
}
