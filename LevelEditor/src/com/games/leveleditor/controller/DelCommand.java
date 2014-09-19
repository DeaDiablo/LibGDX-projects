package com.games.leveleditor.controller;

import java.util.Vector;

import com.games.leveleditor.model.EditModel;
import com.games.leveleditor.model.Layer;

public class DelCommand extends Command
{
  public DelCommand()
  {
    super();
  }
  
  protected Layer layer = null;
  protected Vector<EditModel> newModels = new Vector<EditModel>();
  
  public void SetLayer(Layer layer)
  {
    this.layer = layer;
  }
  
  public void AddModel(EditModel model)
  {
    newModels.add(model);
  }
  
  public void AddModels(Vector<EditModel> models)
  {
    newModels.addAll(models);
  }

  @Override
  public boolean execute()
  {
    if (layer == null || newModels.isEmpty())
      return false;
    
    for(EditModel model : newModels)
      layer.removeModel(model);
    
    return true;
  }
  
  @Override
  public void unExecute()
  {
    for(EditModel model : newModels)
      layer.addModel(model);
  }
}
