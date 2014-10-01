package com.games.leveleditor.controller;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

public class RemoveGroupCommand extends Command
{
  protected class Child
  {
    Actor model;
    float x, y;
    float angle;
    float sX, sY;
  }

  public RemoveGroupCommand()
  {
    super();
  }

  protected Array<Child>  children = new Array<Child>();
  protected Group         group = null;
  protected int           index = 0;
  protected Group         parent = null;
  
  public void setGroup(Group model)
  {
    this.group = model;
    parent = group.getParent();
    index = parent.getChildren().indexOf(group, true);
    
    for (Actor childActor : group.getChildren())
    {
      Child child = new Child();
      child.model = childActor;
      child.x = model.getX();
      child.y = model.getY();
      child.angle = model.getRotation();
      child.sX = model.getScaleX();
      child.sY = model.getScaleY();
      children.add(child);
    }
  }

  @Override
  public boolean execute()
  {
    if (children.size <= 0 || group == null || parent == null)
      return false;

    group.remove();

    for(Child child : children)
    {
      child.model.moveBy(group.getX(), group.getY());
      child.model.rotateBy(group.getRotation());
      child.model.setScaleX(child.sX * group.getScaleX());
      child.model.setScaleY(child.sY * group.getScaleY());
      parent.addActor(child.model);
    }
    
    return true;
  }
  
  @Override
  public void unExecute()
  {
    parent.addActorAt(index, group);

    for(Child child : children)
    {
      child.model.setPosition(child.x, child.y);
      child.model.setRotation(child.angle);
      child.model.setScale(child.sX, child.sY);
      group.addActor(child.model);
    }
  }
}
