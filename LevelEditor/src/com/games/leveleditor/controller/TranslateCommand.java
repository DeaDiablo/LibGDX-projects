package com.games.leveleditor.controller;

import java.util.Vector;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class TranslateCommand extends Command
{
  protected class ActorPosition
  {
    float x, y;
    Actor actor;
  }
  
  public TranslateCommand()
  {
    super();
  }
  
  protected Vector<ActorPosition> apVec = new Vector<ActorPosition>();
  protected float x = 0, y = 0;
  protected boolean delta = false;
  
  public void addActors(Vector<Actor> actors)
  {
    for (Actor actor : actors)
      addActor(actor);
  }

  public void addActor(Actor actor)
  {
    ActorPosition ap = new ActorPosition();
    ap.actor = actor;
    ap.x = actor.getX();
    ap.y = actor.getY();
    apVec.add(ap);
  }
  
  public void setNewPosition(float x, float y)
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
    if (apVec.isEmpty())
      return false;
    
    if (!delta)
    {
      for(ActorPosition ap : apVec)
        ap.actor.setPosition(x, y);
    }
    else
    {
      for(ActorPosition ap : apVec)
        ap.actor.setPosition(ap.x + x, ap.y + y);
    }
    
    return true;
  }
  
  @Override
  public void unExecute()
  {
    for(ActorPosition ap : apVec)
      ap.actor.setPosition(ap.x, ap.y);
  }
}
