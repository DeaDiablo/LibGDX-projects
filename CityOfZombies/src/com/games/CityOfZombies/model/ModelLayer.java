package com.games.CityOfZombies.model;

import java.util.HashMap;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.shellGDX.model3D.Group3D;
import com.shellGDX.model3D.ModelObject3D;
import com.shellGDX.utils.leveleditor2d.Layer;
import com.shellGDX.utils.leveleditor2d.Settings;

public class ModelLayer extends Group3D
{
  private String name = "";
  
  public ModelLayer()
  {
  }
  
  public String getName()
  {
    return name;
  }

  private HashMap<String, Array<ModelObject3D>> models = new HashMap<String, Array<ModelObject3D>>();
  
  public HashMap<String, Array<ModelObject3D>> getModels()
  {
    return models;
  }
  
  protected String buffer = new String();
  
  public void parseLayer(Layer layer)
  {
    name = layer.getName();
    
    HashMap<String, Array<Actor>> objects = layer.getObjects();
    
    for(Array<Actor> actors : objects.values())
    {
      for (int i = 0; i < actors.size; i++)
      {
        Actor actor = actors.get(i);
        /*if (actor.getFile().compareToIgnoreCase("window.png") == 0)
        {
          Wall model = new Wall(ResourceManager.instance.getModel("window.obj"));
          model.setPosition(texture.getX(), texture.getY(), 0);
          model.setRotation(0, 0, texture.getRotation() - 90);
          addModel(model);
          arrayTextures.removeValue(texture, true);
        }*/
      }
    }
  }
  
  public Array<ModelObject3D> getModels(int x, int y)
  {
    return models.get(String.format("%d %d", x, y));
  }
  
  public void addModel(ModelObject3D model)
  {
    String key = String.format("%d %d", (int)model.getX() / Settings.xGridSize, (int)model.getY() / Settings.yGridSize);
    Array<ModelObject3D> arrayModel = models.get(key);
    if (arrayModel == null)
    {
      arrayModel = new Array<ModelObject3D>();
      models.put(key, arrayModel);
    }
    arrayModel.add(model);
    addModel3D(model);
  }
  
  @Override
  public boolean update(float delta)
  {
    getChildren().clear();
    if (!isVisible())
      return false;
    
    int blockX = (int)scene.getCamera().position.x / Settings.xGridSize;
    int blockY = (int)scene.getCamera().position.y / Settings.yGridSize;
    
    for (int i = -1; i <= 1; i ++)
    {
      for (int j = -1; j <= 1; j ++)
      {
        Array<ModelObject3D> models = getModels(blockX + i, blockY + j);
        if (models != null && models.size > 0)
          for(ModelObject3D model : models)
            getChildren().add(model);
      }
    }
    
    return super.update(delta);
  }
}
