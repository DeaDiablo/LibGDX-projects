package com.games.leveleditor.model;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class PanelLayers extends PanelScroll
{
  public Tree        tree        = null;
  public TextButton  addButton    = null;
  public TextButton  removeButton = null;
  
  public PanelLayers(String title, Skin skin)
  {
    super(title, skin);
    content.align(Align.bottom);
    
    tree = new Tree(skin);
    content.add(tree).fill().expand();

    content.row();

    addButton = new TextButton("add", skin);
    removeButton = new TextButton("remove", skin);
    
    content.add(addButton);
    content.add(removeButton);
    
    setSize(450, 350);
  }
}
