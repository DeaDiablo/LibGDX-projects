package com.shellGDX.manager;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class FontStruct
{
  public FontStruct()
  {
  }

  public String fontFile = "";
  public BitmapFont bitmapFont = null;
  public FreeTypeFontParameter parameters = new FreeTypeFontParameter();
}
