package com.games.leveleditor.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.games.leveleditor.controller.CommandController;

public class UndoRedoProcessor extends InputAdapter
{
  protected boolean ctrlPress = false;
  
  @Override
  public boolean keyDown(int keycode)
  {
    switch(keycode)
    {
      case Input.Keys.CONTROL_LEFT:
      case Input.Keys.CONTROL_RIGHT:
        ctrlPress = true;
        break;
    }
    return false;
  }
  
  @Override
  public boolean keyUp(int keycode)
  {
    switch(keycode)
    {
      case Input.Keys.Y:
        if (ctrlPress)
          CommandController.instance.redo();
        return true;
      case Input.Keys.Z:
        if (ctrlPress)
          CommandController.instance.undo();
        return true;
    }
    return false;
  }
}
