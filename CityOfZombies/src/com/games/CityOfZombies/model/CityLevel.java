package com.games.CityOfZombies.model;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.shellGDX.utils.gleed.Layer;
import com.shellGDX.utils.gleed.Level;

public class CityLevel
{
  protected Level gleedLevel = null;
  protected HashMap<String, ModelLayer> modelLayers = new HashMap<String, ModelLayer>();
  
  public CityLevel(Level gleedLevel)
  {
    this.gleedLevel = gleedLevel;

    for(Layer layer : gleedLevel.getLayers().values())
    {
      ModelLayer modelLayer = new ModelLayer();
      modelLayer.parseLayer(layer);
      modelLayers.put(modelLayer.getName(), modelLayer);
    }
  }

  public boolean update(Camera camera, float deltaTime)
  {
    return gleedLevel.update(camera, deltaTime);
  }
  
  public void draw2D(Batch batch, String name)
  {
    batch.begin();
    gleedLevel.getLayer(name).draw(batch, 1.0f);
    batch.end();
  }
  
  public void draw3DAll(Camera camera, ModelBatch batch, Shader shader)
  {
    batch.begin(camera);
    for(ModelLayer layer : modelLayers.values())
      layer.draw(batch, null, shader);
    batch.end();
  }
  
  public void draw3D(Batch batch, String name)
  {
    batch.begin();

    batch.end();
  }
}
