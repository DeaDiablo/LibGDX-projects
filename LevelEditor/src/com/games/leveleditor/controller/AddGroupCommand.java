package com.games.leveleditor.controller;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.games.leveleditor.model.SelectObject;

public class AddGroupCommand extends Command
{
  protected class Child
  {
    Actor model;
    float x, y;
    int index;
  }

  public AddGroupCommand()
  {
    super();
  }

  protected Array<Child>  children = new Array<Child>();
  protected Group         group = null;
  protected Group         parent = null;
  
  public void setGroup(Group group)
  {
    this.group = group;
  }
  
  public void addModels(Array<Actor> models)
  {
    for(Actor model : models)
      addModel(model);
  }
  
  public void addModel(Actor model)
  {
    Child child = new Child();
    child.model = model;
    child.x = model.getX();
    child.y = model.getY();
    child.index = 0;
    children.add(child);
  }

  @Override
  public boolean execute()
  {
    if (children.size <= 0 || group == null)
      return false;

    parent = children.get(0).model.getParent();
    if (parent == null)
      return false;

    float x = 0.0f;
    float y = 0.0f;

    for(Child child : children)
    {
      child.index = parent.getChildren().indexOf(child.model, true);
      ((SelectObject)child.model).setSelection(false);
      x += child.model.getX();
      y += child.model.getY();
    }
    
    group.setPosition(x / children.size, y / children.size);
    
    for(Child child : children)
    {
      child.model.moveBy(-group.getX(), -group.getY());
      group.addActor(child.model);
    }

    parent.addActor(group);
    
    return true;
  }
  
  @Override
  public void unExecute()
  {
    group.remove();

    for(Child child : children)
    {
      child.model.setPosition(child.x, child.y);
      parent.addActorAt(child.index, child.model);
    }
  }
}
