package com.games.leveleditor.controller;

import com.games.leveleditor.model.Layer;
import com.shellGDX.model2D.Scene2D;

public class DelLayerCommand extends Command
{
  public DelLayerCommand()
  {
    super();
  }

  protected Scene2D scene = null;
  protected Layer   layer = null;

  public void setScene(Scene2D scene)
  {
    this.scene = scene;
  }
  
  public void setLayer(Layer layer)
  {
    this.layer = layer;
  }

  @Override
  public boolean execute()
  {
    if (scene == null || layer == null)
      return false;
    
    if (!scene.getActors().contains(layer, true))
      return false;
    
    layer.remove();
    
    return true;
  }
  
  @Override
  public void unExecute()
  {
    scene.addActor(layer);
  }
}
