package com.games.leveleditor.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.shellGDX.manager.ResourceManager;
import com.shellGDX.model2D.ModelObject2D;

public class EditModel extends ModelObject2D implements SelectObject
{
  protected BoundingBox bb = null;
  
  public EditModel()
  {
    this("", null);
  }
  
  public EditModel(String name, TextureRegion region)
  {
    this(name, region, 0, 0);
  }
  
  public EditModel(String name, TextureRegion region, float x, float y)
  {
    super(region, x, y);
    setName(name);
    bb = new BoundingBox(getBound());
  }

  public EditModel copy()
  {
    EditModel newModel = new EditModel(getName(), new TextureRegion(region), getX(), getY());
    newModel.setVisible(isVisible());
    newModel.setRotation(getRotation());
    newModel.setScale(getScaleX(), getScaleY());
    newModel.setAlign(getHorzAlign(), getVertAlign());
    newModel.variables.putAll(variables);
    return newModel;
  }
  
  public String getPath()
  {
    return ((FileTextureData)region.getTexture().getTextureData()).getFileHandle().path();
  }

  public void setPath(String path)
  {
    try
    {
      setTextureRegion(ResourceManager.instance.getTextureRegion(path));
    }
    catch(GdxRuntimeException exception)
    {
    }
  }

  public float getU0()
  {
    return region.getU();
  }
  
  public float getV0()
  {
    return region.getV();
  }
  
  public float getU1()
  {
    return region.getU2();
  }
  
  public float getV1()
  {
    return region.getV2();
  }
  
  public void setRegion(float u0, float v0, float u1, float v1)
  {
    region.setRegion(u0, v0, u1, v1);
    setTextureRegion(region);
  }
  
  
  @Override
  public void load(Element element) throws IOException
  {
    setName(element.get("name"));
    setVisible(element.getBoolean("visible"));
    
    {
      Element texture = element.getChildByName("texture");
      String file = texture.get("file");
      float u0 = texture.getFloat("u0");
      float v0 = texture.getFloat("v0");
      float u1 = texture.getFloat("u1");
      float v1 = texture.getFloat("v1");
      setTextureRegion(ResourceManager.instance.getTextureRegion(file, u0, v0, u1, v1));
    }
    
    {
      Element position = element.getChildByName("position");
      setX(position.getFloat("x"));
      setY(position.getFloat("y"));
    }
    
    setRotation(element.getFloat("rotation"));

    {
      Element scale = element.getChildByName("scale");
      setScaleX(scale.getFloat("x"));
      setScaleY(scale.getFloat("y"));
    }
    
    {
      Element color = element.getChildByName("color");
      if (color != null)
      {
        getColor().r = color.getFloat("r");
        getColor().g = color.getFloat("g");
        getColor().b = color.getFloat("b");
        getColor().a = color.getFloat("a");
      }
    }
    
    {
      Element variablesGroup = element.getChildByName("variables");
      if (variablesGroup != null)
      {
        Array<Element> variables = variablesGroup.getChildrenByName("variable");
        for (Element variable : variables)
        {
          this.variables.put(variable.get("key"), variable.get("value"));
        }
      }
    }
    
    setAlign(element.getInt("horzAlign"), element.getInt("vertAlign"));
  }
  
  @Override
  public void save(XmlWriter xml) throws IOException
  {
    xml.element("model");
    xml.element("name", getName());
    xml.element("visible", isVisible());
    
    {
      xml.element("texture");
      xml.element("file", ((FileTextureData)region.getTexture().getTextureData()).getFileHandle().path());
      xml.element("u0", region.getU());
      xml.element("v0", region.getV());
      xml.element("u1", region.getU2());
      xml.element("v1", region.getV2());
      xml.pop();
    }
    
    {
      xml.element("position");
      xml.element("x", getX());
      xml.element("y", getY());
      xml.pop();
    }
    
    xml.element("rotation", getRotation());
    
    {
      xml.element("scale");
      xml.element("x", getScaleX());
      xml.element("y", getScaleY());
      xml.pop();
    }
    
    {
      Color color = getColor();
      xml.element("color");
      xml.element("r", color.r);
      xml.element("g", color.g);
      xml.element("b", color.b);
      xml.element("a", color.a);
      xml.pop();
    }
    
    xml.element("horzAlign", getHorzAlign());
    xml.element("vertAlign", getVertAlign());
    
    if (!variables.isEmpty())
    {
      xml.element("variables");
      for(Entry<String, String> variable : variables.entrySet())
      {
        xml.element("variable");
        xml.element("key", variable.getKey());
        xml.element("value", variable.getValue());
        xml.pop();
      }
      xml.pop();
    }
    xml.pop();
  }

  @Override
  public BoundingBox getBoundingBox()
  {
    return bb;
  }

  protected boolean select = false;
  
  @Override
  public void setSelection(boolean select)
  {
    this.select = select;
  }
  
  @Override
  public boolean isSelected()
  {
    return select;
  }

  @Override
  public void drawBound()
  {
    if (!select)
      return;
    
    bb.draw();
  }
  
  protected LinkedHashMap<String, String> variables = new LinkedHashMap<String, String>();
  
  @Override
  public void setVariable(String key, String value)
  {
    variables.put(key, value);
  }
  
  @Override
  public void removeVariable(String key)
  {
    variables.remove(key);
  }
  
  @Override
  public void setNewKey(String oldKey, String newKey)
  {
    String value = variables.get(oldKey);
    if (value == null)
      return;
    
    variables.remove(oldKey);
    variables.put(newKey, value);
  }
  
  @Override
  public String getVariableValue(String key)
  {
    return variables.get(key);
  }
  
  @Override
  public HashMap<String, String> getVariables()
  {
    return variables;
  }
}
