package com.shellGDX.model2D.ui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.shellGDX.GameInstance;
import com.shellGDX.model2D.Group2D;
import com.shellGDX.model2D.ModelObject2D;

public class Edit extends Group2D
{
  protected ShapeRenderer shape          = null;
  protected Text          text           = null;
  protected EditAction    action         = null;
  protected ModelObject2D graphics       = null;
  protected TextureRegion regionOn       = null;
  protected TextureRegion regionOff      = null;
  protected int           maxTextVisible = 0;
  protected int           beginVisible   = 0;
  protected int           endVisible     = 0;
  public boolean          readOnly       = false;

  public Edit(TextureRegion regionOff, TextureRegion regionOn, float x, float y, Text text, int maxTextVisible)
  {
    this(regionOff, regionOn, x, y, text, maxTextVisible, null);
  }

  public Edit(TextureRegion regionOff, TextureRegion regionOn, float x, float y, Text text, int maxTextVisible, EditAction action)
  {
    super(x, y);
    this.regionOff = regionOff;
    this.regionOn = regionOn;
    this.text = text;
    this.maxTextVisible = maxTextVisible;
    this.action = action;

    
    initText();
    shape = GameInstance.view.getShapeRenderer();
    graphics = new ModelObject2D(regionOff);
    addActor(graphics);
    
    setActive(true);
  }
  
  public void initText()
  {
    cursorPos = text.content.length();
    beginVisible = cursorPos - maxTextVisible;
    if (beginVisible < 0) 
      beginVisible = 0;
    endVisible = cursorPos;
  }

  public String getText()
  {
    return text.content;
  }
  
  public void setActive(boolean active)
  {
    setTouchEnable(active);
  }
  
  public boolean getActive()
  {
    return getTouchEnable();
  }

  public void setText(String newText)
  {
    changeText(newText);
    initText();
    if (action != null && action.set)
      action.execute(this);
  }

  protected void changeText(String newText)
  {
    text.content = newText;
  }

  public boolean    needUpFocus  = true;
  public Actor      nextControll = null;
  protected float   yBuffer      = 0.0f;
  protected int     cursorPos    = 0;
  protected boolean focus        = false;
  protected boolean oldFocus     = false;
  protected boolean showCursor   = false;
  protected float   updateTime   = 0.0f;
  protected float   cursorTime   = 0.0f;
  protected float   keyTime      = 0.0f;
  protected boolean startDrag    = false;
  protected float   dragPosX     = 0.0f;

  @Override
  public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
  {
    if (text.content.length() > maxTextVisible)
    {
      dragPosX = x;
      startDrag = true;
    }

    if (readOnly)
      return true;

    float minX = Float.MAX_VALUE;
    cursorPos = 0;
    for (int i = beginVisible; i <= endVisible; i++)
    {
      float posChar = Math.abs(text.getTextBound(beginVisible, i).width + text.getX() - text.getOffsetX() - x);
      if (minX > posChar)
      {
        cursorPos = i;
        minX = posChar;
      }
    }
    return true;
  }

  protected static final float speedDrag = 2.0f;

  @Override
  public void touchDragged(InputEvent event, float x, float y, int pointer)
  {
    if (!startDrag)
      return;

    int len = text.content.length();
    int delta = (int) ((dragPosX - x) * speedDrag * 0.1f);
    dragPosX = x;
    if (delta != 0)
    {
      beginVisible += delta;
      if (beginVisible < 0)
        beginVisible = 0;
      if (beginVisible > len - maxTextVisible)
        beginVisible = len - maxTextVisible;

      if (len < beginVisible + maxTextVisible)
        endVisible = len;
      else
        endVisible = beginVisible + maxTextVisible;

      if (cursorPos < beginVisible)
        cursorPos = beginVisible;
      else if (cursorPos > endVisible)
        cursorPos = endVisible;
    }
  }

  @Override
  public void touchUp(InputEvent event, float x, float y, int pointer, int button)
  {
    startDrag = false;
    FocusElement.setFocus(this);
    if (readOnly)
      return;
    cursorTime = updateTime;
    showCursor = true;
    return;
  }

  @Override
  public boolean update(float deltaTime)
  {
    if (!super.update(deltaTime))
      return false;

    if (readOnly)
      return true;

    updateTime += deltaTime;

    if (FocusElement.getFocus() == this)
      focus = true;
    else
      focus = false;

    if (focus)
    {
      if (regionOn != null)
        graphics.setTextureRegion(regionOn);
      else
        graphics.setTextureRegion(regionOff);

      if (updateTime - cursorTime > 0.5f)
      {
        cursorTime = updateTime;
        showCursor = !showCursor;
      }

      if (keyTime < updateTime)
      {
        switch (key)
        {
          case backspaceKey:
            removeLeftChar();
            break;

          case deleteKey:
            removeRightChar();
            break;

          case leftKey:
            nextLeftPosition();
            break;

          case rightKey:
            nextRightPosition();
            break;

          default:
            break;
        }

        keyTime = updateTime + 0.1f;
      }
    }

    if (focus != oldFocus)
    {
      oldFocus = focus;
      if (focus)
      {
        if (Gdx.app.getType() != Application.ApplicationType.Desktop)
          Gdx.input.setOnscreenKeyboardVisible(true);
      }
      else
      {
        if (Gdx.app.getType() != Application.ApplicationType.Desktop)
        {
          Gdx.input.setOnscreenKeyboardVisible(false);
        }

        if (action != null && action.focus)
          action.execute(this);
      }
    }
    return true;
  }

  @Override
  public void draw(Batch batch, float parentAlpha)
  {
    if (!isVisible())
      return;

    if (showCursor)
    {
      batch.end();
      shape.begin(ShapeType.Filled);
      shape.setProjectionMatrix(batch.getProjectionMatrix());
      shape.setTransformMatrix(batch.getTransformMatrix());
      float widthCursor = text.font.bitmapFont.getSpaceWidth() * 0.2f;
      float heightCursor = text.font.bitmapFont.getCapHeight();
      shape.setColor(text.getColor());
      shape.rect(getX() + text.getX() + text.getTextBound(beginVisible, cursorPos).width + text.getOriginX() - widthCursor * 0.5f,
                 getY() + text.getY() - heightCursor * 0.5f,
                 widthCursor,
                 heightCursor);
      shape.end();
      batch.begin();
    }

    if (text != null)
    {
      updateTextLength();
      text.setVisible(beginVisible, endVisible);
    }
    
    super.draw(batch, parentAlpha);
  }

  @Override
  public boolean keyTyped(InputEvent event, char character)
  {
    if (!focus)
      return false;
    if (readOnly || character == '\n' || character == '\r')
      return true;
    changeText(text.content.substring(0, cursorPos) + character + text.content.substring(cursorPos));
    cursorPos++;
    return true;
  }

  protected void updateTextLength()
  {
    int len = text.content.length();
    if (len > maxTextVisible)
    {
      if (endVisible > len)
      {
        beginVisible = beginVisible - (endVisible - len);
        endVisible = len;
        return;
      }
      if (cursorPos > endVisible)
      {
        beginVisible = beginVisible + (cursorPos - endVisible);
        endVisible = beginVisible + maxTextVisible;
        return;
      }
      if (cursorPos <= beginVisible)
      {
        endVisible = endVisible - (beginVisible - cursorPos);
        beginVisible = endVisible - maxTextVisible;
        return;
      }
    }
    else
    {
      beginVisible = 0;
      endVisible = len;
      return;
    }
  }

  private static final int backspaceKey = 1;
  private static final int deleteKey    = 2;
  private static final int leftKey      = 4;
  private static final int rightKey     = 8;
  protected int            key          = 0;

  @Override
  public boolean keyDown(InputEvent event, int keycode)
  {
    if (!focus)
      return false;
    if (readOnly || key != 0)
      return true;

    switch (keycode)
    {
      case Input.Keys.BACKSPACE:
        removeLeftChar();
        key = backspaceKey;
        break;

      case Input.Keys.FORWARD_DEL:
        removeRightChar();
        key = deleteKey;
        break;

      case Input.Keys.LEFT:
        nextLeftPosition();
        key = leftKey;
        break;

      case Input.Keys.RIGHT:
        nextRightPosition();
        key = rightKey;
        break;

      case Input.Keys.END:
        cursorPos = text.content.length();
        break;

      case Input.Keys.HOME:
        cursorPos = 0;
        break;

      default:
        break;
    }
    if (key != 0)
      keyTime = updateTime + 0.5f;

    return true;
  }

  @Override
  public boolean keyUp(InputEvent event, int keycode)
  {
    if (!focus)
      return false;
    switch (keycode)
    {
      case Input.Keys.ENTER:
        FocusElement.clearFocus();
        if (action != null && action.enter)
          action.execute(this);
        break;
      case Input.Keys.TAB:
        FocusElement.setFocus(nextControll);
        break;

      default:
        break;
    }
    key = 0;
    return true;
  }

  protected void removeLeftChar()
  {
    if (cursorPos > 0)
    {
      changeText(text.content.substring(0, cursorPos - 1) + text.content.substring(cursorPos));
      cursorPos--;
    }
  }

  protected void removeRightChar()
  {
    if (cursorPos != text.content.length())
      changeText(text.content.substring(0, cursorPos) + text.content.substring(cursorPos + 1));
  }

  protected void nextLeftPosition()
  {
    if (cursorPos > 0)
      cursorPos--;
  }

  protected void nextRightPosition()
  {
    if (cursorPos < text.content.length())
      cursorPos++;
  }

  public void setFocus()
  {
    FocusElement.setFocus(this);
  }
}
