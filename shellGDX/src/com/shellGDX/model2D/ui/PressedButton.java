package com.shellGDX.model2D.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PressedButton extends Button
{

  public PressedButton(TextureRegion regionOff, TextureRegion regionOn, float x, float y)
  {
    super(regionOff, regionOn, x, y, null, null);
  }

  public PressedButton(TextureRegion regionOff, TextureRegion regionOn, float x, float y, TouchAction action)
  {
    super(regionOff, regionOn, x, y, null, action);
  }

  public PressedButton(TextureRegion regionOff, TextureRegion regionOn, float x, float y, Text text, TouchAction action)
  {
    super(regionOff, regionOn, x, y, text, action);
  }

  public PressedButton(TextureRegion regionOff, TextureRegion regionOn, float x, float y, Text text)
  {
    super(regionOff, regionOn, x, y, text, null);
  }

  @Override
  public boolean use()
  {
    FocusElement.clearFocus();
    if (action != null)
      action.execute(this, true);

    return true;
  }
}
