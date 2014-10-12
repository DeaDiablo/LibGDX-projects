package com.games.leveleditor.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.games.leveleditor.controller.CommandController;

public class UndoRedoProcessor extends InputAdapter
{
  protected boolean shiftPress = false;
  protected boolean ctrlPress = false;
  protected MainScreen screen = null;
  
  public UndoRedoProcessor(MainScreen screen)
  {
    this.screen = screen;  
  }
  
  @Override
  public boolean keyDown(int keycode)
  {
    switch(keycode)
    {
      case Input.Keys.SHIFT_LEFT:
      case Input.Keys.SHIFT_RIGHT:
        shiftPress = true;
        break;
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
      case Input.Keys.N:
        if (ctrlPress)
        {
          screen.getGUIScene().setKeyboardFocus(null);
          screen.getMain().newFile("");
        }
        break;
      case Input.Keys.O:
        if (ctrlPress)
        {
          screen.getGUIScene().setKeyboardFocus(null);
          screen.getMain().openFile();
        }
        break;
      case Input.Keys.S:
        if (ctrlPress)
        {
          screen.getGUIScene().setKeyboardFocus(null);
          screen.getMain().saveFile(shiftPress);
        }
        break;
      case Input.Keys.Y:
        if (ctrlPress)
        {
          screen.getGUIScene().setKeyboardFocus(null);
          CommandController.instance.redo();
        }
        return true;
      case Input.Keys.Z:
        if (ctrlPress)
        {
          screen.getGUIScene().setKeyboardFocus(null);
          CommandController.instance.undo();
        }
        return true;
      case Input.Keys.CONTROL_LEFT:
      case Input.Keys.CONTROL_RIGHT:
        ctrlPress = false;
        break;
      case Input.Keys.SHIFT_LEFT:
      case Input.Keys.SHIFT_RIGHT:
        shiftPress = false;
        break;
    }
    return false;
  }
}
