package com.shellGDX.utils.leveleditor2d;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;

public class Layer extends LayerGroup
{
  public Layer()
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
