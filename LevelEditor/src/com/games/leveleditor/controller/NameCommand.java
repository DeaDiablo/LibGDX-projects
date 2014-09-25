package com.games.leveleditor.controller;

import java.util.Vector;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class NameCommand extends Command
{
  protected class ActorName
  {
    String name;
    Actor actor;
  }
  
  public NameCommand()
  {
    super();
  }
  
  protected Vector<ActorName> anVec = new Vector<ActorName>();
  protected String name = "";
  protected boolean delta = false;
  
  public void addActors(Vector<Actor> actors)
  {
    for (Actor actor : actors)
      addActor(actor);
  }

  public void addActor(Actor actor)
  {
    ActorName an = new ActorName();
    an.actor = actor;
    an.name = actor.getName();
    anVec.add(an);
  }
  
  public void setNewName(String name)
  {
    this.name = name;
  }
  
  @Override
  public boolean execute()
  {
    if (anVec.isEmpty())
      return false;

    for(ActorName an : anVec)
      an.actor.setName(name);
    
    return true;
  }
  
  @Override
  public void unExecute()
  {
    for(ActorName an : anVec)
      an.actor.setName(an.name);
  }
}
