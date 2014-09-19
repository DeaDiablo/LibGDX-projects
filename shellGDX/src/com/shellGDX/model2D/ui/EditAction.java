package com.shellGDX.model2D.ui;

public abstract class EditAction
{
  public boolean enter = true;
  public boolean focus = false;
  public boolean set   = false;

  public abstract void execute(Edit edit);
}
