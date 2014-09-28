package com.games.leveleditor.controller;

import com.games.leveleditor.model.Layer;
import com.shellGDX.model2D.Scene2D;

public class AddLayerCommand extends Command
{
  public AddLayerCommand()
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

    scene.addActor(layer);
    
    return true;
  }
  
  @Override
  public void unExecute()
  {
    layer.remove();
  }
}
