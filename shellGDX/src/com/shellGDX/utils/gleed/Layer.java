package com.shellGDX.utils.gleed;

import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.shellGDX.model2D.Group2D;

public class Layer extends Group2D
{
  private HashMap<String, Array<TextureElement>> textures = new HashMap<String, Array<TextureElement>>();
  private HashMap<String, Array<LayerObject>> objects = new HashMap<String, Array<LayerObject>>();
  protected Properties properties = new Properties();
  
  public Layer()
  {
    super();
  }
  
  public HashMap<String, Array<TextureElement>> getTextures()
  {
    return textures;
  }
  
  public HashMap<String, Array<LayerObject>> getObjects()
  {
    return objects;
  }

  public Properties getProperties()
  {
    return properties;
  }
  
  public Array<TextureElement> getTextures(int x, int y)
  {
    return textures.get(String.format("%d %d", x, y));
  }
  
  public Array<LayerObject> getObjects(int x, int y)
  {
    return objects.get(String.format("%d %d", x, y));
  }
  
  public void addTexture(TextureElement texture)
  {
    String key = String.format("%d %d", (int)texture.getX() / Settings.xGridSize, (int)texture.getY() / Settings.yGridSize);
    Array<TextureElement> arrayTexture = textures.get(key);
    if (arrayTexture == null)
    {
      arrayTexture = new Array<TextureElement>();
      textures.put(key, arrayTexture);
    }
    arrayTexture.add(texture);
  }
  
  public void addObject(LayerObject object)
  {
    String key = String.format("%d %d", (int)object.getX() / Settings.xGridSize, (int)object.getY() / Settings.yGridSize);
    Array<LayerObject> arrayObject = objects.get(key);
    if (arrayObject == null)
    {
      arrayObject = new Array<LayerObject>();
      objects.put(key, arrayObject);
    }
    arrayObject.add(object);
  }

  public void unload(AssetManager assetManager)
  {
    for(Array<TextureElement> arrayTexture : textures.values())
    {
      for (int j = 0; j < arrayTexture.size; ++j)
      {
        TextureElement texture = arrayTexture.get(j);
    
        if (assetManager.isLoaded(texture.path, Texture.class))
        {
          assetManager.unload(texture.path);
        }
      }
    }
  }
}
