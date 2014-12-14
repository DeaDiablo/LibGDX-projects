package com.games.CityOfZombies.model;

import java.util.HashMap;

import com.shellGDX.model2D.Scene2D;
import com.shellGDX.model3D.Scene3D;
import com.shellGDX.utils.leveleditor2d.Editor2DLevel;
import com.shellGDX.utils.leveleditor2d.Layer;

public class CityLevel
{ 
  protected HashMap<String, ModelLayer> modelLayers = new HashMap<String, ModelLayer>();
  protected HashMap<String, CityLayer>  cityLayers  = new HashMap<String, CityLayer>();

  public CityLevel(Editor2DLevel editor2Dlevel, Scene2D scene2D, Scene3D scene3D)
  {
    super();
    parseLevel(editor2Dlevel, scene2D, scene3D);
  }
  
  protected void parseLevel(Editor2DLevel editor2Dlevel, Scene2D scene2D, Scene3D scene3D)
  {
    for(Layer layer : editor2Dlevel.layers)
    {
      if (layer.name.compareTo("walls") == 0)
      {
        ModelLayer modelLayer = new ModelLayer(layer);
        modelLayers.put(modelLayer.getName(), modelLayer);
        scene3D.addModel3D(modelLayer);
      }

      CityLayer layerGroup = new CityLayer(layer);
      cityLayers.put(layer.name, layerGroup);
      scene2D.addActor(layerGroup);
    }
  }

  public void showLayer(String name, boolean visible)
  {
    cityLayers.get(name).setVisible(visible);
    modelLayers.get(name).setVisible(visible);
  }
}
