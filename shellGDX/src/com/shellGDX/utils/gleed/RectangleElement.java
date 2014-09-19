package com.shellGDX.utils.gleed;

import com.badlogic.gdx.math.Rectangle;

public class RectangleElement extends LayerObject
{
  Rectangle rectangle = new Rectangle();
  
  RectangleElement()
  {
    super();
  }
  
  public Rectangle getRectangle()
  {
    return rectangle;
  }
}
