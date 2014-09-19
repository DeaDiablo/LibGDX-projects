package com.shellGDX.model2D.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.shellGDX.model2D.Group2D;
import com.shellGDX.model2D.ModelObject2D;

public class Slider extends Group2D
{
  public int              min            = 0;
  public int              max            = 100;
  public int              value          = 50;

  public float            widthScroller  = 20;
  public float            width          = 500;
  public float            height         = 20;

  protected ModelObject2D slider         = null;
  protected ModelObject2D sliderButton   = null;
  protected Vector2       sliderPosition = new Vector2();
  protected SliderAction  action         = null;
  
  public Slider(float x, float y, TextureRegion sliderRegion, float buttonX, float buttonY, TextureRegion sliderButtonRegion, SliderAction action)
  {
    super(x, y);
    this.action = action;

    slider = new ModelObject2D(sliderRegion);
    addActor(slider);
    
    sliderButton = new ModelObject2D(sliderButtonRegion, buttonX, buttonY);
    sliderPosition.set(buttonX, buttonY);
    addActor(sliderButton);
    
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

  public float getPercent(int value)
  {
    if (value <= min)
      return 0.0f;
    if (value >= max)
      return 1.0f;
    return (float) value / (max + 1 - min);
  }

  public int getValue(float percent)
  {
    if (percent <= 0.0f)
      return min;
    if (percent >= 1.0f)
      return max;
    return (int) (percent * (max + 1 - min));
  }

  @Override
  public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
  {
    FocusElement.setFocus(this);
    touchDragged(event, x, y, pointer);
    return true;
  }

  @Override
  public void touchDragged(InputEvent event, float x, float y, int pointer)
  {
    int oldValue = value;
    value = getValue((x + slider.getWidth() * 0.5f) / slider.getWidth());
    sliderButton.setPosition(sliderPosition);
    sliderButton.moveBy(getPercent(value) * slider.getWidth(), 0);
    if (oldValue != value && action != null)
      action.execute(this);
  }

  @Override
  public void touchUp(InputEvent event, float x, float y, int pointer, int button)
  {
    touchDragged(event, x, y, pointer);
    FocusElement.clearFocus();
  }
}
