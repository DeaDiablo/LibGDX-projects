package com.games.leveleditor.model;

import java.util.Locale;
import java.util.Vector;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.games.leveleditor.controller.CommandController;
import com.games.leveleditor.controller.GroupCommand;
import com.games.leveleditor.controller.NameCommand;
import com.games.leveleditor.controller.RotateCommand;
import com.games.leveleditor.controller.ScaleCommand;
import com.games.leveleditor.controller.TranslateCommand;
import com.games.leveleditor.controller.Updater;
import com.games.leveleditor.controller.VisibleCommand;

public class PanelProperties extends Panel
{
  public TextField name      = null;
  public CheckBox  visible   = null;
  public TextField positionX = null;
  public TextField positionY = null;
  public TextField rotation  = null;
  public TextField scaleX    = null;
  public TextField scaleY    = null;
  
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
        if (editModels == null || c == 0)
          return;
        
        GroupCommand groupCommand = new GroupCommand();

        for (EditModel model : editModels)
        {
          if (textField == name)
          {
            NameCommand nameCommand = new NameCommand();
            nameCommand.setNewName(textField.getText());
            nameCommand.addActor(model);
            nameCommand.addUpdater(panelUpdater);
            groupCommand.addCommand(nameCommand);
          }
          else
          {
            if (textField.getText().indexOf(c) != textField.getText().lastIndexOf(c))
            {
              textField.setText(textBuffer);
              textField.setCursorPosition(cursorBuffer);
              return;
            }
            
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
              scaleCommand.setNewScale(value, model.getScaleY());
              scaleCommand.addActor(model);
              groupCommand.addCommand(scaleCommand);
            }
            else if (textField == scaleY)
            {
              ScaleCommand scaleCommand = new ScaleCommand();
              scaleCommand.setNewScale(model.getScaleX(),value);
              scaleCommand.addActor(model);
              groupCommand.addCommand(scaleCommand);
            }
          }
        }
        groupCommand.addUpdater(panelUpdater);
        CommandController.instance.addCommand(groupCommand, false);
      }
    };
    
    // name
    name = new TextField("", skin);
    name.setTextFieldListener(listener);
    add(new Label("Name: ", skin));
    add(name);

    // visible
    visible = new CheckBox(" visible", skin);
    visible.addListener(new ChangeListener()
    {
      @Override
      public void changed(ChangeEvent event, Actor actor)
      {
        if (updateProperties || editModels == null)
          return;

        GroupCommand groupCommand = new GroupCommand();

        for (EditModel model : editModels)
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
    positionY = new TextField("", skin);
    positionY.setTextFieldListener(listener);
    positionY.setTextFieldFilter(filter);
    add(new Label("Position: ", skin));
    add(positionX);
    add(positionY);

    row();

    // angle
    rotation = new TextField("", skin);
    rotation.setTextFieldListener(listener);
    rotation.setTextFieldFilter(filter);
    add(new Label("Angle: ", skin));
    add(rotation);

    row();

    // scale
    scaleX = new TextField("", skin);
    scaleX.setTextFieldListener(listener);
    scaleX.setTextFieldFilter(filter);
    scaleY = new TextField("", skin);
    scaleY.setTextFieldListener(listener);
    scaleY.setTextFieldFilter(filter);
    add(new Label("Scale: ", skin));
    add(scaleX);
    add(scaleY);
    
    row();

    setSize(450, 250);
    setEditModels(null);
  }

  protected Vector<EditModel> editModels = null;

  public void setEditModels(Vector<EditModel> models)
  {
    updateProperties = true;
    
    if (models == null || models.isEmpty())
      editModels = null;
    else
      editModels = models;

    boolean disable = (editModels == null);

    name.setDisabled(disable);
    visible.setDisabled(disable);
    positionX.setDisabled(disable);
    positionY.setDisabled(disable);
    rotation.setDisabled(disable);
    scaleX.setDisabled(disable);
    scaleY.setDisabled(disable);
    
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
      }
    }
    updateProperties = false;

    if (!disable)
      updateProperties();
  }

  public void updateProperties()
  {
    if (editModels == null)
      return;

    updateProperties = true;

    EditModel model = editModels.get(0);
    getStage().unfocusAll();
    name.setText(model.getName());
    visible.setChecked(model.isVisible());
    positionX.setText(String.format(Locale.ENGLISH, "%.2f", model.getX()));
    positionY.setText(String.format(Locale.ENGLISH, "%.2f", model.getY()));
    rotation.setText(String.format(Locale.ENGLISH, "%.2f", model.getRotation()));
    scaleX.setText(String.format(Locale.ENGLISH, "%.2f", model.getScaleX()));
    scaleY.setText(String.format(Locale.ENGLISH, "%.2f", model.getScaleY()));
    updateProperties = false;
  }
}
