package com.shellGDX.model2D.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RadioButton extends Button
{

  protected RadioGroup group = null;
  private boolean      radio = false;

  public RadioButton(TextureRegion regionOff, TextureRegion regionOn, float x, float y)
  {
    super(regionOff, regionOn, x, y, null, null);
  }

  public RadioButton(TextureRegion regionOff, TextureRegion regionOn, float x, float y, TouchAction action)
  {
    super(regionOff, regionOn, x, y, null, action);
  }

  public RadioButton(TextureRegion regionOff, TextureRegion regionOn, float x, float y, Text text, TouchAction action)
  {
    super(regionOff, regionOn, x, y, text, action);
  }

  public RadioButton(TextureRegion regionOff, TextureRegion regionOn, float x, float y, Text text)
  {
    super(regionOff, regionOn, x, y, text, null);
  }

  @Override
  public boolean use()
  {
    if (radio == true)
      return true;

    radio = true;

    if (group != null)
      group.updateChecks(this);

    if (action != null)
      action.execute(this, radio);

    return true;
  }

  public boolean getCheck()
  {
    return radio;
  }

  public void setCheck(boolean check)
  {
    this.radio = check;
  }

  @Override
  public boolean checkButton()
  {
    return radio;
  }
}
