package com.games.leveleditor.controller;

import com.badlogic.gdx.utils.Array;

public class GroupCommand extends Command
{
  private Array<Command> commands = new Array<Command>();
  private Array<Command> buffer = new Array<Command>();

  public void addCommand(Command command)
  {
    commands.add(command);
  }
  
  public Array<Command> getCommands()
  {
    return commands;
  }
  
  @Override
  public boolean execute()
  {
    buffer.addAll(commands);
    for(Command command : buffer)
    {
      if (!command.execute())
        commands.removeValue(command, true);
      command.update();
    }

    buffer.clear();
    return commands.size > 0;
  }

  @Override
  public void unExecute()
  {
    for(int i = commands.size - 1; i >= 0; --i)
    {
      Command command = commands.get(i);
      command.unExecute();
      command.update();
    }
  }
}
