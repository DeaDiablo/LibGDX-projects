package com.games.leveleditor.controller;

import com.games.leveleditor.model.Layer;
import com.shellGDX.model2D.Scene2D;

public class DelLayerCommand extends Command
{
  public DelLayerCommand()
  {
    super();
  }

  protected int     index = 0;
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
    
    index = scene.getRoot().getChildren().indexOf(layer, true);
    layer.remove();
    
    return true;
  }
  
  @Override
  public void unExecute()
  {
    scene.getRoot().addActorAt(index, layer);
  }
}
