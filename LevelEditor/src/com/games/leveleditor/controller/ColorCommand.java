package com.games.leveleditor.controller;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class ColorCommand extends Command
{
  protected class ActorColor
  {
    Color color = new Color();
    Actor actor;
  }
  
  public ColorCommand()
  {
    super();
  }
  
  protected Array<ActorColor> acVec = new Array<ActorColor>();
  protected float r = -1.0f, g = -1.0f, b = -1.0f, a = -1.0f;
  
  public void addActors(Array<Actor> actors)
  {
    for (Actor actor : actors)
      addActor(actor);
  }

  public void addActor(Actor actor)
  {
    ActorColor ac = new ActorColor();
    ac.actor = actor;
    ac.color.set(actor.getColor());
    acVec.add(ac);
  }
  
  public void setColor(float r, float g, float b, float a)
  {
    this.r = r;
    this.g = g;
    this.b = b;
    this.a = a;
  }
  
  public void setR(float r)
  {
    this.r = r;
  }
  
  public void setG(float g)
  {
    this.g = g;
  }
  
  public void setB(float b)
  {
    this.b = b;
  }
  
  public void setA(float a)
  {
    this.a = a;
  }

  @Override
  public boolean execute()
  {
    if (acVec.size <= 0)
      return false;

    for(ActorColor ac : acVec)
    {
      Color color = ac.actor.getColor();
      if (r >= 0.0f)
        color.r = r;
      if (g >= 0.0f)
        color.g = g;
      if (b >= 0.0f)
        color.b = b;
      if (a >= 0.0f)
        color.a = a;
    }
    
    return true;
  }
  
  @Override
  public void unExecute()
  {
    for(ActorColor ac : acVec)
      ac.actor.setColor(ac.color);
  }
}
