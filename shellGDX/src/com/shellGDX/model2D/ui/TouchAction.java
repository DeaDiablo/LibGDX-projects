package com.shellGDX.model2D.ui;

public abstract class TouchAction
{
  public void touchDown(Button button) {};
  public void touchUp(Button button) {};
  public abstract void execute(Button button, boolean touch);
}
