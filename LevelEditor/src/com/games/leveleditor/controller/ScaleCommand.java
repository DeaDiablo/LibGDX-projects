package com.games.leveleditor.controller;

import java.util.Vector;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class ScaleCommand extends Command
{
  protected class ActorScale
  {
    float x, y;
    Actor actor;
  }
  
  public ScaleCommand()
  {
    super();
  }
  
  protected Vector<ActorScale> asVec = new Vector<ActorScale>();
  protected float x = 0, y = 0;
  protected boolean delta = false;

  public void addActor(Actor actor)
  {
    ActorScale as = new ActorScale();
    as.actor = actor;
    as.x = actor.getScaleX();
    as.y = actor.getScaleY();
    asVec.add(as);
  }
  
  public void setNewScale(float x, float y)
  {
    this.x = x;
    this.y = y;
    delta = false;
  }
  
  public void setDelta(float x, float y)
  {
    this.x = x;
    this.y = y;
    delta = true;
  }
  
  @Override
  public boolean execute()
  {
    if (asVec.isEmpty())
      return false;

    if (!delta)
    {
      for(ActorScale as : asVec)
        as.actor.setScale(x, y);
    }
    else
    {
      for(ActorScale as : asVec)
        as.actor.setScale(as.x + x, as.y + y);
    }
    
    return true;
  }
  
  @Override
  public void unExecute()
  {
    for(ActorScale as : asVec)
      as.actor.setScale(as.x, as.y);
  }
}
