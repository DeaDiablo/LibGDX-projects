package com.games.leveleditor.model;

import java.util.Locale;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.games.leveleditor.controller.ColorCommand;
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

public class PanelProperties extends PanelScroll
{
  public TextField  name      = null;
  public CheckBox   visible   = null;
  public TextField  positionX = null;
  public TextField  positionY = null;
  public TextField  rotation  = null;
  public TextField  scaleX    = null;
  public TextField  scaleY    = null;
  public TextField  r, g, b, a = null;
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
    
    scroll.getListeners().removeIndex(0);

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
              else
              {
                ColorCommand colorCommand = new ColorCommand();
                colorCommand.addActor(model);
                value /= 255.0f;
                value = Math.min(1.0f, Math.max(0.0f, value));
                if (textField == r)
                  colorCommand.setR(value);
                else if (textField == g)
                  colorCommand.setG(value);
                else if (textField == b)
                  colorCommand.setB(value);
                else
                  colorCommand.setA(value);
                groupCommand.addCommand(colorCommand);
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
    content.add(new Label("Name: ", skin));
    content.add(name);

    // visible
    visible = new CheckBox(" visible", skin);
    visible.getStyle().disabledFontColor = disableColor;
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
    content.add(visible);

    content.row();

    // position
    positionX = new TextField("", skin);
    positionX.setTextFieldListener(listener);
    positionX.setTextFieldFilter(filter);
    positionX.addListener(tabListener);
    positionY = new TextField("", skin);
    positionY.setTextFieldListener(listener);
    positionY.setTextFieldFilter(filter);
    positionY.addListener(tabListener);
    content.add(new Label("Position: ", skin));
    content.add(positionX);
    content.add(positionY);

    content.row();

    // angle
    rotation = new TextField("", skin);
    rotation.setTextFieldListener(listener);
    rotation.setTextFieldFilter(filter);
    rotation.addListener(tabListener);
    content.add(new Label("Angle: ", skin));
    content.add(rotation);
    
    lockRatio = new CheckBox(" ratio", skin);
    lockRatio.getStyle().disabledFontColor = disableColor;
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
    content.add(lockRatio);

    content.row();

    // scale
    scaleX = new TextField("", skin);
    scaleX.setTextFieldListener(listener);
    scaleX.setTextFieldFilter(filter);
    scaleX.addListener(tabListener);
    scaleY = new TextField("", skin);
    scaleY.setTextFieldListener(listener);
    scaleY.setTextFieldFilter(filter);
    scaleX.addListener(tabListener);
    content.add(new Label("Scale: ", skin));
    content.add(scaleX);
    content.add(scaleY);
    
    content.row();

    r = new TextField("", skin);
    r.setTextFieldListener(listener);
    r.setTextFieldFilter(filter);
    r.addListener(tabListener);
    g = new TextField("", skin);
    g.setTextFieldListener(listener);
    g.setTextFieldFilter(filter);
    g.addListener(tabListener);
    b = new TextField("", skin);
    b.setTextFieldListener(listener);
    b.setTextFieldFilter(filter);
    b.addListener(tabListener);
    a = new TextField("", skin);
    a.setTextFieldListener(listener);
    a.setTextFieldFilter(filter);
    a.addListener(tabListener);
    content.add(new Label("Color: ", skin));
    
    Table colorTable = new Table(skin);
    colorTable.defaults().spaceBottom(10);
    colorTable.defaults().space(10);
    colorTable.add(r).width(75);
    colorTable.add(g).width(75);   
    content.add(colorTable); 
    colorTable = new Table(skin);
    colorTable.defaults().spaceBottom(10);
    colorTable.defaults().space(10);
    colorTable.add(b).width(75);
    colorTable.add(a).width(75);
    content.add(colorTable);
    
    content.row();
    
    final TextButtonStyle styleButton = skin.get(TextButtonStyle.class);
    TextButtonStyle style = new TextButtonStyle(styleButton.up, styleButton.down, styleButton.down, styleButton.font);
    style.disabledFontColor = disableColor;
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
    content.add(new Label("Advanced:", skin));
    content.add(variablesButton);

    setSize(450, 300);
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
    r.setDisabled(disable);
    g.setDisabled(disable);
    b.setDisabled(disable);
    a.setDisabled(disable);
    //variablesButton.setDisabled(disable);
    
    disableActors(getChildren(), disable);
    
    updateProperties = false;

    if (!disable)
      updateProperties();
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
    Color color = model.getColor();
    r.setText(String.format(Locale.ENGLISH, "%.0f", color.r * 255));
    g.setText(String.format(Locale.ENGLISH, "%.0f", color.g * 255));
    b.setText(String.format(Locale.ENGLISH, "%.0f", color.b * 255));
    a.setText(String.format(Locale.ENGLISH, "%.0f", color.a * 255));
    
    scaleY.setDisabled(lockRatio.isChecked());
    scaleY.setColor(lockRatio.isChecked() ? disableColor : enableColor);
    
    updateProperties = false;
  }
  
  public boolean getRatio()
  {
    return lockRatio.isChecked();
  }
}
