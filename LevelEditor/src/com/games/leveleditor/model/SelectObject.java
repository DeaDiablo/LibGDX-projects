package com.games.leveleditor.model;

import java.io.IOException;
import java.util.HashMap;

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
  
  public void setVariable(String key, String value);
  public void removeVariable(String key);
  public void setNewKey(String oldKey, String newKey);
  public String getVariableValue(String key);
  public HashMap<String, String> getVariables();
}
