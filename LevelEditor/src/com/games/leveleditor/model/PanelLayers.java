package com.games.leveleditor.model;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class PanelLayers extends Panel
{
  public TextButton  addButton    = null;
  public TextButton  removeButton = null;
  
  public PanelLayers(String title, Skin skin)
  {
    super(title, skin);
    setSize(1000, 1000);
    defaults().spaceBottom(10);
    defaults().space(10);
    
    //visible
    addButton = new TextButton("add", skin);
    removeButton = new TextButton("remove", skin);
    add(addButton);
    add(removeButton);

    row();
    
    pack();
    setWidth(450);
  }
}
