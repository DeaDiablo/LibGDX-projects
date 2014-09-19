package com.shellGDX.controller;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.shellGDX.box2dLight.LightWorld2D;
import com.shellGDX.manager.SoundManager;
import com.shellGDX.model2D.Scene2D;
import com.shellGDX.model3D.Scene3D;

public class MainController extends InputMultiplexer
{
  // Scenes
  private SnapshotArray<Scene2D> scenes2D = new SnapshotArray<Scene2D>(false, 1, Scene2D.class);
  private SnapshotArray<Scene3D> scenes3D = new SnapshotArray<Scene3D>(false, 1, Scene3D.class);

  public MainController()
  {
    super();
  }

  // Scenes2D
  public SnapshotArray<Scene2D> getScenes2D()
  {
    return scenes2D;
  }

  public boolean addScene2D(Scene2D scene)
  {
    if (scenes2D.contains(scene, true))
      return false;

    if (scene instanceof InputProcessor)
      addProcessor(scene);
    scenes2D.add(scene);
    
    return true;
  }

  public void removeScene2D(Scene2D scene)
  {
    if (!scenes2D.contains(scene, true))
      return;

    if (scene instanceof InputProcessor)
      removeProcessor(scene);

    scenes2D.removeValue(scene, true);
    scene.dispose();
  }
  
  //Scenes3D

  public SnapshotArray<Scene3D> getScenes3D()
  {
    return scenes3D;
  }

  public boolean addScene3D(Scene3D scene)
  {
    if (scenes3D.contains(scene, true))
      return false;

    scenes3D.add(scene);
    
    return true;
  }

  public void removeScene3D(Scene3D scene)
  {
    if (!scenes3D.contains(scene, true))
      return;

    scenes3D.removeValue(scene, true);
    scene.dispose();
  }

  public void update(float deltaTime)
  {
    TimeController.update(deltaTime);
    SoundManager.instance.update();
    PhysicsWorld2D.update(deltaTime);

    // update elements
    if (scenes2D.size > 0)
    {
      Scene2D[] scenesUpdate = scenes2D.begin();
      for (int i = 0, n = scenes2D.size; i < n; i++)
      {
        scenesUpdate[i].act(deltaTime);
      }
      scenes2D.end();
      LightWorld2D.update();
    }
    
    if (scenes3D.size > 0)
    {
      Scene3D[] scenesUpdate = scenes3D.begin();
      for (int i = 0, n = scenes3D.size; i < n; i++)
      {
        scenesUpdate[i].act(deltaTime);
      }
      scenes3D.end();
    }
  }

  public void clear()
  {
    super.clear();
    
    for(Scene2D scene : scenes2D)
      removeScene2D(scene);
    scenes2D.clear();
    
    for(Scene3D scene : scenes3D)
      removeScene3D(scene);
    scenes3D.clear();
    
    PhysicsWorld2D.destroy();
    LightWorld2D.destroy();
  }

  public void resize(int width, int height)
  {
    for (Scene2D scene : scenes2D)
    {
      if (scene.getResize())
        scene.setViewport(width, height); 
    }
    
    for (Scene3D scene : scenes3D)
    {
      if (scene.getResize())
        scene.setViewport(width, height);
    }
 
    if (LightWorld2D.instance != null)
    {
      LightWorld2D.instance.useCustomViewport(0, 0, width, height);
    }
  }
}
