package com.games.leveleditor.controller;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

public class DelCommand extends Command
{
  protected class ActorDel
  {
    int index;
    Actor actor;
    Group parent;
  }
  
  public DelCommand()
  {
    super();
  }

  protected Array<ActorDel> adArray = new Array<ActorDel>();
  
  public void addModel(Actor model)
  {
    Group parent = model.getParent();
    if (parent == null)
      return;
    
    ActorDel ad = new ActorDel();
    ad.actor = model;
    ad.parent = parent;
    ad.index = parent.getChildren().indexOf(model, true);
    adArray.add(ad);
  }
  
  public void addModels(Array<Actor> models)
  {
    for(Actor model : models)
      addModel(model);
  }

  @Override
  public boolean execute()
  {
    if (adArray.size <= 0)
      return false;
    
    for(ActorDel model : adArray)
      model.actor.remove();
    
    return true;
  }
  
  @Override
  public void unExecute()
  {
    for(ActorDel model : adArray)
      model.parent.addActorAt(model.index, model.actor);
  }
}
