package com.games.leveleditor.controller;

import java.util.Vector;

public class GroupCommand extends Command
{
  private Vector<Command> commands = new Vector<Command>();
  private Vector<Command> buffer = new Vector<Command>();

  public void addCommand(Command command)
  {
    commands.add(command);
  }
  
  @Override
  public boolean execute()
  {
    buffer.addAll(commands);
    for(Command command : buffer)
    {
      if (!command.execute())
        commands.remove(command);
      command.update();
    }

    buffer.clear();
    return !commands.isEmpty();
  }

  @Override
  public void unExecute()
  {
    for(Command command : commands)
    {
      command.unExecute();
      command.update();
    }
  }
}
