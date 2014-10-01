package com.games.leveleditor.controller;

import com.badlogic.gdx.utils.Array;

public enum CommandController
{
  instance;

  private final int stackSize = 100;
  private Array<Command> undoStack = new Array<Command>();
  private Array<Command> redoStack = new Array<Command>();
  
  public void addCommand(Command command)
  {
    addCommand(command, true);
  }
  
  public void addCommand(Command command, boolean needUpdate)
  {
    if (!command.execute())
      return;
    
    if (undoStack.size >= stackSize)
      undoStack.removeIndex(0);
    undoStack.add(command);
    redoStack.clear();

    if (needUpdate)
      command.update();
  }
  
  public void undo()
  {
    if (undoStack.size <= 0)
      return;
    
    Command command = undoStack.get(undoStack.size - 1);
    undoStack.removeValue(command, true);
    redoStack.add(command);
    command.unExecute();
    command.update();
  }
  
  public void redo()
  {
    if (redoStack.size <= 0)
      return;
    
    Command command = redoStack.get(redoStack.size - 1);
    redoStack.removeValue(command, true);
    undoStack.add(command);
    command.execute();
    command.update();
  }

  public void clear()
  {
    undoStack.clear();
    redoStack.clear();
  }
}
