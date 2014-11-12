package com.shellGDX.utils.leveleditor2d;

import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.shellGDX.model2D.Group2D;
import com.shellGDX.utils.gleed.LayerObject;

public class Layer extends Group2D
{
  private HashMap<String, Array<Actor>> objects = new HashMap<String, Array<Actor>>();
  
  public Layer()
  {
    super();
  }

  public HashMap<String, Array<Actor>> getObjects()
  {
    return objects;
  }
  
  public Array<Actor> getObjects(int x, int y)
  {
    return objects.get(String.format("%d %d", x, y));
  }
  
  public void addObject(LayerObject object)
  {
    String key = String.format("%d %d", (int)object.getX() / Settings.xGridSize, (int)object.getY() / Settings.yGridSize);
    Array<Actor> arrayObject = objects.get(key);
    if (arrayObject == null)
    {
      arrayObject = new Array<Actor>();
      objects.put(key, arrayObject);
    }
    arrayObject.add(object);
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
