package com.shellGDX.model2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

public class Scene2D extends Stage
{
  protected int     zIndex = 0;
  protected Group2D root = null;
  protected boolean resize = false;

  protected OrthographicCamera camera = null;
  protected Rectangle cameraRect = new Rectangle(0, 0, 0, 0);
  
  public Scene2D()
  {
    super(new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
    resize = true;
    camera = (OrthographicCamera)getCamera();
    root = new Group2D();
    super.getRoot().addActor(root);
  }
  
    
  public Scene2D(float width, float height)
  {
    super(new ScalingViewport(Scaling.stretch, width, height));
    resize = false;
    camera = (OrthographicCamera)getCamera();
    root = new Group2D();
    super.getRoot().addActor(root);
  }
  
  public Vector2 screenToSceneCoordinates(float screenX, float screenY)
  {
    Vector2 buffer = new Vector2(screenX, screenY);
    buffer = screenToStageCoordinates(buffer);
    return buffer;
  }

  protected void updateCameraRectangle()
  {
    cameraRect.set(camera.position.x - camera.viewportWidth * 0.5f * camera.zoom,
                   camera.position.y - camera.viewportHeight * 0.5f * camera.zoom,
                   camera.viewportWidth * camera.zoom,
                   camera.viewportHeight * camera.zoom);
  }
  
  public Rectangle getCameraRectangle()
  {
    return cameraRect;
  }
  
  public float getCameraLeft()
  {
    return cameraRect.x;
  }
  
  public float getCameraRight()
  {
    return cameraRect.x + cameraRect.width;
  }
  
  public float getCameraBottom()
  {
    return cameraRect.y;
  }
  
  public float getCameraTop()
  {
    return cameraRect.y + cameraRect.height;
  }
  
  public float getCameraWidth()
  {
    return cameraRect.width;
  }
  
  public float getCameraHeight()
  {
    return cameraRect.height;
  }
  
  public void setZIndex(int zIndex)
  {
    this.zIndex = zIndex;
  }

  public int getZIndex()
  {
    return this.zIndex;
  }
  
  public void setResize(boolean resize)
  {
    this.resize = resize;
  }

  public boolean getResize()
  {
    return resize;
  }
  
  @Override
  public void act(float delta)
  {
    camera.update();
    super.act(delta);
    updateCameraRectangle();
  }

  @Override
  public void addActor(Actor actor)
  {
    Array<Actor> children = root.getChildren();
    for (int i = 0; i < children.size; i++)
    {
      if (actor.getZIndex() < children.get(i).getZIndex())
      {
        root.addActorAt(i, actor);
        return;
      }
    }
    root.addActor(actor);
  }

  @Override
  public Group getRoot() {
    return root;
  }

  public void setViewport(int width, int height)
  {
    getViewport().update(width, height);
  }
}
