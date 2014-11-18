package com.games.leveleditor.controller;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.games.leveleditor.model.Layer;

public class AddCommand extends Command
{
  protected class NewActor
  {
    Actor actor;
    int index;
  }
  
  public AddCommand()
  {
    super();
  }
  
  protected Vector2 bufferPosition = new Vector2();
  protected Vector2 bufferScale = new Vector2();
  protected Group   group = null;
  protected Array<NewActor> newModels = new Array<NewActor>();
  
  public void setGroup(Group group)
  {
    this.group = group;
  }
  
  public void addModel(Actor model)
  {
    NewActor newActor = new NewActor();
    newActor.actor = model;
    newActor.index = -1;
    newModels.add(newActor);
  }
  
  public void addModel(Actor model, int index)
  {
    NewActor newActor = new NewActor();
    newActor.actor = model;
    newActor.index = index;
    newModels.add(newActor);
  }
  
  public void addModels(Array<Actor> models)
  {
    for(Actor actor : models)
      addModel(actor);
  }
  
  public void addModels(Array<Actor> models, int index)
  {
    for(Actor actor : models)
      addModel(actor, index++);
  }


  @Override
  public boolean execute()
  {
    if (group == null || newModels.size <= 0)
      return false;
    
    for(NewActor newActor : newModels)
    {
      if (!(group instanceof Layer))
      {
        bufferPosition.set(newActor.actor.getX(), newActor.actor.getY());
        group.stageToLocalCoordinates(bufferPosition);
        
        bufferScale.set(newActor.actor.getX() + 1.0f, newActor.actor.getY());
        group.stageToLocalCoordinates(bufferScale);
        bufferScale.sub(bufferPosition);
        float angle = MathUtils.atan2(bufferScale.y, bufferScale.x) * MathUtils.radiansToDegrees;
        float scaleX = bufferScale.len();
  
        bufferScale.set(newActor.actor.getX(), newActor.actor.getY() + 1.0f);
        group.stageToLocalCoordinates(bufferScale);
        bufferScale.sub(bufferPosition);
        float scaleY = bufferScale.len();

        newActor.actor.setPosition(bufferPosition.x, bufferPosition.y);
        newActor.actor.rotateBy(angle);
        newActor.actor.setScale(newActor.actor.getScaleX() * scaleX, newActor.actor.getScaleY() * scaleY);
      }

      if (newActor.index > 0)
        group.addActorAt(newActor.index, newActor.actor);
      else
        group.addActor(newActor.actor);
    }
    
    return true;
  }
  
  @Override
  public void unExecute()
  {
    for(NewActor newActor : newModels)
      newActor.actor.remove();
  }
}
