package com.games.leveleditor.model;

import java.io.IOException;

import com.badlogic.gdx.scenes.scene2d.Actor;
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
}
