package com.shellGDX.model2D;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class ParallaxModelObject2D extends ModelObject2D
{
  protected Vector2 parallaxOffset = new Vector2(0, 0);
  
  public Vector2 getParallax()
  {
    return parallaxOffset.cpy();
  }
  
  public void setParallax(Vector2 parallax)
  {
    parallaxOffset.set(parallax);
  }
  
  public void setParallax(float x, float y)
  {
    parallaxOffset.set(x, y);
  }
  
  protected Vector3 cameraPosition = new Vector3();
  protected Vector3 cameraScale    = new Vector3();
  protected Vector2 modelPosition  = new Vector2();
  
  @Override
  public void draw(Batch batch, float parentAlpha)
  {
    if (parallaxOffset.x == 0 && parallaxOffset.y == 0)
    {
      super.draw(batch, parentAlpha);
      return;
    }

    modelPosition.set(getX(), getY());
    batch.getProjectionMatrix().getTranslation(cameraPosition);
    batch.getProjectionMatrix().getScale(cameraScale);
    setPosition(modelPosition.x + (cameraPosition.x + modelPosition.x * cameraScale.x) * parallaxOffset.x,
                modelPosition.y + (cameraPosition.y + modelPosition.y * cameraScale.y) * parallaxOffset.y);
    cameraPosition.scl(960, 540, 0);
    super.draw(batch, parentAlpha);
    setPosition(modelPosition);
  }
}
