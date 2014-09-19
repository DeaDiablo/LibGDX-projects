package com.shellGDX.model2D.ui;

import java.util.Vector;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.shellGDX.model2D.Group2D;
import com.shellGDX.model2D.ModelObject2D;

public class TextList extends Group2D
{
  protected class TextLine
  {
    public TextLine(String text, Color color)
    {
      this.text = text;
      this.color = color;
    }

    public String text;
    public Color  color;
  }

  protected MultilineText    text       = null;
  protected Vector<TextLine> lines      = new Vector<TextLine>();
  protected float            widthList  = 0.0f;
  protected float            heightList = 0.0f;

  protected int              countLine  = 0;
  protected int              beginLine  = 0;
  protected int              endLine    = 0;

  public boolean             inverseAdd = false;

  public TextList(TextureRegion region, float x, float y, MultilineText text, float widthList, float heightList)
  {
    super(x, y);
    this.text = text;
    this.widthList = widthList;
    this.heightList = heightList;
    
    addActor(new ModelObject2D(region));
    addActor(text);

    countLine = (int) (heightList / (text.font.bitmapFont.getLineHeight() * text.getScaleX())) + 1;
    parseText(lines, text.content, text.getColor());
    goToEnd();
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
  
  public void setText(String newText)
  {
    lines.clear();
    text.content = newText;
    parseText(lines, text.content, text.getColor());
    goToEnd();
  }
 
  public void addLine(String text)
  {
    addLine(text, this.text.getColor());
  }

  public void addLine(String text, Color color)
  {
    parseText(lines, text, color);
    goToEnd();
  }

  public void clear()
  {
    lines.clear();
  }

  protected String bufferText;

  protected void goToEnd()
  {
    if (!inverseAdd)
    {
      endLine = lines.size();
      beginLine = endLine - countLine;
      if (beginLine < 0) 
        beginLine = 0;
      return;
    }

    beginLine = 0;
    endLine = Math.min(lines.size(), beginLine + countLine);
  }

  protected void parseText(Vector<TextLine> lines, String text, Color color)
  {
    if (text.isEmpty())
      return;
    
    String[] linesText = text.split("\n");
    Vector<TextLine> newLines = new Vector<TextLine>();
    
    for(String line : linesText)
    {
      float textWidth = this.text.getTextBound(line).width;
      do
      {
        if (textWidth < widthList)
        {
          newLines.add(new TextLine(line + "\n", color));
          break;
        }
  
        int divIndex = 0;
        while (divIndex < line.length() &&
               this.text.getTextBound(line.substring(0, divIndex)).width < widthList)
        {
          divIndex++;
        }
  
        divIndex--;
        if (divIndex < 0)
          break;
  
        int indexSpace = line.substring(0, divIndex).lastIndexOf(" ");
        if (indexSpace >= 0)
          divIndex = indexSpace;
  
        newLines.add(new TextLine(line.substring(0, divIndex) + "\n", color));
        line = line.substring(divIndex + 1);
        textWidth = this.text.getTextBound(line).width;
      } while (textWidth > 0);
    }
    
    lines.clear();
    if (!inverseAdd)
      lines.addAll(newLines);
    else
      lines.addAll(0, newLines);
  }
  
  protected void setText(int beginLine, int endLine)
  {
    text.content = "";
    for (int i = beginLine; i < endLine; i++)
    {
      TextLine line = lines.get(i);
      if (line != null)
        text.content += line.text;
    }
  }

  @Override
  public void draw(Batch batch, float parentAlpha)
  {
    if (!isVisible())
      return;

    if (text != null)
    {
      setText(beginLine, endLine);
    }

    super.draw(batch, parentAlpha);
  }

  protected float   dragPosY  = 0.0f;
  protected boolean startDrag = false;

  @Override
  public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
  {
    setFocus();
    if (lines.size() > countLine)
    {
      dragPosY = y;
      startDrag = true;
    }
    return true;
  }

  protected static final float speedDrag = 1.0f;

  @Override
  public void touchDragged(InputEvent event, float x, float y, int pointer)
  {
    if (!startDrag)
      return;

    int len = lines.size();
    int delta = (int) ((y - dragPosY) * speedDrag * 0.1f);
    dragPosY = y;
    if (delta != 0)
    {
      beginLine += delta;
      if (beginLine < 0)
        beginLine = 0;
      if (beginLine > len - countLine)
        beginLine = len - countLine;

      if (len < beginLine + countLine)
        endLine = len;
      else
        endLine = beginLine + countLine;
    }
  }

  @Override
  public void touchUp(InputEvent event, float x, float y, int pointer, int button)
  {
    startDrag = false;
  }

  public void setFocus()
  {
    FocusElement.setFocus(this);
  }

  @Override
  public boolean scrolled(InputEvent event, float x, float y, int amount)
  {
    int len = lines.size();

    if (len <= countLine)
      return false;

    beginLine += amount;
    if (beginLine < 0)
      beginLine = 0;
    if (beginLine > len - countLine)
      beginLine = len - countLine;

    if (len < beginLine + countLine)
      endLine = len;
    else
      endLine = beginLine + countLine;
    return true;
  }
}
