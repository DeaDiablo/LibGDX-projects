package com.games.leveleditor.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class PanelScroll extends Panel
{
  public static final Color disableColor = new Color(0.5f, 0.5f, 0.5f, 1.0f);
  public static final Color enableColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
  
  protected Table content = null;
  protected ScrollPane scroll = null;
  
  public PanelScroll(String title, Skin skin)
  {
    super(title, skin);
    defaults().spaceLeft(0);
    defaults().spaceRight(0);
    defaults().spaceTop(0);
    defaults().spaceBottom(0);
    defaults().space(0);

    content = new Table(skin);
    content.defaults().spaceBottom(10);
    content.defaults().space(10);

    scroll = new ScrollPane(content, skin);
    add(scroll).expand().fill();
  }
}
