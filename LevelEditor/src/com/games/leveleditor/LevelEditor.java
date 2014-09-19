package com.games.leveleditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.games.leveleditor.screen.LoadingScreen;
import com.shellGDX.GameInstance;

public class LevelEditor extends GameInstance
{
  public static Preferences settings = null;

  public LevelEditor()
  {
    super();
  }

  @Override
  public void create()
  {    
    super.create();
    settings = Gdx.app.getPreferences(this.getClass().getName());
    if (settings.get().isEmpty())
    {
    }

    settings.putFloat("version", 1.00f);
    settings.flush();
    setScreen(new LoadingScreen());
  }
}
