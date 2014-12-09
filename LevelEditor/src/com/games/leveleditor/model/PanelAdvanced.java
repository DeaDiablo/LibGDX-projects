package com.games.leveleditor.model;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.games.leveleditor.controller.CommandController;
import com.games.leveleditor.controller.TexCoordCommand;
import com.games.leveleditor.controller.TextureCommand;
import com.shellGDX.manager.ResourceManager;

public class PanelAdvanced extends PanelScroll
{
  protected Table       table     = null;
  protected Skin        skin      = null;
  protected TextField   editKey   = null;
  protected TextField   editValue = null;
  protected ImageButton addButton = null;

  protected TextField  texture        = null;
  protected TextField  u0, v0, u1, v1 = null;
  protected String     textBuffer     = null;
  protected int        cursorBuffer   = 0;
  protected boolean    updateProperties = false;

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
  
  public PanelAdvanced(String title, Skin skin)
  {
    super(title, skin);
    this.skin = skin;
    
    clear();
    
    texture = new TextField("", skin);
    texture.setTextFieldListener(new TextFieldListener()
    {
      @Override
      public void keyTyped(TextField textField, char c)
      {
        if (editActors == null || c == 0 || c == '\t')
          return;
        
        if (c == '\r' || c == '\n')
        {
          getStage().setKeyboardFocus(null);
          return;
        }
        
        TextureCommand textureCommand = new TextureCommand();
        for (Actor actor : editActors)
        {
          if (actor instanceof EditModel)
            textureCommand.addActor((EditModel)actor);
        }
        textureCommand.setNewTexture(textField.getText());
        CommandController.instance.addCommand(textureCommand, false);
      }
    });
    texture.addListener(tabListener);
    
    table = new Table(skin);
    table.align(Align.top);
    table.defaults().spaceBottom(5);
    table.defaults().space(10);
    
    table.add(new Label("Path: ", skin));
    table.add(texture).width(350);
    table.row();
    
    TextFieldFilter filter = new TextFieldFilter()
    {
      @Override
      public boolean acceptChar(TextField textField, char c)
      {
        textBuffer = textField.getText();
        cursorBuffer = textField.getCursorPosition();
        if (c == '-' && (cursorBuffer == 0 || updateProperties))
          return true;
        if (c >= '0' && c <= '9' || c == '.')
          return true;

        return false;
      }
    };
    
    TextFieldListener texCoordListner = new TextFieldListener()
    {
      @Override
      public void keyTyped(TextField textField, char c)
      {
        if (editActors == null || c == 0 || c == '\t')
          return;
        
        if (c == '\r' || c == '\n')
        {
          getStage().setKeyboardFocus(null);
          return;
        }
        
        if (c == '.' &&
            textField.getText().indexOf(c) != textField.getText().lastIndexOf(c))
        {
          textField.setText(textBuffer);
          textField.setCursorPosition(cursorBuffer);
          return;
        }
        
        TexCoordCommand textureCommand = new TexCoordCommand();
        for (Actor actor : editActors)
        {
          if (actor instanceof EditModel)
            textureCommand.addActor((EditModel)actor);
        }
        
        try
        {
          float u0f = Float.valueOf(u0.getText());
          float v0f = Float.valueOf(v0.getText());
          float u1f = Float.valueOf(u1.getText());
          float v1f = Float.valueOf(v1.getText());
          
          textureCommand.setTexCoord(u0f, v0f, u1f, v1f);
          CommandController.instance.addCommand(textureCommand, false);
        }
        catch (NumberFormatException exception)
        {
        }
      }
    };
    
    u0 = new TextField("", skin);
    u0.setTextFieldListener(texCoordListner);
    u0.setTextFieldFilter(filter);
    u0.addListener(tabListener);
    
    v0 = new TextField("", skin);
    v0.setTextFieldListener(texCoordListner);
    v0.setTextFieldFilter(filter);
    v0.addListener(tabListener);
    
    u1 = new TextField("", skin);
    u1.setTextFieldListener(texCoordListner);
    u1.setTextFieldFilter(filter);
    u1.addListener(tabListener);
    
    v1 = new TextField("", skin);
    v1.setTextFieldListener(texCoordListner);
    v1.setTextFieldFilter(filter);
    v1.addListener(tabListener);
    
    Table texCoordTable = new Table(skin);
    texCoordTable.defaults().spaceBottom(5);
    texCoordTable.defaults().space(10);
    
    texCoordTable.add(new Label("u0", skin));
    texCoordTable.add(u0).width(50);
    texCoordTable.add(new Label("v0", skin));
    texCoordTable.add(v0).width(50);

    texCoordTable.add(new Label("u1", skin));
    texCoordTable.add(u1).width(50);
    texCoordTable.add(new Label("v1", skin));
    texCoordTable.add(v1).width(50);

    table.add(new Label("TexCoord: ", skin));
    table.add(texCoordTable);
    add(table).spaceBottom(10);
    row();

    scroll = new ScrollPane(content, skin);
    scroll.getListeners().removeIndex(0);
    add(scroll).expand().fill();
    
    table = new Table(skin);
    table.align(Align.top);
    table.defaults().spaceBottom(5);
    table.defaults().space(10);
    content.add(table).fill().expand();
    
    Table buttons = new Table(skin);
    buttons.defaults().spaceBottom(5);
    buttons.defaults().space(10);
    
    row();
    add(buttons).spaceTop(5);
    
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

    buttons.add(editKey).width(200);
    buttons.add(editValue).width(200);
    buttons.add(addButton).size(32, 32);

    updateTable();
    
    setSize(500, 500);
  }
  
  protected Array<Actor> editActors = null;

  public void setEditActors(Array<Actor> models)
  {
    updateProperties = true;
    if (models == null || models.size <= 0)
      editActors = null;
    else
      editActors = models;
    updateTable();
    updateProperties = false;
  }
  
  public void updateTable()
  {
    table.clear();
    
    boolean active = (editActors != null);

    texture.setText("");
    u0.setText("");
    v0.setText("");
    u1.setText("");
    v1.setText("");
    editKey.setText("");
    editValue.setText("");
    
    texture.setDisabled(!active);
    u0.setDisabled(!active);
    v0.setDisabled(!active);
    u1.setDisabled(!active);
    v1.setDisabled(!active);
    editKey.setDisabled(!active);
    editValue.setDisabled(!active);
    addButton.setDisabled(!active);
    
    disableActors(getChildren(), !active);
    
    if (!active)
      return;

    getStage().setKeyboardFocus(null);

    if (editActors.size > 0)
    {
      Actor editActor = null;
      for (Actor actor : editActors)
      {
        if (actor instanceof EditModel)
        {
          editActor = actor;
          break;
        }
      }

      if (editActor != null)
      {
        EditModel model = (EditModel)editActor;
        texture.setText(model.getPath());
        u0.setText(String.format(Locale.ENGLISH, "%.2f", model.getU0()));
        v0.setText(String.format(Locale.ENGLISH, "%.2f", model.getV0()));
        u1.setText(String.format(Locale.ENGLISH, "%.2f", model.getU1()));
        v1.setText(String.format(Locale.ENGLISH, "%.2f", model.getV1()));
      }
      else
      {
        texture.setDisabled(true);
        u0.setDisabled(true);
        v0.setDisabled(true);
        u1.setDisabled(true);
        v1.setDisabled(true);
      }
    }

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
  
  protected void disableActors(SnapshotArray<Actor> actors, boolean disable)
  {
    Color color = disable ? disableColor : enableColor;
    for (Actor actor : actors)
    {
      actor.setColor(color);

      if (actor instanceof Group)
      {
        if (!(actor instanceof CheckBox))
          disableActors(((Group)actor).getChildren(), disable);
      }
      
      if (disable)
      {
        if (actor instanceof TextField)
        {
          ((TextField) actor).setText("");
        }
        else if (actor instanceof CheckBox)
        {
          CheckBox checkBox = (CheckBox) actor;
          checkBox.setChecked(false);
        }
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
