package com.games.leveleditor.model;

import java.io.IOException;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;

public interface SelectObject
{
  public void setSelection(boolean select);  
  public boolean isSelected();
  
  public Rectangle getBound();
  public BoundingBox getBoundingBox();
  
  public void drawBound();
  public void load(Element element) throws IOException;
  public void save(XmlWriter xml) throws IOException;
}
