package com.games.leveleditor.controller;

import com.badlogic.gdx.utils.Array;
import com.games.leveleditor.model.EditModel;

public class TexCoordCommand extends Command
{
  protected class ActorTexCoord
  {
    float u0, v0, u1, v1;
    EditModel model;
  }
  
  public TexCoordCommand()
  {
    super();
  }
  
  protected Array<ActorTexCoord> atVec = new Array<ActorTexCoord>();
  protected float u0 = 0.0f, v0 = 0.0f, u1 = 1.0f, v1 = 1.0f;
  protected boolean delta = false;
  
  public void addActors(Array<EditModel> actors)
  {
    for (EditModel actor : actors)
      addActor(actor);
  }

  public void addActor(EditModel actor)
  {    
    ActorTexCoord at = new ActorTexCoord();
    at.model = actor;
    at.u0 = actor.getU0();
    at.v0 = actor.getV0();
    at.u1 = actor.getU1();
    at.v1 = actor.getV1();
    atVec.add(at);
  }
  
  public void setTexCoord(float u0, float v0, float u1, float v1)
  {
    this.u0 = u0;
    this.v0 = v0;
    this.u1 = u1;
    this.v1 = v1;
  }
  
  @Override
  public boolean execute()
  {
    if (atVec.size <= 0)
      return false;

    for(ActorTexCoord at : atVec)
      at.model.setRegion(u0, v0, u1, v1);
    
    return true;
  }
  
  @Override
  public void unExecute()
  {
    for(ActorTexCoord at : atVec)
      at.model.setRegion(at.u0, at.v0, at.u1, at.v1);
  }
}
