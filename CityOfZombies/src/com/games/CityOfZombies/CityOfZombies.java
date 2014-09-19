package com.games.CityOfZombies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.games.CityOfZombies.screen.LoadingScreen;
import com.shellGDX.GameInstance;

public class CityOfZombies extends GameInstance
{
  public static Preferences settings = null;

  public CityOfZombies()
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

    settings.putFloat("version", 1.0f);
    settings.flush();
    setScreen(new LoadingScreen());
  }
}
