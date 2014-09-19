package com.shellGDX.manager;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.shellGDX.GameInstance;
import com.shellGDX.GameLog;

public enum LanguagesManager
{
  instance;

  private static final String LANGUAGES_FILE = "data/langs.xml";

  private HashMap<String, String> language = new HashMap<String, String>();
  private String                  currrentLanguage = null;
  private String                  defaultLanguage = null;
    
  private LanguagesManager()
  {
    defaultLanguage = GameInstance.game.config.getString("language");
    String systemLanguage = java.util.Locale.getDefault().toString();

    setLanguage(systemLanguage);
  }

  public void setLanguage(String language)
  {
    currrentLanguage = language;
    if (!loadLanguage(language))
    {
      GameLog.instance.writeLog("Language not found: " + language);
      loadLanguage(defaultLanguage);
      currrentLanguage = defaultLanguage;
    }
    GameLog.instance.writeLog("Load language: " + currrentLanguage);
  }

  public String getLanguage()
  {
    return currrentLanguage;
  }

  public String getString(String key)
  {
    String string;

    if (language != null) {
      // Look for string in selected language
      string = language.get(key);

      if (string != null) {
        return string;
      }
    }

    // Key not found, return the key itself
    return key;
  }

  public boolean loadLanguage(String languageName)
  {
    try
    {
      XmlReader reader = new XmlReader();
      Element root = reader.parse(Gdx.files.internal(LANGUAGES_FILE).reader("UTF-8"));

      Array<Element> languages = root.getChildrenByName("language");

      for (int i = 0; i < languages.size; ++i)
      {
        Element language = languages.get(i);

        if (language.getAttribute("name").equals(languageName))
        {
          this.language.clear();
          Array<Element> strings = language.getChildrenByName("string");

          for (int j = 0; j < strings.size; ++j)
          {
            Element string = strings.get(j);
            String key = string.getAttribute("key");
            String value = string.getAttribute("value");
            value = value.replace("&lt;br /&gt;&lt;br /&gt;\\n;", "\n");
            this.language.put(key, value);
          }

          return true;
        }
      }
    }
    catch (Exception e)
    {
      Gdx.app.error("Error.", "Error loading languages file: " + LANGUAGES_FILE);
      return false;
    }

    return false;
  }
}