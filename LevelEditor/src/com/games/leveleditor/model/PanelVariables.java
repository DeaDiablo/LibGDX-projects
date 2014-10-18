package com.games.leveleditor.model;

import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.shellGDX.manager.ResourceManager;

public class PanelVariables extends PanelScroll
{
  public    Table       table     = null;
  protected Skin        skin      = null;
  protected TextField   editKey  = null;
  protected TextField   editValue = null;
  protected ImageButton addButton = null;

  protected TextureRegionDrawable plusDrawable = new TextureRegionDrawable(ResourceManager.instance.getTextureRegion("data/editor/plus.png"));
  protected TextureRegionDrawable minusDrawable = new TextureRegionDrawable(ResourceManager.instance.getTextureRegion("data/editor/minus.png"));
  
  protected InputListener tabListener = new InputListener()
  {
    @Override
    public boolean keyUp(InputEvent event, int keycode)
    {
      if (event.getCharacter() == '\t')
      {
        Actor actor = event.getListenerActor();
        if (actor instanceof TextField)
          ((TextField)actor).selectAll();
      }
      return true;
    }
  };
  
  public PanelVariables(String title, Skin skin)
  {
    super(title, skin);
    this.skin = skin;

    scroll.getListeners().removeIndex(0);
    
    table = new Table(skin);
    table.align(Align.top);
    table.defaults().spaceBottom(5);
    table.defaults().space(10);
    content.add(table).fill().expand();
    
    Table buttons = new Table(skin);
    buttons.defaults().spaceBottom(5);
    buttons.defaults().space(10);
    
    row();
    add(buttons);
    
    final ButtonStyle styleButton = skin.get(ButtonStyle.class);

    ImageButtonStyle style = new ImageButtonStyle(styleButton.up, styleButton.down, null,
                                                  plusDrawable, plusDrawable, null);
    
    addButton = new ImageButton(style);
    addButton.addListener(new ClickListener()
    {
      @Override
      public void clicked(InputEvent event, float x, float y)
      {
        addNewVariable();
      }
    });
    
    editKey = new TextField("", skin);
    editKey.setTextFieldListener(new TextFieldListener()
    {
      @Override
      public void keyTyped(TextField textField, char c)
      {
        if (c == '\r' || c == '\n')
        {
          getStage().setKeyboardFocus(null);
          return;
        }
      }
    });
    editKey.addListener(tabListener);
    
    editValue = new TextField("", skin);
    editValue.setTextFieldListener(new TextFieldListener()
    {
      @Override
      public void keyTyped(TextField textField, char c)
      {
        if (c == '\r' || c == '\n')
        {
          addNewVariable();
          return;
        }
      }
    });
    editValue.addListener(tabListener);

    buttons.add(editKey);
    buttons.add(editValue);
    buttons.add(addButton).size(32, 32);
    
    updateTable();
    
    setSize(420, 350);
  }
  
  protected Array<Actor> editActors = null;

  public void setEditActors(Array<Actor> models)
  {    
    if (models == null || models.size <= 0)
      editActors = null;
    else
      editActors = models;
    updateTable();
  }
  
  public void updateTable()
  {
    table.clear();
    
    boolean active = (editActors != null);

    editKey.setText("");
    editValue.setText("");
    
    Color color = active ? enableColor : disableColor;
    
    editKey.setColor(color);
    editValue.setColor(color);
    addButton.setColor(color);
    
    editKey.setDisabled(!active);
    editValue.setDisabled(!active);
    addButton.setDisabled(!active);
    
    if (!active)
      return;

    HashMap<String, String> variables = ((SelectObject)editActors.get(editActors.size - 1)).getVariables();
    
    if (editActors.size <= 1)
    {
      for(Entry<String, String> variable : variables.entrySet())
      {
        addVariable(variable.getKey(), variable.getValue());
      }
    }
    else
    {
      for(Entry<String, String> variable : variables.entrySet())
      {
        if (checkKey(variable.getKey()))
          addVariable(variable.getKey(), variable.getValue());
      }
    }
  }
  
  protected boolean checkKey(String key)
  {
    for(int i = 0; i < editActors.size; i++)
    {
      if (((SelectObject)editActors.get(i)).getVariableValue(key) == null)
        return false;
    }
    return true;
  }
  
  protected void addVariable(final String key, final String value)
  {    
    final TextField editKey = new TextField(key, skin);
    editKey.setTextFieldListener(new TextFieldListener()
    {
      protected String oldKey = key; 
      
      @Override
      public void keyTyped(TextField textField, char c)
      {
        if (c == '\r' || c == '\n')
        {
          getStage().setKeyboardFocus(null);
          return;
        }

        if (editActors == null)
          return;

        String newKey = textField.getText();

        for(Actor model : editActors)
        {
          ((SelectObject)model).setNewKey(oldKey, newKey);
        }
        oldKey = newKey;
      }
    });
    editKey.addListener(tabListener);
    
    final TextField editValue = new TextField(value, skin);
    editValue.setTextFieldListener(new TextFieldListener()
    {
      @Override
      public void keyTyped(TextField textField, char c)
      {
        if (c == '\r' || c == '\n')
        {
          getStage().setKeyboardFocus(null);
          return;
        }

        if (editActors == null)
          return;
        
        String value = textField.getText();
        
        for(Actor model : editActors)
        {
          ((SelectObject)model).setVariable(editKey.getText(), value);
        }
      }
    });
    editValue.addListener(tabListener);
    
    final ButtonStyle styleButton = skin.get(ButtonStyle.class);

    ImageButtonStyle style = new ImageButtonStyle(styleButton.up, styleButton.down, null,
                                                  minusDrawable, minusDrawable, null);

    final ImageButton removeButton = new ImageButton(style);
    removeButton.addListener(new ClickListener()
    {
      @Override
      public void clicked(InputEvent event, float x, float y)
      {
        if (editActors == null)
          return;
        
        String key = editKey.getText();
        for(Actor model : editActors)
        {
          ((SelectObject)model).removeVariable(key);
        }
        
        editKey.remove();
        editValue.remove();
        removeButton.remove();
      }
    });

    table.add(editKey);
    table.add(editValue);
    table.add(removeButton).size(32, 32);
    table.row();
  }

  private void addNewVariable()
  {
    if (editActors == null)
      return;
    
    String key = editKey.getText();
    if (key.isEmpty())
      return;
    
    if (checkKey(key))
      return;
    
    String value = editValue.getText();
    for(Actor model : editActors)
    {
      ((SelectObject)model).setVariable(key, value);
    }
    
    addVariable(key, value);
    
    editKey.setText("");
    editValue.setText("");
    getStage().setKeyboardFocus(editKey);
  }
}
