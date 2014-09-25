package com.games.leveleditor.model;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class PanelMain extends Panel
{
  public TextButton  newButton  = null;
  public TextButton  openButton = null;
  public TextButton  saveButton = null;
  public TextButton  saveAsButton = null;
  public TextButton  exitButton = null;
  
  public PanelMain(String title, Skin skin)
  {
    super(title, skin);
    
    //visible
    newButton = new TextButton("new", skin);
    openButton = new TextButton("open", skin);
    saveButton = new TextButton("save", skin);
    saveAsButton = new TextButton("save as...", skin);
    exitButton = new TextButton("exit", skin);
    
    add(newButton);
    add(openButton);
    add(saveButton);
    add(saveAsButton);
    add(exitButton);

    setSize(450, 80);
  }
}
