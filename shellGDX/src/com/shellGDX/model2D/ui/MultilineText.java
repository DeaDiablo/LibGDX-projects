package com.shellGDX.model2D.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.shellGDX.manager.FontStruct;

public class MultilineText extends Text
{
  public MultilineText(String text, FontStruct font, Color color, float x, float y)
  {
    super(text, font, color, x, y);
  }

  public MultilineText(String text, FontStruct font, Color color, float x, float y, float angle)
  {
    super(text, font, color, x, y, angle);
  }


  public MultilineText(String text, FontStruct font, Color color, float x, float y, float angle, float scaleX, float scaleY)
  {
    super(text, font, color, x, y, angle, scaleX, scaleY);
  }

  @Override
  public TextBounds getTextBound()
  {
    font.bitmapFont.setScale(getScaleX(), getScaleY());
    font.bitmapFont.getMultiLineBounds(content, textBound);
    return textBound;
  }
  
  @Override
  public TextBounds getTextBound(String text)
  {
    font.bitmapFont.setScale(getScaleX(), getScaleY());
    font.bitmapFont.getMultiLineBounds(text, textBound);
    return textBound;
  }

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
    font.bitmapFont.getMultiLineBounds(content, textBound);
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
    if (!isVisible() || content.isEmpty() || font == null || font.bitmapFont == null)
      return;

    if (bound.x > getStage().getWidth() ||
        bound.y > getStage().getHeight() ||
        bound.x + bound.width < 0 || 
        bound.y + bound.height < 0)
      return;

    Color color = getColor();
    font.bitmapFont.setColor(color.r, color.g, color.b, color.a * parentAlpha);
    
    getTextBound(content);
    calcOffset(textBound.width, textBound.height);

    transformMatrix.idt();
    transformMatrix.setToTranslation(getX(), getY() + getOffsetY(), 0);
    transformMatrix.mul(bufferMatrix.setToRotation(0, 0, 1, getRotation()));
    font.bitmapFont.setScale(getScaleX(), getScaleY());

    HAlignment horzAligment = HAlignment.LEFT;
    if (getHorzAlign() == Align.CENTER)
      horzAligment = HAlignment.CENTER;
    else if (getHorzAlign() == Align.RIGHT)
      horzAligment = HAlignment.RIGHT;

    bufferMatrix.set(batch.getTransformMatrix());
    batch.setTransformMatrix(batch.getTransformMatrix().mul(transformMatrix));
    font.bitmapFont.drawMultiLine(batch, content, 0, 0, 0, horzAligment);
    batch.setTransformMatrix(bufferMatrix);
  }
}