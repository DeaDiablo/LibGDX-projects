package com.shellGDX.utils.leveleditor2d;

import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader.Element;

public class LayerModel
{
  public String   name     = "";
  public boolean  visible  = true;
  
  public String   textureFile  = "";
  public float    u0 = 0.0f, v0 = 0.0f,
                  u1 = 1.0f, v1 = 1.0f;
  public Color    color    = new Color();
  
  public Vector2  position = new Vector2(0, 0);
  public float    angle    = 0.0f;
  public Vector2  scale    = new Vector2(1, 1);
  
  public int      hAlign = 0, vAlign = 0;
  
  public HashMap<String, String> variables = new HashMap<String, String>();
  
  public LayerModel()
  {
    super();
  }
  
  public LayerGroup asGroup()
  {
    return null;
  }
  
  public void load(Element element) throws IOException
  {
    name = element.get("name");
    visible = element.getBoolean("visible");
    
    Element texture = element.getChildByName("texture");
    if (texture != null)
    {
      this.textureFile = texture.get("file");
      this.u0 = texture.getFloat("u0");
      this.v0 = texture.getFloat("v0");
      this.u1 = texture.getFloat("u1");
      this.v1 = texture.getFloat("v1");
    }

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
    
    this.hAlign = element.getInt("horzAlign");
    this.vAlign = element.getInt("vertAlign");
  }
}
