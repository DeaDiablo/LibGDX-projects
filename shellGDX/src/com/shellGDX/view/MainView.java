package com.shellGDX.view;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.shellGDX.box2dLight.LightWorld2D;
import com.shellGDX.controller.PhysicsWorld2D;
import com.shellGDX.manager.ResourceManager;
import com.shellGDX.model2D.Scene2D;
import com.shellGDX.model3D.Scene3D;
import com.shellGDX.shader.ShaderManager;

public class MainView
{
  // for debug
  private ShapeRenderer      shapeRenderer = null;
  private Box2DDebugRenderer debugRenderer = null;

  public MainView()
  {
    super();
  }
  
  public ShapeRenderer getShapeRenderer()
  {
    if (shapeRenderer == null)
      shapeRenderer = new ShapeRenderer();
    return shapeRenderer;
  }
  
  public Box2DDebugRenderer getDebugRenderer()
  {
    if (debugRenderer == null)
      debugRenderer = new Box2DDebugRenderer();
    return debugRenderer;
  }

  public void dispose()
  {
    if (debugRenderer != null)
      debugRenderer.dispose();
    if (shapeRenderer != null)
      shapeRenderer.dispose();
    ResourceManager.instance.clear();
    ShaderManager.instance.clear();
  }

  public void draw2DScenes(Array<Scene2D> scenes2D)
  {
    for(Scene2D scene : scenes2D)
      scene.draw();
  }
  
  public void draw3DScenes(Array<Scene3D> scenes3D)
  {
    for(Scene3D scene : scenes3D)
      scene.draw();
  }
  
  protected Matrix4 worldMatrix = new Matrix4();
  
  public void drawPhysicsDebug(Camera camera)
  {
    worldMatrix.set(camera.combined);
    worldMatrix.scale(PhysicsWorld2D.BOX_TO_WORLD, PhysicsWorld2D.BOX_TO_WORLD, PhysicsWorld2D.BOX_TO_WORLD);
    getDebugRenderer().render(PhysicsWorld2D.instance, worldMatrix);
  }
  
  public void drawLightWorld()
  {
    LightWorld2D.instance.render();
  }
}
