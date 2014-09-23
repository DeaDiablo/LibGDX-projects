package com.games.leveleditor.model;

import java.util.Vector;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class PanelProperties extends Panel
{
  public CheckBox  visible   = null;
  public TextField positionX = null;
  public TextField positionY = null;
  public TextField rotation  = null;
  public TextField scaleX    = null;
  public TextField scaleY    = null;

  public PanelProperties(String title, Skin skin)
  {
    super(title, skin);
    setSize(1000, 1000);
    defaults().spaceBottom(10);
    defaults().space(10);

    TextFieldFilter filter = new TextFieldFilter()
    {
      @Override
      public boolean acceptChar(TextField textField, char c)
      {
        if (c == '.')
        {
          return (textField.getText().lastIndexOf(c) == -1);
        }

        if (c >= '0' && c <= '9')
          return true;

        return false;
      }
    };

    TextFieldListener listener = new TextFieldListener()
    {
      @Override
      public void keyTyped(TextField textField, char c)
      {
        if (editModels == null)
          return;

        for (EditModel model : editModels)
        {
          Float value = Float.valueOf(textField.getText());
          if (textField == positionX)
          {
            model.setX(value);
          }
          else if (textField == positionY)
          {
            model.setY(value);
          }
          else if (textField == rotation)
          {
            model.setRotation(value);
          }
          else if (textField == scaleX)
          {
            model.setScaleX(value);
          }
          else if (textField == scaleY)
          {
            model.setScaleY(value);
          }
        }
      }
    };

    // visible
    visible = new CheckBox(" visible", skin);
    visible.addListener(new ChangeListener()
    {
      @Override
      public void changed(ChangeEvent event, Actor actor)
      {
        if (editModels == null)
          return;

        for (EditModel model : editModels)
          model.setVisible(visible.isChecked());
      }
    });
    add(visible);

    row();

    // position
    positionX = new TextField("0", skin);
    positionX.setTextFieldListener(listener);
    positionX.setTextFieldFilter(filter);
    positionY = new TextField("0", skin);
    positionY.setTextFieldListener(listener);
    positionY.setTextFieldFilter(filter);
    add(new Label("Position: ", skin));
    add(positionX);
    add(positionY);

    row();

    // angle
    rotation = new TextField("0", skin);
    rotation.setTextFieldListener(listener);
    rotation.setTextFieldFilter(filter);
    add(new Label("Angle: ", skin));
    add(rotation);

    row();

    // scale
    scaleX = new TextField("1.0", skin);
    scaleX.setTextFieldListener(listener);
    scaleX.setTextFieldFilter(filter);
    scaleY = new TextField("1.0", skin);
    scaleY.setTextFieldListener(listener);
    scaleY.setTextFieldFilter(filter);
    add(new Label("Scale: ", skin));
    add(scaleX);
    add(scaleY);

    pack();
    setWidth(450);
  }

  protected Vector<EditModel> editModels = null;

  public void setEditModels(Vector<EditModel> models)
  {
    if (models == null || models.isEmpty())
      editModels = null;
    else
      editModels = models;

    boolean disable = (editModels == null);

    visible.setDisabled(disable);
    positionX.setDisabled(disable);
    positionY.setDisabled(disable);
    rotation.setDisabled(disable);
    scaleX.setDisabled(disable);
    scaleY.setDisabled(disable);

    if (disable)
    {
      visible.setChecked(false);
      positionX.setText("");
      positionY.setText("");
      rotation.setText("");
      scaleX.setText("");
      scaleY.setText("");
    }
    else
      updateProperties();
  }

  public void updateProperties()
  {
    if (editModels == null)
      return;

    EditModel model = editModels.get(0);
    visible.setChecked(model.isVisible());
    positionX.setText(String.valueOf(model.getX()));
    positionY.setText(String.valueOf(model.getY()));
    rotation.setText(String.valueOf(model.getRotation()));
    scaleX.setText(String.valueOf(model.getScaleX()));
    scaleY.setText(String.valueOf(model.getScaleY()));
  }
}
