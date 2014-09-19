package com.shellGDX.utils.gleed;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader.Element;

public class Properties
{
  HashMap<String, String>  strings   = new HashMap<String, String> ();
  HashMap<String, Color>   colors    = new HashMap<String, Color>();
  HashMap<String, Boolean> booleans  = new HashMap<String, Boolean>();
  HashMap<String, Vector2> vectors   = new HashMap<String, Vector2>();
  
  Properties()
  {
  }

  public String getString(String key)
  {
    return getString(key, "");
  }

  public String getString(String key, String defaultValue)
  {
    String value = strings.get(key);
    
    if (value == null)
    {
      return defaultValue;
    }
    
    return value;
  }

  public Boolean getBoolean(String key)
  {
    return getBoolean(key, false);
  }

  public Boolean getBoolean(String key, Boolean defaultValue)
  {
    Boolean value = booleans.get(key);
    
    if (value == null)
    {
      return defaultValue;
    }
    
    return value;
  }

  public Vector2 getVector(String key)
  {
    return getVector(key, Vector2.Zero);
  }

  public Vector2 getVector(String key, Vector2 defaultValue)
  {
    Vector2 value = vectors.get(key);
    
    if (value == null)
    {
      return defaultValue;
    }
    
    return value;
  }

  public Color getColor(String key)
  {
    return getColor(key, Color.BLACK);
  }

  public Color getColor(String key, Color defaultValue)
  {
    Color value = colors.get(key);
    
    if (value == null)
    {
      return defaultValue;
    }
    
    return value;
  }
  
  void load(Element element)
  {
    Element customProperty = element.getChildByName("CustomProperties");
     
    if (customProperty != null) 
    {
      Array<Element> properties = customProperty.getChildrenByName("Property");
      
      for (int i = 0; i < properties.size; ++i)
      {
        Element property = properties.get(i);
        String type = property.getAttribute("Type");
        
        if (type == null)
        {
          continue;
        }
        
        if (type.equals("string"))
        {
          strings.put(property.getAttribute("Name"), property.getChildByName("string").getText());
        }
        else if (type.equals("bool"))
        {
          booleans.put(property.getAttribute("Name"), Boolean.parseBoolean(property.getChildByName("boolean").getText()));
        }
        else if (type.equals("Vector2"))
        {
          Element vectorElement = property.getChildByName("Vector2");
          Vector2 v = new Vector2(Float.parseFloat(vectorElement.getChildByName("X").getText()),
                      Float.parseFloat(vectorElement.getChildByName("Y").getText()));
          
          vectors.put(property.getAttribute("Name"), v);
        }
        else if (type.equals("Color"))
        {
          Element colorElement = property.getChildByName("Color");
          Color c = new Color(Float.parseFloat(colorElement.getChildByName("R").getText()) / 255.0f,
                    Float.parseFloat(colorElement.getChildByName("G").getText()) / 255.0f,
                    Float.parseFloat(colorElement.getChildByName("B").getText()) / 255.0f,
                    Float.parseFloat(colorElement.getChildByName("A").getText()) / 255.0f);
          
          colors.put(property.getAttribute("Name"), c);
        }
      }
    }
  }
}
