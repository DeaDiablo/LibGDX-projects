package com.games.leveleditor.screen;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.games.leveleditor.controller.AddCommand;
import com.games.leveleditor.controller.Command;
import com.games.leveleditor.controller.CommandController;
import com.games.leveleditor.controller.DelCommand;
import com.games.leveleditor.controller.RotateCommand;
import com.games.leveleditor.controller.ScaleCommand;
import com.games.leveleditor.controller.TranslateCommand;
import com.games.leveleditor.controller.Updater;
import com.games.leveleditor.model.EditModel;
import com.games.leveleditor.model.Layer;
import com.games.leveleditor.model.PanelGraphics;
import com.games.leveleditor.model.PanelLayers;
import com.games.leveleditor.model.PanelMain;
import com.games.leveleditor.model.PanelProperties;
import com.games.leveleditor.model.PanelTree;
import com.shellGDX.GameLog;
import com.shellGDX.manager.ResourceManager;
import com.shellGDX.model2D.Scene2D;
import com.shellGDX.screen.GameScreen;

public class MainScreen extends GameScreen implements InputProcessor
{
  //scenes
  private Scene2D mainScene = null;
  private Scene2D guiScene = null;
  
  private EditModel         addModel   = null;
  private Vector<EditModel> copyModels = new Vector<EditModel>();
  private Layer             selectLayer = null;
  
  private PanelMain       main       = null;
  private PanelProperties properties = null;
  private PanelTree       tree       = null;
  private PanelLayers     layers     = null;
  private PanelGraphics   graphics   = null;

  public final Updater selectionUpdater = new Updater()
  {
    @Override
    public void update()
    {
      properties.setEditModels(selectLayer.getSelectedModels());
      tree.panelUpdater.update();
    }
  };
  
  public MainScreen()
  {
    super();
  }

  @Override
  public void show()
  {
    Gdx.input.setCatchBackKey(true);
    Gdx.input.setCatchMenuKey(true);
    setClearColor(0.5f, 0.5f, 0.5f, 1);
    
    EditModel.setRegionEdit("bb", ResourceManager.instance.getTextureRegion("data/editor/editor.png", 0, 0, 236, 237));
    EditModel.setRegionEdit("scale", ResourceManager.instance.getTextureRegion("data/editor/editor.png", 236, 0, 280, 280));
    EditModel.setRegionEdit("rotate", ResourceManager.instance.getTextureRegion("data/editor/editor.png", 516, 0, 277, 288));
    EditModel.setRegionEdit("point", ResourceManager.instance.getTextureRegion("data/editor/editor.png", 0, 280, 302, 302));
    
    mainScene = new Scene2D(1920.0f, 1080.0f);
    
    Layer layer = new Layer("layer");
    mainScene.addActor(layer);
    selectLayer = layer;
    
    guiScene = new Scene2D(1920.0f, 1080.0f);
    
    Skin skin = ResourceManager.instance.getSkin("data/skin/uiskin.json");
   
    //main
    main = new PanelMain("editor", skin);
    main.setPosition(0, guiScene.getHeight() - main.getHeight());
    guiScene.addActor(main);
    
    //properties
    properties = new PanelProperties("properties", skin); 
    properties.setPosition(0, main.getY() - properties.getHeight());  
    guiScene.addActor(properties);
    
    //tree
    tree = new PanelTree("objects of layer", skin, this);
    tree.setPosition(0, properties.getY() - tree.getHeight());
    tree.setLayer(layer);
    guiScene.addActor(tree);
    
    //layers
    layers = new PanelLayers("layers", skin);
    layers.setPosition(0, 0);  
    guiScene.addActor(layers);
    
    //graphics
    graphics = new PanelGraphics("graphics", skin, this);
    graphics.setPosition(layers.getWidth(), 0);
    guiScene.addActor(graphics);

    contoller.addProcessor(new UndoRedoProcessor());
    contoller.addScene2D(mainScene);
    contoller.addScene2D(guiScene);
    contoller.addProcessor(this);
  }
  
  @Override
  public void update(float deltaTime)
  {
    GameLog.instance.writeFPS();
    super.update(deltaTime);
  }
  
  public void clearAddModel()
  {
    graphics.clearActiveButton();
    if (addModel != null)
    {
      addModel.remove();
      addModel = null;
    }
  }

  public void setAddModel(String name)
  {
    clearSelection();
    
    String nameModel = name;
    int index = name.lastIndexOf(".");
    if (index > 0)
      nameModel = nameModel.substring(0, index);
    
    addModel = new EditModel(nameModel, ResourceManager.instance.getTextureRegion(name));
    addModel.setColor(1, 1, 1, 0.5f);
    mainScene.addActor(addModel);
  }

  public void clearSelection()
  {
    clearAddModel();
    properties.setEditModels(null);
    
    Vector<EditModel> models = selectLayer.getModels();
    for(int i = 0; i < models.size(); i++)
      models.get(i).setSelection(false);
  }

  protected boolean ctrlPress = false;
  protected boolean shiftPress = false;

  @Override
  public boolean keyDown(int keycode)
  {
    switch(keycode)
    {
      case Input.Keys.CONTROL_LEFT:
      case Input.Keys.CONTROL_RIGHT:
        ctrlPress = true;
        break;
      case Input.Keys.SHIFT_LEFT:
      case Input.Keys.SHIFT_RIGHT:
        shiftPress = true;
        break;
    }
    return false;
  }

  @Override
  public boolean keyUp(int keycode)
  {
    switch(keycode)
    {
      case Input.Keys.ESCAPE:
        clearAddModel();
        break;
      case Input.Keys.DEL:
      case Input.Keys.FORWARD_DEL:
        if (selectLayer != null)
        {
          DelCommand command = new DelCommand();
          command.setLayer(selectLayer);
          command.addModels(selectLayer.getSelectedModels());
          command.addUpdater(selectionUpdater);
          CommandController.instance.addCommand(command);
        }
        break;
      case Input.Keys.H:
        EditModel.setHideMode(!EditModel.getHideMode());
        break;
      case Input.Keys.V:
        if (ctrlPress && selectLayer != null)
        {
          AddCommand command = new AddCommand();
          command.setLayer(selectLayer);
          command.addModels(copyModels);
          command.addUpdater(tree.panelUpdater);
          CommandController.instance.addCommand(command);
        }
        break;
      case Input.Keys.C:
        if (ctrlPress && selectLayer != null)
        {
          copyModels.clear();
          Vector<EditModel> selectedModels = selectLayer.getSelectedModels();
          for(int i = 0; i < selectedModels.size(); i++)
          {
            copyModels.add(selectedModels.get(i).copy());
          }
        }
        break;
      case Input.Keys.X:
        if (ctrlPress && selectLayer != null)
        {
          copyModels.clear();
          Vector<EditModel> selectedModels = selectLayer.getSelectedModels();
          for(int i = 0; i < selectedModels.size(); i++)
          {
            copyModels.add(selectedModels.get(i).copy());
          }

          DelCommand command = new DelCommand();
          command.setLayer(selectLayer);
          command.addModels(selectedModels);
          command.addUpdater(selectionUpdater);
          CommandController.instance.addCommand(command);
        }
        break;
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

  @Override
  public boolean keyTyped(char character)
  {
    // TODO Auto-generated method stub
    return false;
  }

  protected final float minDelta = 3.0f;
  protected int touchPointer = -1;
  protected Vector2 touch = new Vector2();
  protected Vector2 newTouch = new Vector2();
  protected Vector2 delta = new Vector2();
  protected Vector2 buffer1 = new Vector2();
  protected Vector2 buffer2 = new Vector2();
  protected Command command = null;
  protected EditModel mainSelectModel = null;
  protected float   bufferX = 0, bufferY = 0;
  protected boolean translate = false, rotate = false, scale = false, move = false;
  protected int     direction = 0;
  
  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button)
  {
    touchPointer = pointer;
    touch.set(mainScene.screenToSceneCoordinates(screenX, screenY));
    newTouch.set(touch);
    
    Vector<EditModel> selected = selectLayer.getSelectedModels();
    for(int i = 0; i < selected.size(); i++)
    {
      EditModel model = selected.get(i);
      
      if (model.getBound().contains(touch))
      {
        translate = true;
        break;
      }
      else if (model.isRotate(touch))
      {
        rotate = true;
        bufferX = model.getRotation();
        mainSelectModel = model;
      }
      else if (model.isScale(touch))
      {
        scale = true;
        bufferX = model.getScaleX();
        bufferY = model.getScaleY();
        mainSelectModel = model;
      }
    }
    
    if (translate)
    {
      TranslateCommand transCommand = new TranslateCommand();
      for(int i = 0; i < selected.size(); i++)
      {
        transCommand.addActor(selected.get(i));
        transCommand.addUpdater(properties.panelUpdater);
      }
      command = transCommand;
      rotate = false;
      scale = false;
      return true;
    }
    
    if (rotate)
    {
      RotateCommand rotateCommand = new RotateCommand();
      for(int i = 0; i < selected.size(); i++)
      {
        rotateCommand.addActor(selected.get(i));
        rotateCommand.addUpdater(properties.panelUpdater);
      }
      command = rotateCommand;
      scale = false;
      return true;
    }
    
    if (scale)
    {
      ScaleCommand scaleCommand = new ScaleCommand();
      for(int i = 0; i < selected.size(); i++)
      {
        scaleCommand.addActor(selected.get(i));
        scaleCommand.addUpdater(properties.panelUpdater);
      }
      command = scaleCommand;
      return true;
    }
    
    return true;
  }
  
  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button)
  {
    if (move)
    {
      move = false;
      return false;
    }
    
    if (touchPointer != pointer)
      return false;
    
    newTouch = mainScene.screenToSceneCoordinates(screenX, screenY);    
    delta.set(newTouch);
    delta.sub(touch);

    if (addModel != null)
    {
      switch(button)
      {
        case Buttons.LEFT:
        {
          EditModel newModel = addModel.copy();
          newModel.setPosition(newTouch);
          
          AddCommand command = new AddCommand();
          command.addModel(newModel);
          command.setLayer(selectLayer);
          command.addUpdater(tree.panelUpdater);
          CommandController.instance.addCommand(command);
          break;
        }
        case Buttons.RIGHT:
        {
          clearAddModel();
          break;
        }
      }
      return true;
    }

    if (translate)
    {
      TranslateCommand transCommand = (TranslateCommand)command;
      transCommand.setDelta(newTouch.x - touch.x, newTouch.y - touch.y);
      CommandController.instance.addCommand(transCommand);
    }
    else if (rotate)
    {
      RotateCommand rotateCommand = (RotateCommand)command;
      rotateCommand.setDelta(mainSelectModel.getRotation() - bufferX);
      CommandController.instance.addCommand(rotateCommand);
    }
    else if (scale)
    {
      ScaleCommand scaleCommand = (ScaleCommand)command;
      scaleCommand.setDelta(mainSelectModel.getScaleX() - bufferX, mainSelectModel.getScaleY() - bufferY);
      CommandController.instance.addCommand(scaleCommand);
    }
    
    if (delta.len() < minDelta)
    {
      boolean firstSelect = false;
      Vector<EditModel> models = selectLayer.getModels();
      for(int i = models.size() - 1; i >= 0; i--)
      {
        EditModel model = models.get(i);
        if (model.getBound().contains(touch) && (!firstSelect || ctrlPress))
        {
          model.setSelection(!model.isSelected());
          firstSelect = true;
        }
        else if (!ctrlPress)
        {
          model.setSelection(false);
        }
        selectionUpdater.update();
      }
    }

    direction = 0;
    translate = false;
    rotate = false;
    scale = false;
    command = null;
    properties.updateProperties();

    return true;
  }
  
  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer)
  {
    if (touchPointer != pointer)
      return false;
    
    if (translate)
    {
      delta.set(newTouch);
      newTouch.set(mainScene.screenToSceneCoordinates(screenX, screenY));
      delta.sub(newTouch);
      Vector<EditModel> models = selectLayer.getSelectedModels();
      for(int i = 0; i < models.size(); i++)
      {
        EditModel model = models.get(i);
        if (shiftPress)
        {
          if (direction == 0)
          {
            if (delta.len() < 4)
              return true;

            if (Math.abs(delta.x) > Math.abs(delta.y))
              direction = 1;
            else
              direction = 2;
          }
          
          if (direction == 1)
            model.moveBy(-delta.x, 0);
          else
            model.moveBy(0, -delta.y);
        }
        else
        {
          model.moveBy(-delta.x, -delta.y);
        }
      }
      return true;
    }
    
    if (rotate)
    {
      delta.set(newTouch);
      newTouch.set(mainScene.screenToSceneCoordinates(screenX, screenY));
      Vector<EditModel> models = selectLayer.getSelectedModels();
      for(int i = 0; i < models.size(); i++)
      {
        EditModel model = models.get(i);

        buffer1.set(delta);
        buffer1 = mainSelectModel.stageToLocalCoordinates(buffer1);
        buffer1.add(mainSelectModel.getOffset());

        buffer2.set(newTouch);
        buffer2 = mainSelectModel.stageToLocalCoordinates(buffer2);
        buffer2.add(mainSelectModel.getOffset());
          
        float deltaAngle = (MathUtils.atan2(buffer2.y, buffer2.x) - MathUtils.atan2(buffer1.y, buffer1.x)) * MathUtils.radiansToDegrees;

        float angle = model.getRotation() + deltaAngle;
        while(angle > 360 || angle < 0)
        {
          angle += angle > 360 ? -360 : 360;
        }
 
        if (shiftPress)
        {
          if (angle < 1)
            angle = 0;
          else if (angle > 44 && angle < 46)
            angle = 45;
          else if (angle > 89 && angle < 91)
            angle = 90;
          else if (angle > 134 && angle < 136)
            angle = 135;
          else if (angle > 179 && angle < 181)
            angle = 180;
          else if (angle > 224 && angle < 226)
            angle = 225;
          else if (angle > 269 && angle < 271)
            angle = 270;
          else if (angle > 314 && angle < 316)
            angle = 315;
          else if (angle > 359)
            angle = 360;
        }

        model.setRotation(angle);
      }
      return true;
    }
    
    if (scale)
    {
      delta.set(newTouch);
      newTouch.set(mainScene.screenToSceneCoordinates(screenX, screenY));
      delta.sub(newTouch);
      Vector<EditModel> models = selectLayer.getSelectedModels();
      for(int i = 0; i < models.size(); i++)
      {
        EditModel model = models.get(i);

        buffer1.set(delta);
        buffer1.scl(2.0f);
        buffer1.x /= model.getScale().x;
        buffer1.y /= model.getScale().y;
        float scaleX = (model.getWidth() - buffer1.x) * model.getScaleX() / model.getWidth();
        float scaleY = (model.getHeight() + buffer1.y) * model.getScaleY() / model.getHeight();
        
        if (shiftPress)
        {
          model.setScaleX(scaleX);
          model.setScaleY(scaleY);
        }
        else
        {
          model.setScale((scaleX + scaleY) * 0.5f);
        }
      }
      return true;
    }

    delta.set(newTouch);
    newTouch.set(mainScene.screenToSceneCoordinates(screenX, screenY));
    delta.sub(newTouch);
    
    if (delta.len() > minDelta)
      move = true;
    
    mainScene.getCamera().translate(delta.x, delta.y, 0);
    newTouch.add(delta);

    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY)
  {
    if (addModel != null)
    {
      newTouch.set(mainScene.screenToSceneCoordinates(screenX, screenY));
      addModel.setPosition(newTouch);
      return true;
    }
    return false;
  }

  @Override
  public boolean scrolled(int amount)
  {
    // TODO Auto-generated method stub
    return false;
  }
}
