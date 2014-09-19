package com.shellGDX.manager;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public enum FontManager
{
  instance;

  private final boolean               needRussian = true;
  private String                      charaters   = "";
  private HashMap<String, FontStruct> fonts       = new HashMap<String, FontStruct>();
  
  private FontManager()
  {
    charaters = FreeTypeFontGenerator.DEFAULT_CHARS;
    if (needRussian)
      charaters += "ÉÖÓÊÅÍÃØÙÇÕÚÔÛÂÀÏĞÎËÄÆİß×ÑÌÈÒÜÁŞ¨éöóêåíãøùçõúôûâàïğîëäæıÿ÷ñìèòüáş¸";
  }

  public FontStruct loadFont(String fontFile, int size)
  {
    return loadFont(fontFile, size, "");
  }
  
  public FontStruct loadFont(String fontFile, int size, String specialCharacters)
  {
    try
    {
      FreeTypeFontGenerator generator = new FreeTypeFontGenerator(ResourceManager.instance.loadFile(fontFile));
      
      FontStruct font = new FontStruct();
      font.fontFile = fontFile;
      font.parameters.flip = false;
      font.parameters.size = size;
      font.parameters.characters = charaters + specialCharacters;
      font.bitmapFont = generator.generateFont(font.parameters);
      font.bitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

      generator.dispose();

      fonts.put(fontFile + String.valueOf(size), font);
      return font;

    } catch (Exception ex)
    {
      Gdx.app.log("Error", "Font not found: " + fontFile);
      return null;
    }
  }

  public FontStruct getLoadFont(String fontFile, int size)
  {
    FontStruct font = fonts.get(fontFile + String.valueOf(size));
    if (font != null)
      return font;
    return loadFont(fontFile, size);
  }

  public void clear()
  {
    fonts.clear();
  }
  
  public void reload()
  {
    for(FontStruct font : fonts.values())
    {
      FreeTypeFontGenerator generator = new FreeTypeFontGenerator(ResourceManager.instance.loadFile(font.fontFile));
      font.bitmapFont = generator.generateFont(font.parameters);
      generator.dispose();
    }
  }
}
