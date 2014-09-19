package com.games.leveleditor.controller;

import java.util.Vector;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class RotateCommand extends Command
{
  protected class ActorRotation
  {
    float angle;
    Actor actor;
  }
  
  public RotateCommand()
  {
    super();
  }
  
  protected Vector<ActorRotation> arVec = new Vector<ActorRotation>();
  protected float angle = 0;
  protected boolean delta = false;

  public void addActor(Actor actor)
  {
    ActorRotation ar = new ActorRotation();
    ar.actor = actor;
    ar.angle = actor.getRotation();
    arVec.add(ar);
  }
  
  public void setAngle(float angle)
  {
    this.angle = angle;
    delta = false;
  }
  
  public void setDelta(float angle)
  {
    this.angle = angle;
    delta = true;
  }
  
  @Override
  public boolean execute()
  {
    if (arVec.isEmpty())
      return false;

    if (!delta)
    {
      for(ActorRotation ar : arVec)
        ar.actor.setRotation(angle);
    }
    else
    {
      for(ActorRotation ar : arVec)
        ar.actor.setRotation(ar.angle + angle);
    }
    
    return true;
  }
  
  @Override
  public void unExecute()
  {
    for(ActorRotation ar : arVec)
      ar.actor.setRotation(ar.angle);
  }
}
