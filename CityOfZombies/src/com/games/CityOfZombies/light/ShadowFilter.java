package com.games.CityOfZombies.light;

import com.badlogic.gdx.physics.box2d.Filter;

public class ShadowFilter extends Filter
{
  public ShadowFilter()
  {
    super();
    categoryBits = 0x0003;
  }
}
