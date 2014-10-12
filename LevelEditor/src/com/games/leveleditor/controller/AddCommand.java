package com.games.leveleditor.controller;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.games.leveleditor.model.Layer;

public class AddCommand extends Command
{
  public AddCommand()
  {
    super();
  }
  
  protected Vector2 bufferPosition = new Vector2();
  protected Vector2 bufferScale = new Vector2();
  protected Group   group = null;
  protected Array<Actor> newModels = new Array<Actor>();
  
  public void setGroup(Group group)
  {
    this.group = group;
  }
  
  public void addModel(Actor model)
  {
    newModels.add(model);
  }
  
  public void addModels(Array<Actor> models)
  {
    newModels.addAll(models);
  }

  @Override
  public boolean execute()
  {
    if (group == null || newModels.size <= 0)
      return false;
    
    for(Actor model : newModels)
    {
      if (!(group instanceof Layer))
      {
        bufferPosition.set(model.getX(), model.getY());
        group.stageToLocalCoordinates(bufferPosition);
        
        bufferScale.set(model.getX() + 1.0f, model.getY());
        group.stageToLocalCoordinates(bufferScale);
        bufferScale.sub(bufferPosition);
        float angle = MathUtils.atan2(bufferScale.y, bufferScale.x) * MathUtils.radiansToDegrees;
        float scaleX = bufferScale.len();
  
        bufferScale.set(model.getX(), model.getY() + 1.0f);
        group.stageToLocalCoordinates(bufferScale);
        bufferScale.sub(bufferPosition);
        float scaleY = bufferScale.len();

        model.setPosition(bufferPosition.x, bufferPosition.y);
        model.rotateBy(angle);
        model.setScale(model.getScaleX() * scaleX, model.getScaleY() * scaleY);
      }
      group.addActor(model);
    }
    
    return true;
  }
  
  @Override
  public void unExecute()
  {
    for(Actor model : newModels)
      model.remove();
  }
}
