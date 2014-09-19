package com.shellGDX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;

public enum GameLog
{
  instance;
  
  public boolean enableLog = true;
  public boolean logInFile = false;
  public String  fileErrorLog = "errors.log";
  public String  fileGameLog = "game.log";
  protected FPSLogger fpsLogger = new FPSLogger();
  
  public void writeError(String error)
  {
    if (enableLog)
      Gdx.app.error("Error!", error);
  }
  
  public void writeLog(String message)
  {
    if (enableLog)
      Gdx.app.log("Log \"" + GameInstance.game.getClass().getSimpleName().toString() + "\"", message);
  }
  
  public void writeFPS()
  {
    if (enableLog)
      fpsLogger.log();
  }
}
