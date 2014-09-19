package com.shellGDX.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.shellGDX.GameInstance;
import com.shellGDX.controller.MainController;
import com.shellGDX.manager.FontManager;
import com.shellGDX.manager.SoundManager;
import com.shellGDX.view.MainView;

public abstract class GameScreen implements Screen
{
  protected MainController contoller   = null;
  protected MainView       view        = null;
  protected Color          clearColor  = new Color(0, 0, 0, 1);

  public GameScreen()
  {
    contoller = GameInstance.contoller;
    view = GameInstance.view;
  }

  public Color getClearColor()
  {
    return clearColor;
  }

  public void setClearColor(Color color)
  {
    clearColor.set(color);
  }

  public void setClearColor(float r, float g, float b, float a)
  {
    clearColor.set(r, g, b, a);
  }

  public void update(float deltaTime)
  {
    contoller.update(deltaTime);
  }

  @Override
  public void render(float deltaTime)
  {
    Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    update(deltaTime);
    draw(deltaTime);
  }
  
  public void draw(float deltaTime)
  {
    view.draw2DScenes(contoller.getScenes2D());
  }

  @Override
  public void resize(int width, int height)
  {
    contoller.resize(width, height);
  }

  @Override
  public void hide()
  {
  }

  @Override
  public void pause()
  {
    SoundManager.instance.pauseAllAudio();
  }

  @Override
  public void resume()
  {
    SoundManager.instance.resumeAllAudio();
    if (Gdx.app.getType() == ApplicationType.Android)
      FontManager.instance.reload();
  }

  @Override
  public void dispose()
  {
    Gdx.input.setCatchBackKey(false);
    Gdx.input.setCatchMenuKey(false);
    contoller.clear();
    SoundManager.instance.stopAllAudio();
  }
}
