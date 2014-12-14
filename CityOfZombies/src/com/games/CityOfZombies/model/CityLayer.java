package com.games.CityOfZombies.model;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.shellGDX.manager.ResourceManager;
import com.shellGDX.model2D.Group2D;
import com.shellGDX.model2D.ModelObject2D;
import com.shellGDX.utils.leveleditor2d.Layer;
import com.shellGDX.utils.leveleditor2d.LayerModel;

public class CityLayer extends Group2D
{
  protected static final int xGridSize = 1920;
  protected static final int yGridSize = 1080;

  protected HashMap<String, Array<Actor>> mapActors = new HashMap<String, Array<Actor>>();
  
  public CityLayer(Layer layer)
  {
    super();
    parseLayer(layer);
  }
  
  public void parseLayer(Layer layer)
  {
    setName(layer.name);
    setVisible(layer.visible);
    
    for (LayerModel model : layer.children)
      createModel(model, this);
  }

  protected void createModel(LayerModel model, Group2D group)
  {
    if (model.asGroup() != null)
    {
      Group2D group2D = null;
      
      group2D = new Group2D();

      if (group2D != null)
      {
        group2D.setName(model.name);
        group2D.setVisible(model.visible);
        group2D.setPosition(model.position.x, model.position.y);
        group2D.setRotation(model.angle);
        group2D.setScale(model.scale.x, model.scale.y);
        group2D.setColor(model.color);
        
        for (LayerModel childModel : model.asGroup().children)
          createModel(childModel, group2D);
      }
      
      group.addActor(group2D);
    }
    else
    {
      ModelObject2D model2D = null;

      model2D = new ModelObject2D(ResourceManager.instance.getTextureRegion(model.textureFile, model.u0, model.v0, model.u1, model.v1));
      
      if (model2D != null)
      {
        model2D.setName(model.name);
        model2D.setVisible(model.visible);
        model2D.setPosition(model.position.x, model.position.y);
        model2D.setRotation(model.angle);
        model2D.setScale(model.scale.x, model.scale.y);
        model2D.setColor(model.color);
        group.addActor(model2D);
      }
    }
  }

  @Override
  public void addActor(Actor actor)
  {
    String key = String.format("%d %d", (int)actor.getX() / xGridSize, (int)actor.getY() / yGridSize);
    Array<Actor> arrayObject = mapActors.get(key);
    if (arrayObject == null)
    {
      arrayObject = new Array<Actor>();
      mapActors.put(key, arrayObject);
    }
    arrayObject.add(actor);
  }
  
  public Array<Actor> getActors(int x, int y)
  {
    return mapActors.get(String.format("%d %d", x, y));
  }

  protected int oldBlockX = -100000, oldBlockY = -100000;
  
  @Override
  public boolean update(float deltaTime)
  {
    if (!super.update(deltaTime))
      return false;
    
    Camera camera = scene.getCamera();
    int blockX = (int)camera.position.x / xGridSize;
    int blockY = (int)camera.position.y / yGridSize;
    
    if (blockX == oldBlockX && blockY == oldBlockY)
      return true;
    
    oldBlockX = blockX;
    oldBlockY = blockY;

    getChildren().clear();
    
    for (int i = -1; i <= 1; i ++)
    {
      for (int j = -1; j <= 1; j ++)
      {
        Array<Actor> models = getActors(blockX + i, blockY + j);
        if (models != null && models.size > 0)
          for(Actor model : models)
            super.addActor(model);
      }
    }
    
    return true;
  }
}
