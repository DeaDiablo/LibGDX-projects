package com.games.CityOfZombies.screen;

import com.shellGDX.GameInstance;
import com.shellGDX.manager.ResourceManager;
import com.shellGDX.model3D.light.LightWorld3D;
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
    ResourceManager.instance.loadFolder("data");
    ResourceManager.instance.loadGleed2DMap("data/testLevel1.xml");
    LightWorld3D.instance.init();
  }

  @Override
  public void render(float deltaTime)
  {
    super.render(deltaTime);

    if (ResourceManager.instance.update())
    {
      dispose();
      GameInstance.game.setScreen(new CityScreen(1920.0f, 1080.0f));
    }
  }
}
