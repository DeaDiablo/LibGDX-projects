package com.games.leveleditor.model;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class PanelMain extends Panel
{
  public TextButton  newButton  = null;
  public TextButton  openButton = null;
  public TextButton  saveButton = null;
  public TextButton  saveAsButton = null;
  
  public PanelMain(String title, Skin skin)
  {
    super(title, skin);
    setSize(1000, 1000);
    defaults().spaceBottom(10);
    defaults().space(10);
    
    //visible
    newButton = new TextButton("new", skin);
    openButton = new TextButton("open", skin);
    saveButton = new TextButton("save", skin);
    saveAsButton = new TextButton("save as...", skin);
    
    add(newButton);
    add(openButton);
    add(saveButton);
    add(saveAsButton);
    
    pack();
    setWidth(450);
  }
}
