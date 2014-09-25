package com.games.leveleditor.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class Panel extends Window
{
  public static final Color disableColor = new Color(0.5f, 0.5f, 0.5f, 1.0f);
  public static final Color enableColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
  
  public Panel(String title, Skin skin)
  {
    super(title, skin);
    defaults().spaceBottom(10);
    defaults().space(10);
  }
}
