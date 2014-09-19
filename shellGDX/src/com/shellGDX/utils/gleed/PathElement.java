package com.shellGDX.utils.gleed;

import com.badlogic.gdx.math.Polygon;

public class PathElement extends LayerObject
{
  Polygon polygon;
  int lineWidth;
  
  PathElement()
  {
    super();
  }

  public Polygon getPolygon()
  {
    return polygon;
  }

  public int getLineWidth()
  {
    return lineWidth;
  }
}
