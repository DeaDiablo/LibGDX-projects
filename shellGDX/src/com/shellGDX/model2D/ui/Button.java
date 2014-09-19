package com.shellGDX.model2D.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.shellGDX.model2D.Group2D;
import com.shellGDX.model2D.ModelObject2D;

public abstract class Button extends Group2D
{
  public    Text          text      = null;
  protected ModelObject2D graphics  = null;
  protected TextureRegion regionOn  = null;
  protected TextureRegion regionOff = null;
  protected TouchAction   action    = null;
  protected int           keyCode   = 0;

  public Button(TextureRegion regionOff, TextureRegion regionOn, float x, float y, Text text, TouchAction action)
  {
    super(x, y);
    this.regionOff = regionOff;
    this.regionOn = regionOn;
    this.text = text;
    this.action = action;
    
    graphics = new ModelObject2D(regionOff);
    addActor(graphics);
    
    if (this.text != null)
      addActor(this.text);
    
    setActive(true);
  }

  public void setActive(boolean active)
  {
    setTouchEnable(active);
  }
  
  public boolean getActive()
  {
    return getTouchEnable();
  }

  @Override
  public void setColor (Color color) {
    graphics.setColor(color);
  }

  @Override
  public void setColor (float r, float g, float b, float a) {
    graphics.setColor(r, g, b, a);
  }

  @Override
  public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
  {
    if (!isVisible())
      return false;
    if (action != null)
      action.touchDown(this);
    FocusElement.setFocus(this);
    return true;
  }
  
  @Override
  public void touchUp(InputEvent event, float x, float y, int pointer, int button)
  {
    if (!isVisible() || FocusElement.getFocus() != this)
      return;
    
    Vector2 point = new Vector2();
    point.set(x, y);
    point = localToStageCoordinates(point);

    if (!getBound().contains(point))
      return;

    if (action != null)
      action.touchUp(this);

    use();
    FocusElement.clearFocus();
  }
  
  @Override
  public boolean keyDown (InputEvent event, int keycode) {
    if (!isVisible() || keycode != keyCode)
      return false;
    FocusElement.setFocus(this);
    return true;
  }

  @Override
  public boolean keyUp (InputEvent event, int keycode) {
    if (!isVisible() || keycode != keyCode)
      return false;
    FocusElement.clearFocus();
    return use();
  }

  @Override
  public boolean keyTyped (InputEvent event, char character) {
    if (!isVisible())
      return false;
    return true;
  }

  @Override
  public boolean update(float deltaTime)
  {    
    if (checkButton() && regionOn != null)
      graphics.setTextureRegion(regionOn);
    else
      graphics.setTextureRegion(regionOff);

    return super.update(deltaTime);
  }

  public boolean checkButton()
  {
    return (FocusElement.getFocus() == this);
  }

  public abstract boolean use();
}
