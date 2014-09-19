package com.shellGDX;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.shellGDX.controller.MainController;
import com.shellGDX.manager.SoundManager;
import com.shellGDX.utils.GameAction;
import com.shellGDX.view.MainView;

public class GameInstance extends Game
{
  // game
  public static GameInstance   game = null;
  public static MainController contoller = null;
  public static MainView       view = null;

  public Preferences           config   = null;

  public GameInstance()
  {
  }

  @Override
  public void create()
  {
    config = Gdx.app.getPreferences("config");
    
    if (config.get().isEmpty())
    {
      config.putString("defaultLanguage", "en_US");
      config.putBoolean("soundOn", true);
      config.putBoolean("musicOn", true);
    }
    
    config.putFloat("version", 1.0f);
    config.flush();

    // logging java
    if (GameLog.instance.logInFile)
    {
      try
      {
        System.setErr(new PrintStream(new FileOutputStream(GameLog.instance.fileErrorLog)));
        System.setOut(new PrintStream(new FileOutputStream(GameLog.instance.fileGameLog)));
      }
      catch (FileNotFoundException e)
      {
      }
    }

    // game
    ShaderProgram.pedantic = false;
    game = this;
    contoller = new MainController();
    view = new MainView();
    Gdx.input.setInputProcessor(contoller);
    SoundManager.instance.setSoundOn(config.getBoolean("soundOn"));
    SoundManager.instance.setMusicOn(config.getBoolean("musicOn"));

    GameLog.instance.writeLog("Start game!");
  }

  @Override
  public void dispose()
  {
    GameLog.instance.writeLog("End game!");

    if (getScreen() != null)
      getScreen().dispose();
    view.dispose();
    Gdx.app.exit();
  }
  
  
  //creating actions for different platforms (ads, authorization, etc.)
  
  protected HashMap<String, GameAction> gameActions = new HashMap<String, GameAction>();
  
  public void addGameAction(String name, GameAction action)
  {
    gameActions.put(name, action);
  }
  
  public void removeGameAction(String name)
  {
    gameActions.remove(name);
  }
  
  public GameAction getGameAction(String name)
  {
    return gameActions.get(name);
  }
  
  public void activeGameAction(String name)
  {
    GameAction ga = gameActions.get(name);
    if (ga != null)
      ga.action();
  }
}
