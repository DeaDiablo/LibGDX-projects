package com.games.CityOfZombies.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.games.CityOfZombies.CityOfZombies;

public class Main
{
  public static void main(String[] args)
  {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.title = "City of zombies";
    config.useGL30 = false;
    config.width = 1440;
    config.height = 810;
    config.fullscreen = false;
    config.vSyncEnabled = true;
    //config.foregroundFPS = 0;
    //config.samples = 8;
    new LwjglApplication(new CityOfZombies(), config);
  }
}
