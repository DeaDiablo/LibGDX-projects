package com.games.leveleditor.controller;

import com.badlogic.gdx.utils.Array;
import com.games.leveleditor.model.EditModel;

public class TextureCommand extends Command
{
  protected class ActorTexture
  {
    String texture;
    EditModel model;
  }
  
  public TextureCommand()
  {
    super();
  }
  
  protected Array<ActorTexture> atVec = new Array<ActorTexture>();
  protected String texture = "";
  protected boolean delta = false;
  
  public void addActors(Array<EditModel> actors)
  {
    for (EditModel actor : actors)
      addActor(actor);
  }

  public void addActor(EditModel actor)
  {    
    ActorTexture at = new ActorTexture();
    at.model = actor;
    at.texture = actor.getPath();
    atVec.add(at);
  }
  
  public void setNewTexture(String texture)
  {
    this.texture = texture;
  }
  
  @Override
  public boolean execute()
  {
    if (atVec.size <= 0)
      return false;

    for(ActorTexture at : atVec)
      at.model.setPath(texture);
    
    return true;
  }
  
  @Override
  public void unExecute()
  {
    for(ActorTexture at : atVec)
      at.model.setPath(at.texture);
  }
}
