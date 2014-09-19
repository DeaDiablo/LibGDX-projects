package com.games.leveleditor.model;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.shellGDX.model2D.ModelObject2D;

public class EditModel extends ModelObject2D
{
  private static HashMap<String, TextureRegion> editRegions = new HashMap<String, TextureRegion>();
  
  public static void setRegionEdit(String name, TextureRegion region)
  {
    editRegions.put(name, region);
  }
  
  private static boolean hideMode = false;
  
  public static void setHideMode(boolean modeOn)
  {
    hideMode = modeOn;
  }
  
  public static boolean getHideMode()
  {
    return hideMode;
  }
  
  protected boolean select = false;
  
  public EditModel(TextureRegion region)
  {
    super(region);
  }
  
  public EditModel(TextureRegion region, float x, float y)
  {
    super(region, x, y);
  }
  
  public void setSelection(boolean select)
  {
    this.select = select;
  }
  
  public boolean isSelected()
  {
    return select;
  }
  
  public boolean isRotate(Vector2 touch)
  {
    if (hideMode)
      return false;
    
    float size = getSize();
    float maxX = bound.x + bound.width;
    float maxY = bound.y + bound.height;
    
    if (touch.x < maxX || touch.y < maxY)
      return false;
    
    if (touch.x > maxX + 3.0f * size || touch.y > maxY + 3.0f * size)
      return false;
    
    return true;
  }
  
  public boolean isScale(Vector2 touch)
  {
    if (hideMode)
      return false;

    float size = getSize();
    float maxX = bound.x + bound.width;
    float minY = bound.y;
    
    if (touch.x < maxX || touch.y < minY - 3.0f * size)
      return false;
    if (touch.x > maxX + 3.0f * size || touch.y > minY)
      return false;
    return true;
  }
  
  protected float getSize()
  {
    float size = 20;
    if (size > bound.getWidth() * 0.5f)
      size = bound.getWidth() * 0.5f;
    if (size > bound.getHeight() * 0.5f)
      size = bound.getHeight() * 0.5f;
    return size;
  }

  public EditModel copy()
  {
    EditModel newModel = new EditModel(region, getX(), getY());
    newModel.setRotation(getRotation());
    newModel.setScale(getScaleX(), getScaleY());
    newModel.setAlign(getHorzAlign(), getVertAlign());
    return newModel;
  }

  public void saveModel(Element elementModel)
  {
    
  }
  
  protected Batch editBatch = new SpriteBatch();

  public void drawSelect(Batch batch, float parentAlpha)
  {
    batch.end();

    editBatch.setProjectionMatrix(batch.getProjectionMatrix());
    editBatch.begin();
    editBatch.setColor(1, 1, 1, parentAlpha);
    
    float size = getSize();
    float minX = bound.x;
    float minY = bound.y;
    float maxX = bound.x + bound.width;
    float maxY = bound.y + bound.height;
    
    TextureRegion region = editRegions.get("bb");
    if (region!= null)
    {
      float width = region.getRegionWidth();
      float height = region.getRegionHeight();
      float halfWidth = width * 0.5f;
      float halfHeight = height * 0.5f;
      float scale = 2.0f * size / width;
      
      editBatch.draw(region,
                     minX - halfWidth, maxY - halfHeight,
                     halfWidth, halfHeight,
                     width, height,
                     scale, scale,
                     0);
      
      editBatch.draw(region,
                     minX - halfWidth, minY - halfHeight,
                     halfWidth, halfHeight,
                     width, height,
                     scale, scale,
                     90);

      editBatch.draw(region,
                     maxX - halfWidth, minY - halfHeight,
                     halfWidth, halfHeight,
                     width, height,
                     scale, scale,
                     180);
      
      editBatch.draw(region,
                     maxX - halfWidth, maxY - halfHeight,
                     halfWidth, halfHeight,
                     width, height,
                     scale, scale,
                     270);
    }
    
    if (!hideMode)
    {
      region = editRegions.get("scale");
      if (region != null)
      {
        float width = region.getRegionWidth();
        float height = region.getRegionHeight();
        float scale = 3.0f * size / width;
  
        editBatch.draw(region,
                       maxX - width * 0.5f + 2 * size, minY - height * 0.5f - 2 * size,
                       width * 0.5f, height * 0.5f,
                       width, height,
                       scale, scale,
                       0);
      }
      
      region = editRegions.get("rotate");
      if (region != null)
      {
        float width = region.getRegionWidth();
        float height = region.getRegionHeight();
        float scale = 3.0f * size / width;
  
        editBatch.draw(region,
                       maxX - width * 0.5f + 2 * size, maxY - height * 0.5f + 2 * size,
                       width * 0.5f, height * 0.5f,
                       width, height,
                       scale, scale,
                       0);
      }
    }
    
    region = editRegions.get("point");
    if (region != null)
    {
      float width = region.getRegionWidth();
      float height = region.getRegionHeight();
      float scale = 2.0f * size / width;

      float x = minX;
      if (hAlign == Align.CENTER)
        x += bound.getWidth() * 0.5f;
      else if (hAlign == Align.RIGHT)
        x += bound.getWidth();
      
      float y = minY;
      if (vAlign == Align.CENTER)
        y += bound.getHeight() * 0.5f;
      else if (vAlign == Align.TOP)
        y += bound.getHeight();

      editBatch.draw(region,
                     x - width * 0.5f, y - height * 0.5f,
                     width * 0.5f, height * 0.5f,
                     width, height,
                     scale, scale,
                     0);
    }
    
    editBatch.end();
    batch.begin();
  }
}
