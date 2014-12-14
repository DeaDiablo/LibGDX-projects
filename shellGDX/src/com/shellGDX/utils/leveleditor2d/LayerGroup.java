package com.shellGDX.utils.leveleditor2d;

import java.io.IOException;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.XmlReader.Element;

public class LayerGroup extends LayerModel
{  
  public final SnapshotArray<LayerModel> children = new SnapshotArray<LayerModel>(true, 4, LayerModel.class);
  
  public LayerGroup()
  {
    super();
  }
  
  @Override
  public LayerGroup asGroup()
  {
    return this;
  }

  @Override
  public void load(Element element) throws IOException
  {
    name = element.get("name");
    visible = element.getBoolean("visible");
    
    Element position = element.getChildByName("position");
    if (position != null)
    {
      this.position.x = position.getFloat("x");
      this.position.y = position.getFloat("y");
    }
    
    this.angle = element.getFloat("rotation");

    Element scale = element.getChildByName("scale");
    if (scale != null)
    {
      this.scale.x = scale.getFloat("x");
      this.scale.y = scale.getFloat("y");
    }
    
    Element color = element.getChildByName("color");
    if (color != null)
    {
      this.color.r = color.getFloat("r");
      this.color.g = color.getFloat("g");
      this.color.b = color.getFloat("b");
      this.color.a = color.getFloat("a");
    }
    
    Element variablesGroup = element.getChildByName("variables");
    if (variablesGroup != null)
    {
      Array<Element> variables = variablesGroup.getChildrenByName("variable");
      for (Element variable : variables)
      {
        this.variables.put(variable.get("key"), variable.get("value"));
      }
    }
    
    Element children = element.getChildByName("children");
    if (children != null)
    {
      for(int i = 0; i < children.getChildCount(); i++)
      {
        Element child = children.getChild(i);
        if (child.getName().compareToIgnoreCase("model") == 0)
        {
          LayerModel model = new LayerModel();
          model.load(child);
          this.children.add(model);
          continue;
        }
        
        if (child.getName().compareToIgnoreCase("group") == 0)
        {
          LayerGroup group = new LayerGroup();
          group.load(child);
          this.children.add(group);
          continue;
        }
      }
    }
  }
}
