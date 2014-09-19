package com.shellGDX.model2D.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class FocusElement
{
  private static Actor focusElement = null;

  public static Actor getFocus()
  {
    return focusElement;
  }

  public static void setFocus(Actor element)
  {
    focusElement = element;
  }

  public static void clearFocus()
  {
    focusElement = null;
  }
}
