package com.games.CityOfZombies.model;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.shellGDX.utils.leveleditor2d.Layer;
import com.shellGDX.utils.leveleditor2d.Editor2DLevel;

public class CityLevel
{
  protected Editor2DLevel editor2Dlevel = null;
  protected HashMap<String, ModelLayer> modelLayers = new HashMap<String, ModelLayer>();
  
  public CityLevel(Editor2DLevel editor2Dlevel)
  {
    this.editor2Dlevel = editor2Dlevel;

    for(Layer layer : editor2Dlevel.getLayers().values())
    {
      ModelLayer modelLayer = new ModelLayer();
      modelLayer.parseLayer(layer);
      modelLayers.put(modelLayer.getName(), modelLayer);
    }
  }
  
  public Editor2DLevel getEditor2DLevel()
  {
    return editor2Dlevel;
  }

  public boolean update(Camera camera, float deltaTime)
  {
    return editor2Dlevel.update(camera, deltaTime);
  }
  
  public void draw2D(Batch batch, String name)
  {
    Layer layer = editor2Dlevel.getLayer(name);
    if (layer != null)
    {
      batch.begin();
      layer.draw(batch, 1.0f);
      batch.end();
    }
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
