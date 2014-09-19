package com.games.CityOfZombies.light;

import com.badlogic.gdx.physics.box2d.Filter;

public class LightFilter extends Filter
{
  public LightFilter()
  {
    super();
    categoryBits = 0x0002;
    maskBits = 0x0002;
  }
}
