package com.games.leveleditor.controller;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class UpDownCommand extends Command
{
  protected int deltaIndex = 0;
  protected int   index = -1;
  protected Actor actor = null;
  protected Group parent = null;

  public void setDeltaIndex(int deltaIndex)
  {
    this.deltaIndex = deltaIndex;
  }
  
  public void setModel(Actor model)
  {
    actor = model;
    parent = actor.getParent();
    index = parent.getChildren().indexOf(actor, true);
  }
  
  @Override
  public boolean execute()
  {
    if (actor == null || parent == null)
      return false;
    
    int newIndex = index + deltaIndex;
    if (newIndex < 0 || newIndex > parent.getChildren().size)
      return false;

    parent.addActorAt(newIndex, actor);
    return true;
  }

  @Override
  public void unExecute()
  {
    parent.addActorAt(index, actor);
  }
}
