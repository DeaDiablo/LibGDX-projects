package com.shellGDX.utils.leveleditor2d;

import com.badlogic.gdx.utils.SnapshotArray;

public class Editor2DLevel
{
  public String levelName = "";
  public SnapshotArray<Layer> layers = new SnapshotArray<Layer>(true, 4, Layer.class);
  
  public void setName(String name)
  {
    levelName = name;
  }
  
  public String getName()
  {
    return levelName;
  }
}
