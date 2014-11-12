package com.shellGDX.utils.leveleditor2d;

import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.shellGDX.manager.ResourceManager;
import com.shellGDX.model2D.ModelObject2D;

public class LayerModel extends ModelObject2D
{
  protected HashMap<String, String> variables = new HashMap<String, String>();
  
  public LayerModel()
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
      Element texture = element.getChildByName("texture");
      String file = texture.get("file");
      int x = texture.getInt("x");
      int y = texture.getInt("y");
      int width = texture.getInt("width");
      int height = texture.getInt("height");
      setTextureRegion(ResourceManager.instance.getTextureRegion(file, x, y, width, height));
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
}
