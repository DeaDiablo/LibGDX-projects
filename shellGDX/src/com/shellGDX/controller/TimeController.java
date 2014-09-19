package com.shellGDX.controller;

public class TimeController
{
  public static float globalTime = 0.0f;

  public static void setTime(float time)
  {
    globalTime = time;
  }

  public static void update(float deltaTime)
  {
    globalTime += deltaTime;
  }
}
