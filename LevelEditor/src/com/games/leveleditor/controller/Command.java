package com.games.leveleditor.controller;

public abstract class Command
{
  public abstract boolean execute();
  public abstract void unExecute();
}
