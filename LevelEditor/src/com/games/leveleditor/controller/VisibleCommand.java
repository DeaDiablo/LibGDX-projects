package com.games.leveleditor.controller;

import java.util.Vector;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class VisibleCommand extends Command
{
  protected class ActorVisible
  {
    boolean visible;
    Actor actor;
  }
  
  public VisibleCommand()
  {
    super();
  }
  
  protected Vector<ActorVisible> avVec = new Vector<ActorVisible>();
  protected boolean visible = false;
  protected boolean delta = false;
  
  public void addActors(Vector<Actor> actors)
  {
    for (Actor actor : actors)
      addActor(actor);
  }

  public void addActor(Actor actor)
  {
    ActorVisible av = new ActorVisible();
    av.actor = actor;
    av.visible = actor.isVisible();
    avVec.add(av);
  }
  
  public void setNewVisible(boolean visible)
  {
    this.visible = visible;
  }
  
  @Override
  public boolean execute()
  {
    if (avVec.isEmpty())
      return false;

    for(ActorVisible av : avVec)
      av.actor.setVisible(visible);
    
    return true;
  }
  
  @Override
  public void unExecute()
  {
    for(ActorVisible av : avVec)
      av.actor.setVisible(av.visible);
  }
}
