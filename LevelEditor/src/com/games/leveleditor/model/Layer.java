package com.games.leveleditor.model;

import java.io.IOException;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.shellGDX.model2D.Group2D;

public class Layer extends Group2D
{
  protected Group currentGroup = null;
  
  public Layer()
  {
    this("");
  }
  
  public Layer(String name)
  {
    super();
    setName(name);
    currentGroup = this;
  }

  public void load(Element element) throws IOException
  {
    setName(element.get("name"));
    setVisible(element.getBoolean("visible"));
    
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
  
  public void save(XmlWriter xml) throws IOException
  {
    xml.element("layer");
    xml.element("name", getName());
    xml.element("visible", isVisible());

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

  public Array<Actor> getSelectedModels()
  {
    Array<Actor> selectModels = new Array<Actor>();

    SnapshotArray<Actor> children = currentGroup.getChildren();
    Actor[] actors = children.begin();
    for (int i = 0, n = children.size; i < n; i++)
    {
      Actor model = actors[i];
      if (((SelectObject)model).isSelected())
        selectModels.add(model);
    }
    children.end();
    
    return selectModels;
  }
  
  @Override
  public void draw(Batch batch, float parentAlpha)
  {
    super.draw(batch, parentAlpha);

    SnapshotArray<Actor> children = currentGroup.getChildren();
    Actor[] actors = children.begin();
    for(int i = 0; i < actors.length; i++)
    {
      Actor model = actors[i];
      if (model instanceof SelectObject)
      {
        ((SelectObject)model).drawBound(batch, parentAlpha);
      }
    }
    children.end();
  }
  
  public Group getCurrentGroup()
  {
    return currentGroup;
  }

  public void setCurrentGroup(Group group)
  {
    currentGroup = group;
  }
}
