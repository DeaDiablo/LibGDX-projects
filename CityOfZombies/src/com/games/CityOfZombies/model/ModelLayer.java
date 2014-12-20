package com.games.CityOfZombies.model;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.shellGDX.manager.ResourceManager;
import com.shellGDX.model3D.Group3D;
import com.shellGDX.model3D.ModelObject3D;
import com.shellGDX.utils.leveleditor2d.Layer;
import com.shellGDX.utils.leveleditor2d.LayerModel;

public class ModelLayer extends Group3D
{
  protected int level = 0;
  
  public ModelLayer(Layer layer, int level)
  {
    super();
    this.level = level;
    parseLayer(layer);
  }
  
  public void parseLayer(Layer layer)
  {
    setName(layer.name);
    setVisible(layer.visible);
    
    for (LayerModel model : layer.children)
      createModel(model, this);
  }

  protected void createModel(LayerModel model, Group3D group)
  {
    if (model.asGroup() != null)
    {
      Group3D group3D = null;
      
      group3D = new Group3D();

      if (group3D != null)
      {
        group3D.setName(model.name);
        group3D.setVisible(model.visible);
        group3D.setPosition(model.position.x, model.position.y, 0);
        group3D.setScale(model.scale.x - 1.0f, model.scale.y - 1.0f, 0.0f);
        group3D.setRotation(0.0f, 0.0f, model.angle);
        group3D.setColor(model.color);
        
        for (LayerModel childModel : model.asGroup().children)
          createModel(childModel, group3D);
      }
      
      group.addModel3D(group3D);
    }
    else
    {
      ModelObject3D model3D = null;
      
      String fileName = model.textureFile;
      fileName = fileName.substring(fileName.lastIndexOf('/') + 1, fileName.lastIndexOf('.')) + ".obj";
      
      try
      {
        model3D = new ModelObject3D(ResourceManager.instance.getModel(fileName));
      }
      catch(GdxRuntimeException exception)
      {
      }
      
      if (model3D != null)
      {
        model3D.setName(model.name);
        model3D.setVisible(model.visible);
        model3D.translate(model.position.x, model.position.y, 200 * level);
        model3D.setScale(model.scale.x, model.scale.y, 1.0f);
        model3D.rotate(0.0f, 0.0f, model.angle);
        model3D.setColor(model.color);
        group.addModel3D(model3D);
      }
    }
  }

  private HashMap<String, Array<ModelObject3D>> mapModels = new HashMap<String, Array<ModelObject3D>>();
  
  public HashMap<String, Array<ModelObject3D>> getModels()
  {
    return mapModels;
  }
  
  @Override
  public void addModel3D(ModelObject3D model)
  {
    String key = String.format("%d %d", (int)model.getX() / CityLayer.xGridSize, (int)model.getY() / CityLayer.yGridSize);
    Array<ModelObject3D> arrayModel = mapModels.get(key);
    if (arrayModel == null)
    {
      arrayModel = new Array<ModelObject3D>();
      mapModels.put(key, arrayModel);
    }
    arrayModel.add(model);
  }
  
  public Array<ModelObject3D> getModels(int x, int y)
  {
    return mapModels.get(String.format("%d %d", x, y));
  }

  protected int oldBlockX = -100000, oldBlockY = -100000;

  @Override
  public boolean update(float delta)
  {
    if (!super.update(delta))
      return false;
    
    Camera camera = scene.getCamera();
    int blockX = (int)camera.position.x / CityLayer.xGridSize;
    int blockY = (int)camera.position.y / CityLayer.yGridSize;
    
    if (blockX == oldBlockX && blockY == oldBlockY)
      return true;
    
    oldBlockX = blockX;
    oldBlockY = blockY;
    
    getChildren().clear();
    
    for (int i = -1; i <= 1; i ++)
    {
      for (int j = -1; j <= 1; j ++)
      {
        Array<ModelObject3D> models = getModels(blockX + i, blockY + j);
        if (models != null && models.size > 0)
          for(ModelObject3D model : models)
            super.addModel3D(model);
      }
    }
    
    return true;
  }
  
  @Override
  public void draw(ModelBatch modelBatch, Environment environment, Shader shader)
  {
    //modelBatch.getRenderContext().setDepthTest(0);
    modelBatch.getRenderContext().setCullFace(GL20.GL_FRONT);
    super.draw(modelBatch, environment, shader);
  }
}
