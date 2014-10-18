package com.games.leveleditor.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.shellGDX.model2D.Group2D;

public class EditGroup extends Group2D implements SelectObject
{
  protected BoundingBox bb = null;
  
  public EditGroup()
  {
    this("");
  }
  
  public EditGroup(String name)
  {
    this(name, 0, 0);
  }
  
  public EditGroup(String name, float x, float y)
  {
    super(x, y);
    setName(name);
    bb = new BoundingBox(getBound());
  }

  public EditGroup copy()
  {
    EditGroup newModel = new EditGroup(getName(), getX(), getY());
    newModel.setVisible(isVisible());
    newModel.setRotation(getRotation());
    newModel.setScale(getScaleX(), getScaleY());
    newModel.variables.putAll(variables);
    
    for(Actor actor : getChildren())
    {
      if (actor instanceof EditGroup)
      {
        newModel.addActor(((EditGroup)actor).copy());
      }
      else if (actor instanceof EditModel)
      {
        newModel.addActor(((EditModel)actor).copy());
      }
    }
    
    return newModel;
  }

  @Override
  public void load(Element element) throws IOException
  {
    setName(element.get("name"));
    setVisible(element.getBoolean("visible"));
    
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
    
    Element children = element.getChildByName("children");
    for(int i = 0; i < children.getChildCount(); i++)
    {
      Element child = children.getChild(i);
      if (child.getName().compareToIgnoreCase("model") == 0)
      {
        EditModel model = new EditModel();
        model.load(child);
        addActor(model);
        continue;
      }
      
      if (child.getName().compareToIgnoreCase("group") == 0)
      {
        EditGroup group = new EditGroup();
        group.load(child);
        addActor(group);
        continue;
      }
    }
  }

  @Override
  public void save(XmlWriter xml) throws IOException
  {
    xml.element("group");
    xml.element("name", getName());
    xml.element("visible", isVisible());

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
    
    if (!this.variables.isEmpty())
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
    
    xml.element("children");
    for(Actor model : getChildren())
    {      
      if (model instanceof SelectObject)
      {
        ((SelectObject)model).save(xml);
      }
    }
    xml.pop();
    
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
  
  protected HashMap<String, String> variables = new HashMap<String, String>();
  
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
