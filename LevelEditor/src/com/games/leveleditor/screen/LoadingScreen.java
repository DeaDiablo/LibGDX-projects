package com.games.leveleditor.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ObjectMap;
import com.games.leveleditor.LevelEditor;
import com.shellGDX.manager.FontManager;
import com.shellGDX.manager.FontStruct;
import com.shellGDX.manager.LanguagesManager;
import com.shellGDX.manager.ResourceManager;
import com.shellGDX.model2D.Scene2D;
import com.shellGDX.model2D.ui.Text;
import com.shellGDX.screen.GameScreen;

public class LoadingScreen extends GameScreen
{
  public LoadingScreen()
  {
    super();
  }

  @Override
  public void show()
  {
    FontStruct font = FontManager.instance.loadFont("data/fonts/calibriz.ttf", 50);
    FontStruct skinFont = FontManager.instance.loadFont("data/fonts/calibriz.ttf", 22);
    ResourceManager.instance.finishLoading();
    
    ResourceManager.instance.loadFolder("data");
    
    ObjectMap<String, Object> objects = new ObjectMap<String, Object>();
    objects.put("default-font", skinFont.bitmapFont);
    ResourceManager.instance.loadSkin("data/skin/uiskin.json", "data/skin/uiskin.atlas", objects);
    
    Scene2D scene = new Scene2D(1920.0f, 1080.0f);
    setClearColor(0.96f, 0.94f, 0.92f, 1);
    scene.addActor(new Text(LanguagesManager.instance.getString("Loading"), font, Color.BLACK, scene.getWidth() * 0.5f, scene.getHeight() * 0.5f));
    contoller.addScene2D(scene);
  }

  @Override
  public void render(float deltaTime)
  {
    super.render(deltaTime);

    if (ResourceManager.instance.update())
    {
      dispose();
      LevelEditor.game.setScreen(new MainScreen());
    }
  }
}
