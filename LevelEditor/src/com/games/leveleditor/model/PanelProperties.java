package com.games.leveleditor.model;

import java.util.Locale;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.games.leveleditor.controller.CommandController;
import com.games.leveleditor.controller.GroupCommand;
import com.games.leveleditor.controller.NameCommand;
import com.games.leveleditor.controller.RotateCommand;
import com.games.leveleditor.controller.ScaleCommand;
import com.games.leveleditor.controller.TranslateCommand;
import com.games.leveleditor.controller.Updater;
import com.games.leveleditor.controller.VisibleCommand;
import com.games.leveleditor.screen.MainScreen;
import com.shellGDX.GameInstance;

public class PanelProperties extends Panel
{
  public TextField  name      = null;
  public CheckBox   visible   = null;
  public TextField  positionX = null;
  public TextField  positionY = null;
  public TextField  rotation  = null;
  public TextField  scaleX    = null;
  public TextField  scaleY    = null;
  public CheckBox   lockRatio = null;
  public TextButton variablesButton = null;
  
  protected boolean updateProperties = false;
  protected String  textBuffer = null;
  protected int     cursorBuffer = 0;
  
  public final Updater panelUpdater = new Updater()
  {
    @Override
    public void update()
    {
      updateProperties();
    }
  };

  public PanelProperties(String title, Skin skin)
  {
    super(title, skin);

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

    TextFieldListener listener = new TextFieldListener()
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
        
        GroupCommand groupCommand = new GroupCommand();

        for (Actor model : editActors)
        {
          if (textField == name)
          {
            NameCommand nameCommand = new NameCommand();
            nameCommand.setNewName(textField.getText());
            nameCommand.addActor(model);
            nameCommand.addUpdater(((MainScreen)GameInstance.game.getScreen()).getTree().panelUpdater);
            groupCommand.addCommand(nameCommand);
          }
          else
          {
            if (c == '.' &&
                textField.getText().indexOf(c) != textField.getText().lastIndexOf(c))
            {
              textField.setText(textBuffer);
              textField.setCursorPosition(cursorBuffer);
              return;
            }
            
            if (textField.getText().isEmpty())
              return;
            
            try
            {
              Float value = Float.valueOf(textField.getText());
              if (textField == positionX && value != model.getX())
              {
                TranslateCommand transCommand = new TranslateCommand();
                transCommand.setNewPosition(value, model.getY());
                transCommand.addActor(model);
                groupCommand.addCommand(transCommand);
              }
              else if (textField == positionY)
              {
                TranslateCommand transCommand = new TranslateCommand();
                transCommand.setNewPosition(model.getX(), value);
                transCommand.addActor(model);
                groupCommand.addCommand(transCommand);
              }
              else if (textField == rotation)
              {
                RotateCommand rotateCommand = new RotateCommand();
                rotateCommand.setAngle(value);
                rotateCommand.addActor(model);
                groupCommand.addCommand(rotateCommand);
              }
              else if (textField == scaleX)
              {
                ScaleCommand scaleCommand = new ScaleCommand();
                scaleCommand.setNewScale(value, lockRatio.isChecked() ? value : model.getScaleY());
                scaleCommand.addActor(model);
                groupCommand.addCommand(scaleCommand);
                
                if (lockRatio.isChecked())
                  scaleY.setText(scaleX.getText());
              }
              else if (textField == scaleY)
              {
                ScaleCommand scaleCommand = new ScaleCommand();
                scaleCommand.setNewScale(model.getScaleX(), value);
                scaleCommand.addActor(model);
                groupCommand.addCommand(scaleCommand);
              }
            }
            catch (NumberFormatException exception)
            {
            }
          }
        }
        
        if (groupCommand.getCommands().size > 0)
          CommandController.instance.addCommand(groupCommand, false);
      }
    };
    
    InputListener tabListener = new InputListener()
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
    
    // name
    name = new TextField("", skin);
    name.setTextFieldListener(listener);
    name.addListener(tabListener);
    add(new Label("Name: ", skin));
    add(name);

    // visible
    visible = new CheckBox(" visible", skin);
    visible.addListener(new ChangeListener()
    {
      @Override
      public void changed(ChangeEvent event, Actor actor)
      {
        if (updateProperties || editActors == null)
          return;

        GroupCommand groupCommand = new GroupCommand();

        for (Actor model : editActors)
        {
          VisibleCommand command = new VisibleCommand();
          command.setNewVisible(visible.isChecked());
          command.addActor(model);
          groupCommand.addCommand(command);
        }

        CommandController.instance.addCommand(groupCommand);
      }
    });
    add(visible);

    row();

    // position
    positionX = new TextField("", skin);
    positionX.setTextFieldListener(listener);
    positionX.setTextFieldFilter(filter);
    positionX.addListener(tabListener);
    positionY = new TextField("", skin);
    positionY.setTextFieldListener(listener);
    positionY.setTextFieldFilter(filter);
    positionY.addListener(tabListener);
    add(new Label("Position: ", skin));
    add(positionX);
    add(positionY);

    row();

    // angle
    rotation = new TextField("", skin);
    rotation.setTextFieldListener(listener);
    rotation.setTextFieldFilter(filter);
    rotation.addListener(tabListener);
    add(new Label("Angle: ", skin));
    add(rotation);

    row();

    // scale
    scaleX = new TextField("", skin);
    scaleX.setTextFieldListener(listener);
    scaleX.setTextFieldFilter(filter);
    scaleX.addListener(tabListener);
    scaleY = new TextField("", skin);
    scaleY.setTextFieldListener(listener);
    scaleY.setTextFieldFilter(filter);
    scaleX.addListener(tabListener);
    add(new Label("Scale: ", skin));
    add(scaleX);
    add(scaleY);
    
    row();
    
    lockRatio = new CheckBox(" ratio", skin);
    lockRatio.addListener(new ChangeListener()
    {
      @Override
      public void changed(ChangeEvent event, Actor actor)
      {
        if (updateProperties || editActors == null)
          return;
        
        scaleY.setDisabled(lockRatio.isChecked());
        scaleY.setColor(lockRatio.isChecked() ? disableColor : enableColor);
        
        if (lockRatio.isChecked())
        {
          scaleY.setText(scaleX.getText());
          
          GroupCommand groupCommand = new GroupCommand();
          for (Actor model : editActors)
          {
            ScaleCommand scaleCommand = new ScaleCommand();
            scaleCommand.setNewScale(model.getScaleX(), model.getScaleX());
            scaleCommand.addActor(model);
            groupCommand.addCommand(scaleCommand);
          }
          CommandController.instance.addCommand(groupCommand);
        }
      }
    });
    
    final TextButtonStyle styleButton = skin.get(TextButtonStyle.class);
    TextButtonStyle style = new TextButtonStyle(styleButton.up, styleButton.down, styleButton.down, styleButton.font);
    variablesButton = new TextButton("Variables", style);
    variablesButton.addListener(new ClickListener()
    {
      @Override
      public void clicked(InputEvent event, float x, float y)
      {
        PanelVariables variables = ((MainScreen)GameInstance.game.getScreen()).getVariables();
        variables.setVisible(variablesButton.isChecked());
      }
    });
    add(new Label("Advanced:", skin));
    add(variablesButton);
    add(lockRatio);

    setSize(450, 250);
    setEditActors(null);
  }

  protected Array<Actor> editActors = null;

  public void setEditActors(Array<Actor> models)
  {
    updateProperties = true;
    
    if (models == null || models.size <= 0)
      editActors = null;
    else
      editActors = models;

    boolean disable = (editActors == null);

    name.setDisabled(disable);
    visible.setDisabled(disable);
    positionX.setDisabled(disable);
    positionY.setDisabled(disable);
    rotation.setDisabled(disable);
    lockRatio.setDisabled(disable);
    scaleX.setDisabled(disable);
    scaleY.setDisabled(disable);
    //variablesButton.setDisabled(disable);
    
    Color color = disable ? disableColor : enableColor;

    for (Actor actor : getChildren())
    {
      actor.setColor(color);
      
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
          checkBox.getStyle().disabledFontColor = disableColor;
        }
        else if (actor instanceof TextButton)
        {
          ((TextButton)actor).getStyle().disabledFontColor = disableColor;
        }
      }
    }
    updateProperties = false;

    if (!disable)
      updateProperties();
  }

  public void updateProperties()
  {
    if (editActors == null)
      return;

    updateProperties = true;

    Actor model = editActors.get(0);
    getStage().setKeyboardFocus(null);
    name.setText(model.getName());
    visible.setChecked(model.isVisible());
    positionX.setText(String.format(Locale.ENGLISH, "%.2f", model.getX()));
    positionY.setText(String.format(Locale.ENGLISH, "%.2f", model.getY()));
    rotation.setText(String.format(Locale.ENGLISH, "%.2f", model.getRotation()));
    lockRatio.setChecked(Math.abs(model.getScaleX() - model.getScaleY()) < 0.01f);
    scaleX.setText(String.format(Locale.ENGLISH, "%.2f", model.getScaleX()));
    scaleY.setText(String.format(Locale.ENGLISH, "%.2f", model.getScaleY()));
    
    scaleY.setDisabled(lockRatio.isChecked());
    scaleY.setColor(lockRatio.isChecked() ? disableColor : enableColor);
    
    updateProperties = false;
  }
  
  public boolean getRatio()
  {
    return lockRatio.isChecked();
  }
}
