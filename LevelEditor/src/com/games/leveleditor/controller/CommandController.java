package com.games.leveleditor.controller;

import java.util.Vector;

public enum CommandController
{
  instance;

  private final int stackSize = 100;
  private Vector<Command> undoStack = new Vector<Command>();
  private Vector<Command> redoStack = new Vector<Command>();
  
  public void addCommand(Command command)
  {
    addCommand(command, true);
  }
  
  public void addCommand(Command command, boolean needUpdate)
  {
    if (!command.execute())
      return;
    
    if (undoStack.size() >= stackSize)
      undoStack.remove(0);
    undoStack.add(command);
    redoStack.clear();

    if (needUpdate)
      command.update();
  }
  
  public void undo()
  {
    if (undoStack.isEmpty())
      return;
    
    Command command = undoStack.get(undoStack.size() - 1);
    undoStack.remove(command);
    redoStack.add(command);
    command.unExecute();
    command.update();
  }
  
  public void redo()
  {
    if (redoStack.isEmpty())
      return;
    
    Command command = redoStack.get(redoStack.size() - 1);
    redoStack.remove(command);
    undoStack.add(command);
    command.execute();
    command.update();
  }
}
