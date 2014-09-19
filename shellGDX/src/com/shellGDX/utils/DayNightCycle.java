package com.shellGDX.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.shellGDX.box2dLight.LightWorld2D;

public class DayNightCycle
{
  private float currentTime = 0.0f;
  private int hours = 0;
  private int minutes = 0;
  private float hourDuration = 0.0f;
  
  public DayNightCycle(float startHour, float hourDuration, boolean clearWeather)
  {
    this.hourDuration = hourDuration;
    this.currentTime = (startHour % 24) * 60;
    hours = (int)currentTime / 60;
    minutes = (int)currentTime % 60;
    currentClearWeather = clearWeather;
    
    //init
    factorWeather = clearWeather ? 0.0f : -0.25f;
    updateColor(dayColor, hours);
    switchTime = 0.0f;
  }
  public int getHours()
  {
    return hours;
  }
  
  public int getMinutes()
  {
    return minutes;
  }
  
  public final Color getDayColor()
  {
    return dayColor;
  }
  
  private float switchTime = 0.0f;
  private float switchDuration = 0.0f;
  
  private boolean currentClearWeather = true;
  private float deltaWeather = 60.0f;
  private float factorWeather = 0.0f;
  final private Color dayColor = new Color();
  final private Color startColor = new Color();
  final private Color endColor = new Color();

  public void update(float deltaTime, boolean clearWeather)
  {
    currentTime += deltaTime / hourDuration;
    if (currentTime >= 1440) //minutes in day
      currentTime = 0;
    
    hours = (int)currentTime / 60;
    minutes = (int)currentTime % 60;
    
    switchTime -= deltaTime / hourDuration;
    if (switchTime <= 0.0f)
    {
      startColor.set(dayColor);
      updateColor(endColor, hours);
      if (!clearWeather)
        endColor.set(endColor.b, endColor.b, endColor.b, 1.0f);
    }

    //weather
    if (currentClearWeather != clearWeather)
    {
      deltaWeather = 0.0f;
      currentClearWeather = clearWeather;
    }

    deltaWeather += deltaTime / hourDuration;
    if (deltaWeather < 60.0f) //1 hour
    {
      factorWeather = -0.15f * deltaWeather / 60.0f;
      if (currentClearWeather)
        factorWeather = 0.15f - factorWeather;
    }

    dayColor.r = startColor.r + (endColor.r - startColor.r) * (switchDuration - switchTime) / switchDuration;
    dayColor.g = startColor.g + (endColor.g - startColor.g) * (switchDuration - switchTime) / switchDuration;
    dayColor.b = startColor.b + (endColor.b - startColor.b) * (switchDuration - switchTime) / switchDuration;

    if (LightWorld2D.instance != null)
      LightWorld2D.instance.setAmbientLight(dayColor.r + factorWeather, dayColor.g + factorWeather, dayColor.b + factorWeather, 1.0f);
  }
  
  protected void updateColor(Color color, float hours)
  {
    //night
    if (hours >= 22)
    {
      float rgb = MathUtils.random(0.06f);
      color.set(rgb, rgb, rgb, 1.0f);
      switchTime = 30 - hours;
    }
    else if (hours <= 5)
    {
      float rgb = MathUtils.random(0.06f);
      color.set(rgb, rgb, rgb, 1.0f);
      switchTime = 6 - hours;
    }
    //early morning
    else if (hours <= 7)
    {
      float r = MathUtils.random(0.2f, 0.5f);
      float g = MathUtils.random(0.2f, 0.4f);
      float b = MathUtils.random(0.2f, 0.3f);
      color.set(r, g, b, 1.0f);
      switchTime = 8 - hours;
    }
    //morning
    else if (hours <= 9)
    {
      float rgb = MathUtils.random(0.45f, 0.65f);
      color.set(rgb, rgb, rgb, 1.0f);
      switchTime = 10 - hours;
    }
    //day
    else if (hours <= 17)
    {
      float rgb = MathUtils.random(0.45f, 0.65f);
      color.set(rgb, rgb, rgb, 1.0f);
      switchTime = 18 - hours;
    }
    //early evening
    else if (hours <= 19)
    {
      float r = MathUtils.random(0.2f, 0.5f);
      float g = MathUtils.random(0.2f, 0.4f);
      float b = MathUtils.random(0.2f, 0.3f);
      color.set(r, g, b, 1.0f);
      switchTime = 20 - hours;
    }
    //evening
    else if (hours <= 21)
    {
      float rgb = MathUtils.random(0.06f);
      color.set(rgb, rgb, rgb, 1.0f);
      switchTime = 22 - hours;
    }
    
    switchTime *= 60.0f;
    switchDuration = switchTime;
  }
}
