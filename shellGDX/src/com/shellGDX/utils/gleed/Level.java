package com.shellGDX.utils.gleed;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.shellGDX.model2D.Group2D;

public class Level extends Group2D
{
  private Properties properties = new Properties();
  private HashMap<String, Layer> layers = new HashMap<String, Layer>();
  private String levelName = "";
  
  public Properties getProperties()
  {
    return properties;
  }

  public int getNumLayers()
  {
    return layers.size();
  }

  public HashMap<String, Layer> getLayers()
  {
    return layers;
  }

  public Layer getLayer(int index)
  {
    if (index < 0 || index >= layers.size())
    {
      return null;
    }
    
    return layers.get(index);
  }

  public Layer getLayer(String name)
  {
    return layers.get(name);
  }
  
  public void setName(String name)
  {
    levelName = name;
  }
  
  public String getName()
  {
    return levelName;
  }
  
  protected Vector2   bufferVec = new Vector2();
  protected Rectangle rectBound = new Rectangle();
  protected int       oldBlockX = -100000, oldBlockY = -100000;
  protected Camera    camera    = null;
  
  @Override
  protected void setStage(Stage stage)
  {
    if (stage != null)
      camera = stage.getCamera();
    super.setStage(stage);
  }
  
  @Override
  public boolean update(float deltaTime)
  {
    return update(camera, deltaTime);
  }

  public boolean update(Camera camera, float deltaTime)
  {
    if (!super.update(deltaTime))
      return false;
    
    int blockX = (int)camera.position.x / Settings.xGridSize;
    int blockY = (int)camera.position.y / Settings.yGridSize;
    
    if (blockX == oldBlockX && blockY == oldBlockY)
      return true;
    
    oldBlockX = blockX;
    oldBlockY = blockY;
    
    for(Layer layer : layers.values())
    {
      layer.clearChildren();
      for (int x = -1; x <= 1; x ++)
      {
        for (int y = -1; y <= 1; y ++)
        {
          Array<TextureElement> textures = layer.getTextures(blockX + x, blockY + y);
          if (textures != null && textures.size > 0)
            for(TextureElement texture : textures)
              layer.addActor(texture);
        }
      }
    }
    
    return true;
  }
}
