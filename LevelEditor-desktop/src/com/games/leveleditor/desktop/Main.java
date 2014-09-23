package com.games.leveleditor.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.games.leveleditor.LevelEditor;

public class Main
{
  public static void main(String[] args)
  {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.title = java.util.Locale.getDefault().toString().compareTo("ru_RU") == 0 ? "Редактор уровней" : "Level editor";
    config.useGL30 = false;
    config.width = 1440;
    config.height = 810;
    config.fullscreen = false;
    config.vSyncEnabled = false;
    config.foregroundFPS = 0;
    new LwjglApplication(new LevelEditor(), config);
  }
}
