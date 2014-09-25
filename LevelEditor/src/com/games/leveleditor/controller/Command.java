package com.games.leveleditor.controller;

import com.badlogic.gdx.utils.Array;

public abstract class Command
{
  protected Array<Updater> updaters = new Array<Updater>();
  
  public void addUpdater(Updater updater)
  {
    updaters.add(updater);
  }
  
  public void update()
  {
    for(Updater updater : updaters)
      updater.update();
  }
  
  public abstract boolean execute();
  public abstract void unExecute();
}
