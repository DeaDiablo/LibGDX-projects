package com.shellGDX.model2D.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.math.Matrix4;
import com.shellGDX.manager.FontStruct;
import com.shellGDX.model2D.ModelObject2D;

public class Text extends ModelObject2D
{
  public String     content = null;
  public FontStruct font    = null;

  public Text(String text, FontStruct font, Color color, float x, float y)
  {
    this(text, font, color, x, y, 0, 1, 1);
  }
  
  public Text(String text, FontStruct font, Color color, float x, float y, float angle)
  {
    this(text, font, color, x, y, angle, 1, 1);
  }
  
  public Text(String text, FontStruct font, Color color, float x, float y, float angle, float scaleX, float scaleY)
  {
    super(x, y, angle, scaleX, scaleY);
    this.content = text;
    this.font = font;
    setColor(color);
  }

  protected Matrix4    transformMatrix = new Matrix4();
  protected Matrix4    bufferMatrix    = new Matrix4();
  protected TextBounds textBound       = new TextBounds();
  protected int        beginText       = -1;
  protected int        endText         = -1;

  public TextBounds getTextBound(String text)
  {
    font.bitmapFont.setScale(getScaleX(), getScaleY());
    font.bitmapFont.getBounds(text, textBound);
    return textBound;
  }

  public TextBounds getTextBound()
  {
    font.bitmapFont.setScale(getScaleX(), getScaleY());
    font.bitmapFont.getBounds(content, textBound);
    return textBound;
  }

  public TextBounds getTextBound(int startChar, int finishChar)
  {
    font.bitmapFont.setScale(getScaleX(), getScaleY());
    font.bitmapFont.getBounds(content, startChar, finishChar, textBound);
    return textBound;
  }
  
  public float getWidth () {
    return getTextBound().width;
  }

  public float getHeight () {
    return getTextBound().height;
  }
  
  public void setVisible(int begin, int end)
  {
    beginText = begin;
    endText = end;
  }
  
  @Override
  protected void calcOffset(float width, float height)
  {
    switch (getHorzAlign())
    {
      case Align.LEFT:
        offset.x = 0.0f;
        break;
      case Align.RIGHT:
        offset.x = -width;
        break;
      default:
        offset.x = -width * 0.5f;
        break;
    }

    switch (vAlign)
    {
      case Align.TOP:
        offset.y = 0.0f;
        break;
      case Align.BOTTOM:
        offset.y = height;
        break;
      default:
        offset.y = height * 0.5f;
        break;
    }
  }
  
  protected String drawText = null;
  
  @Override
  public void updateBound()
  {
    if (content.isEmpty())
    {
      bound.set(0, 0, 0, 0);
      return;
    }

    bound.set(0, 0, 0, 0);

    TextBounds textBound = new TextBounds();
    font.bitmapFont.setScale(1, 1);
    font.bitmapFont.getBounds(content, textBound);
    calcOffset(textBound.width, textBound.height);
    
    bufferVec.set(offset.x, offset.y);
    bufferVec = localToStageCoordinates(bufferVec);
    bound.set(bufferVec.x, bufferVec.y, 0, 0);

    bufferVec.set(offset.x + textBound.width, offset.y);
    bufferVec = localToStageCoordinates(bufferVec);
    bound.merge(bufferVec);
    
    bufferVec.set(offset.x, offset.y + textBound.height);
    bufferVec = localToStageCoordinates(bufferVec);
    bound.merge(bufferVec);
    
    bufferVec.set(offset.x + textBound.width, offset.y + textBound.height);
    bufferVec = localToStageCoordinates(bufferVec);
    bound.merge(bufferVec);
  }

  @Override
  public void draw(Batch batch, float parentAlpha)
  {
    if (!isVisible() || content == null || content.length() == 0 || font == null || font.bitmapFont == null)
      return;
    
    if (bound.x > getStage().getWidth() ||
        bound.y > getStage().getHeight() ||
        bound.x + bound.width < 0 || 
        bound.y + bound.height < 0)
      return;
    
    if (beginText > 0 && endText > beginText)
      drawText = content.substring(beginText, endText);
    else
      drawText = content;
    
    Color color = getColor();
    font.bitmapFont.setColor(color.r, color.g, color.b, color.a * parentAlpha);
    font.bitmapFont.setScale(getScaleX(), getScaleY());
    font.bitmapFont.getBounds(drawText, textBound);
    calcOffset(textBound.width, textBound.height);
    
    transformMatrix.idt();
    transformMatrix.setToTranslation(getX() + getOffsetX(), getY() + getOffsetY(), 0);
    transformMatrix.mul(bufferMatrix.setToRotation(0, 0, 1, getRotation()));
    
    bufferMatrix.set(batch.getTransformMatrix());
    batch.setTransformMatrix(batch.getTransformMatrix().mul(transformMatrix));
    font.bitmapFont.draw(batch, drawText, 0, 0);
    batch.setTransformMatrix(bufferMatrix);
  }
}