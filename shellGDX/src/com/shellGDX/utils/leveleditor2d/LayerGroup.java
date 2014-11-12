package com.shellGDX.utils.leveleditor2d;

import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.shellGDX.model2D.Group2D;

public class LayerGroup extends Group2D
{
  protected HashMap<String, String> variables = new HashMap<String, String>();
  
  public LayerGroup()
  {
    super();
  }

  public HashMap<String, String> getVariables()
  {
    return variables;
  }

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
        LayerModel model = new LayerModel();
        model.load(child);
        addActor(model);
        continue;
      }
      
      if (child.getName().compareToIgnoreCase("group") == 0)
      {
        LayerGroup group = new LayerGroup();
        group.load(child);
        addActor(group);
        continue;
      }
    }
  }
}
