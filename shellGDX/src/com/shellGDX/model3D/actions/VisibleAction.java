package com.shellGDX.model3D.actions;

import com.shellGDX.model3D.Action3D;

/**
 * Sets the model's {@link Model#setVisible(boolean) visibility}.
 * 
 * @author Nathan Sweet
 */
public class VisibleAction extends Action3D
{
  private boolean visible;

  @Override
  public boolean act(float delta)
  {
    model3D.setVisible(visible);
    return true;
  }

  public boolean isVisible()
  {
    return visible;
  }

  public void setVisible(boolean visible)
  {
    this.visible = visible;
  }
}