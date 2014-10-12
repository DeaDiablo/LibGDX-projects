package com.games.leveleditor.controller;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.games.leveleditor.screen.MainScreen;
import com.shellGDX.GameInstance;

public class GoInGroupCommand extends Command
{
  protected Group         group = null;
  protected Group         parent = null;
  protected MainScreen    screen  = null;
  
  public void setGroup(Group group, Group prevGroup)
  {
    this.group = group;
    parent = prevGroup;
    screen = (MainScreen)GameInstance.game.getScreen();
  }

  @Override
  public boolean execute()
  {
    if (group == null || parent == null || screen == null)
      return false;
    
    screen.setCurrentGroup(group);
    
    return true;
  }

  @Override
  public void unExecute()
  {
    screen.setCurrentGroup(parent);
  }
}
