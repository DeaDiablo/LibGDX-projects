package com.games.leveleditor.controller;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

public class AddCommand extends Command
{
  public AddCommand()
  {
    super();
  }
  
  protected Group group = null;
  protected Array<Actor> newModels = new Array<Actor>();
  
  public void setGroup(Group group)
  {
    this.group = group;
  }
  
  public void addModel(Actor model)
  {
    newModels.add(model);
  }
  
  public void addModels(Array<Actor> models)
  {
    newModels.addAll(models);
  }

  @Override
  public boolean execute()
  {
    if (group == null || newModels.size <= 0)
      return false;
    
    for(Actor model : newModels)
    {
      model.moveBy(-group.getX(), -group.getY());
      group.addActor(model);
    }
    
    return true;
  }
  
  @Override
  public void unExecute()
  {
    for(Actor model : newModels)
      model.remove();
  }
}
