package com.games.leveleditor.screen;

import java.io.IOException;
import java.util.Locale;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.games.leveleditor.controller.AddCommand;
import com.games.leveleditor.controller.AddGroupCommand;
import com.games.leveleditor.controller.Command;
import com.games.leveleditor.controller.CommandController;
import com.games.leveleditor.controller.DelCommand;
import com.games.leveleditor.controller.GoInGroupCommand;
import com.games.leveleditor.controller.GroupCommand;
import com.games.leveleditor.controller.RemoveGroupCommand;
import com.games.leveleditor.controller.RotateCommand;
import com.games.leveleditor.controller.ScaleCommand;
import com.games.leveleditor.controller.TranslateCommand;
import com.games.leveleditor.controller.UpDownCommand;
import com.games.leveleditor.controller.Updater;
import com.games.leveleditor.model.BoundingBox;
import com.games.leveleditor.model.EditGroup;
import com.games.leveleditor.model.EditModel;
import com.games.leveleditor.model.Layer;
import com.games.leveleditor.model.Operation;
import com.games.leveleditor.model.PanelGraphics;
import com.games.leveleditor.model.PanelLayers;
import com.games.leveleditor.model.PanelMain;
import com.games.leveleditor.model.PanelProperties;
import com.games.leveleditor.model.PanelTree;
import com.games.leveleditor.model.PanelVariables;
import com.games.leveleditor.model.SelectObject;
import com.shellGDX.GameInstance;
import com.shellGDX.GameLog;
import com.shellGDX.manager.ResourceManager;
import com.shellGDX.model2D.Scene2D;
import com.shellGDX.screen.GameScreen;

public class MainScreen extends GameScreen implements InputProcessor
{
  private final float width = 1920.0f;
  private final float height = 1080.0f;
  
  //scenes
  private String fileName = "";

  private Scene2D mainScene = null;
  private Scene2D guiScene = null;
  
  private Label           cursorText = null;
  private EditModel       addModel   = null;
  private Array<Actor>    copyModels = new Array<Actor>();
  
  private PanelMain       main       = null;
  private PanelProperties properties = null;
  private PanelVariables  variables  = null;
  private PanelTree       tree       = null;
  private PanelLayers     layers     = null;
  private PanelGraphics   graphics   = null;
  
  private final float     minGridSize = 16.0f;
  private final float     maxGridSize = 2048.0f;
  private float           gridSize    = 64.0f;
  private boolean         showGrid    = true;
  
  private Pixmap          moveCursor = new Pixmap(Gdx.files.internal("data/editor/cursors/move.png"));
  private Pixmap          vCursor    = new Pixmap(Gdx.files.internal("data/editor/cursors/vertical.png"));
  private Pixmap          hCursor    = new Pixmap(Gdx.files.internal("data/editor/cursors/horizontal.png"));
  private Pixmap          drCursor   = new Pixmap(Gdx.files.internal("data/editor/cursors/diagonal_right.png"));
  private Pixmap          dlCursor   = new Pixmap(Gdx.files.internal("data/editor/cursors/diagonal_left.png"));
  
  protected InputListener clickListener = new InputListener()
  {
    @Override
    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
    {
      return true;
    }
  };

  public final Updater propertiesUpdater = new Updater()
  {
    @Override
    public void update()
    {
      properties.setEditActors(layers.selectLayer.getSelectedModels());
      variables.setEditActors(layers.selectLayer.getSelectedModels());
    }
  };
  
  public MainScreen()
  {
    this("");
  }
  
  public MainScreen(String fileName)
  {
    super();
    this.fileName = fileName;
  }
  
  public Scene2D getMainScene()
  {
    return mainScene;
  }
  
  public Scene2D getGUIScene()
  {
    return guiScene;
  }
  
  public PanelMain getMain()
  {
    return main;
  }
  
  public PanelProperties getProperties()
  {
    return properties;
  }
  
  public PanelVariables getVariables()
  {
    return variables;
  }
  
  public PanelTree getTree()
  {
    return tree;
  }
  
  public PanelLayers getLayers()
  {
    return layers;
  }
  
  public PanelGraphics getGraphics()
  {
    return graphics;
  }
  
  protected ShapeRenderer shapeRenderer = null;

  @Override
  public void show()
  {
    shapeRenderer = GameInstance.view.getShapeRenderer();
    Gdx.input.setCatchBackKey(true);
    Gdx.input.setCatchMenuKey(true);
    setClearColor(0.25f, 0.25f, 0.25f, 1);

    mainScene = new Scene2D(width, height);
    Layer layer = new Layer("layer");
    mainScene.addActor(layer);
    
    guiScene = new Scene2D(width, height);
    
    Skin skin = ResourceManager.instance.getSkin("data/skin/uiskin.json");
   
    //main
    main = new PanelMain(fileName, skin);
    main.setPosition(0, guiScene.getHeight() - main.getHeight());
    main.addListener(clickListener);
    guiScene.addActor(main);
    
    //properties
    properties = new PanelProperties("Properties", skin); 
    properties.setPosition(0, main.getY() - properties.getHeight());
    properties.addListener(clickListener);
    guiScene.addActor(properties);
    
    variables = new PanelVariables("variables", skin);
    variables.setPosition(properties.getWidth(), guiScene.getHeight() - variables.getHeight());
    variables.setVisible(false);
    variables.addListener(clickListener);
    guiScene.addActor(variables);
    
    //tree
    tree = new PanelTree(skin, this);
    tree.setPosition(0, properties.getY() - tree.getHeight());
    tree.setGroup(layer);
    tree.addListener(clickListener);
    guiScene.addActor(tree);
    
    //layers
    layers = new PanelLayers("Layers", skin, mainScene, this);
    layers.setPosition(0, 0);
    layers.addListener(clickListener);
    guiScene.addActor(layers);
    
    //graphics
    graphics = new PanelGraphics("Graphics", skin, this);
    graphics.setPosition(layers.getWidth(), 0);
    graphics.addListener(clickListener);
    guiScene.addActor(graphics);

    contoller.addProcessor(new UndoRedoProcessor(this));
    contoller.addScene2D(mainScene);
    contoller.addScene2D(guiScene);
    contoller.addProcessor(this);
    
    cursorText = new Label("", skin);
    cursorText.setAlignment(Align.right);
    cursorText.setPosition(guiScene.getWidth() - 20, graphics.getHeight() + 20);
    guiScene.addActor(cursorText);

    mainScene.getCamera().position.set(-main.getWidth() * 0.5f, -graphics.getHeight() * 0.5f, 0.0f);
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
    properties.setEditActors(null);

    for(Actor model : layers.selectLayer.getCurrentGroup().getChildren())
      ((SelectObject)model).setSelection(false);
  }

  protected boolean altPress = false;
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
      case Input.Keys.ALT_LEFT:
      case Input.Keys.ALT_RIGHT:
        altPress = true;
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
        if (layers.selectLayer != null)
        {
          DelCommand command = new DelCommand();
          command.addModels(layers.selectLayer.getSelectedModels());
          command.addUpdater(propertiesUpdater);
          command.addUpdater(tree.panelUpdater);
          CommandController.instance.addCommand(command);
        }
        break;
      case Input.Keys.LEFT:
      case Input.Keys.RIGHT:
        if (addModel != null)
        {
          float deltaAngle = ctrlPress ? 5.0f : 45.0f;
          if (keycode == Input.Keys.RIGHT)
            deltaAngle *= -1.0f;

          addModel.rotateBy(deltaAngle);
          if (!ctrlPress)
            addModel.setRotation((float)Math.ceil(addModel.getRotation() / 45.0f) * 45.0f); 
        }
        break;
      case Input.Keys.UP:
      case Input.Keys.DOWN:
        if (addModel != null)
        {
          float deltaScale = ctrlPress ? 0.05f : 0.25f;
          if (keycode == Input.Keys.DOWN)
            deltaScale *= -1.0f;

          addModel.scaleBy(deltaScale);
        }
        else
        {
          if (keycode == Input.Keys.UP)
            UpDownElement(1);
          else
            UpDownElement(-1);
        }
        break;
      case Input.Keys.V:
        if (ctrlPress)
          paste();
        break;
      case Input.Keys.C:
        if (ctrlPress)
          copy();
        break;
      case Input.Keys.X:
        if (ctrlPress)
          cut();
        break;
      case Input.Keys.A:
        if (ctrlPress)
        {
          for(Actor model : layers.selectLayer.getCurrentGroup().getChildren())
          {
            ((SelectObject)model).setSelection(true);
          }
          propertiesUpdater.update();
          tree.panelUpdater.update();
        }
        break;
      case Input.Keys.G:
        if (ctrlPress)
        {
          group();
        }
        break;
      case Input.Keys.U:
        if (ctrlPress)
        {
          ungroup();
        }
        break;
      case Input.Keys.EQUALS:
      case Input.Keys.PLUS:
        if (gridSize < maxGridSize)
          gridSize *= 2.0f;
        break;
      case Input.Keys.MINUS:
        if (gridSize > minGridSize)
          gridSize /= 2.0f;
        break;
      case Input.Keys.H:
        showGrid = !showGrid;
        break;
      case Input.Keys.CONTROL_LEFT:
      case Input.Keys.CONTROL_RIGHT:
        ctrlPress = false;
        break;
      case Input.Keys.SHIFT_LEFT:
      case Input.Keys.SHIFT_RIGHT:
        shiftPress = false;
        break;
      case Input.Keys.ALT_LEFT:
      case Input.Keys.ALT_RIGHT:
        altPress = false;
        break;
    }
    return false;
  }

  @Override
  public boolean keyTyped(char character)
  {
    return false;
  }

  protected boolean brushMode = false;
  protected Vector2 brushStartPos = new Vector2();
  protected Array<Actor> newBrushModels = new Array<Actor>();
  protected Operation operation = Operation.NONE;
  protected Rectangle selectRectangle = new Rectangle();
  protected final float minDelta = 3.0f;
  protected int     touchPointer = -1;
  protected Vector2 touch = new Vector2();
  protected Vector2 newTouch = new Vector2();
  protected Vector2 delta = new Vector2();
  protected Vector2 mouseMove = new Vector2();
  protected Vector2 cursorPos = new Vector2();
  protected Vector2 buffer1 = new Vector2();
  protected Vector2 buffer2 = new Vector2();
  protected Vector2 guiBuffer = new Vector2();
  protected Command command = null;
  protected Actor   mainSelectModel = null;
  protected float   bufferX = 0, bufferY = 0;
  protected boolean translate = false, rotate = false, scale = false, move = false;
  protected int     button = -1;
  
  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button)
  {
    this.button = button;
    touchPointer = pointer;
    touch.set(mainScene.screenToSceneCoordinates(screenX, screenY));
    newTouch.set(touch);
    
    if (addModel != null)
    {
      brushMode = altPress;
      return true;
    }
    
    Array<Actor> selected = layers.selectLayer.getSelectedModels();
    for(int i = 0; i < selected.size; i++)
    {
      if (button == Buttons.LEFT)
      {
        if (operation == Operation.TRANSLATE)
        {
          mainSelectModel = selected.get(i);
          bufferX = mainSelectModel.getX();
          bufferY = mainSelectModel.getY();
          buffer1.set(touch);
          layers.selectLayer.getCurrentGroup().stageToLocalCoordinates(buffer1);
          buffer1.sub(bufferX, bufferY);
          translate = true;
          break;
        }
        else if (operation != Operation.NONE &&
                 operation != Operation.ROTATE)
        {
          scale = true;
          mainSelectModel = selected.get(i);
          bufferX = mainSelectModel.getScaleX();
          bufferY = mainSelectModel.getScaleY();
          layers.selectLayer.getCurrentGroup().stageToLocalCoordinates(newTouch);
          break;
        }
      }
      else if (button == Buttons.MIDDLE)
      {
        rotate = true;
        mainSelectModel = selected.get(i);
        bufferX = mainSelectModel.getRotation();
        break;
      }
    }
    
    if (translate)
    {
      TranslateCommand transCommand = new TranslateCommand();
      for(int i = 0; i < selected.size; i++)
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
      for(int i = 0; i < selected.size; i++)
      {
        rotateCommand.addActor(selected.get(i));
        rotateCommand.addUpdater(properties.panelUpdater);
      }
      command = rotateCommand;
      translate = false;
      scale = false;
      return true;
    }
    
    if (scale)
    {
      ScaleCommand scaleCommand = new ScaleCommand();
      for(int i = 0; i < selected.size; i++)
      {
        scaleCommand.addActor(selected.get(i));
        scaleCommand.addUpdater(properties.panelUpdater);
      }
      command = scaleCommand;
      translate = false;
      rotate = false;
      return true;
    }
    
    return true;
  }
  
  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button)
  {
    if (touchPointer != pointer)
      return false;

    this.button = -1;
    
    newTouch = mainScene.screenToSceneCoordinates(screenX, screenY);    
    delta.set(newTouch);
    delta.sub(touch);
    
    if (button == Buttons.LEFT && addModel == null)
    {
      Array<Actor> models = layers.selectLayer.getCurrentGroup().getChildren();

      if (move)
      {
        if (!ctrlPress)
          clearSelection();

        selectRectangle.set(touch.x, touch.y, delta.x, delta.y);
        if (selectRectangle.width < 0.0f)
        {
          selectRectangle.width = -selectRectangle.width;
          selectRectangle.x -= selectRectangle.width;
        }
        if (selectRectangle.height < 0.0f)
        {
          selectRectangle.height = -selectRectangle.height;
          selectRectangle.y -= selectRectangle.height;
        }
        
        for(int i = models.size - 1; i >= 0; i--)
        {
          SelectObject model = (SelectObject)models.get(i);
          if (selectRectangle.contains(model.getBound()))
            model.setSelection(true);
        }
      }
      else if (delta.len() < minDelta)
      {
        boolean emptySelect = layers.selectLayer.getSelectedModels().size <= 0;
        SelectObject selectObject = null;
        SelectObject unSelectObject = null;    
        
        for(int i = models.size - 1; i >= 0; i--)
        {
          SelectObject model = (SelectObject)models.get(i);
          if (model.getBound().contains(touch))
          {
            emptySelect = false;
            if (!ctrlPress && model instanceof EditGroup)
            {
              if (model.isSelected())
              {
                GoInGroupCommand command = new GoInGroupCommand();
                command.setGroup((EditGroup)model, layers.selectLayer.getCurrentGroup());
                command.addUpdater(tree.panelUpdater);
                CommandController.instance.addCommand(command);
                break;
              }
            }
            
            if (!model.isSelected())
            {
              selectObject = model;
              break;
            }
            else if (unSelectObject == null)
              unSelectObject = model;
          }
        }
        
        if (!ctrlPress)
          clearSelection();

        if (selectObject != null)
          selectObject.setSelection(true);
        else if (unSelectObject != null)
          unSelectObject.setSelection(false);

        if (emptySelect)
        {
          Group currentGroup = layers.selectLayer.getCurrentGroup();
          if (currentGroup instanceof EditGroup)
          {
            GoInGroupCommand command = new GoInGroupCommand();
            command.setGroup(currentGroup.getParent(), layers.selectLayer.getCurrentGroup());
            command.addUpdater(tree.panelUpdater);
            CommandController.instance.addCommand(command);
          }
        }
      }
      
      propertiesUpdater.update();
      tree.panelUpdater.update();
    }

    if (move)
    {
      move = false;
      return false;
    }

    if (addModel != null)
    {
      switch(button)
      {
        case Buttons.LEFT:
        {
          if (!brushMode)
          {
            EditModel newModel = addModel.copy();
            
            AddCommand command = new AddCommand();
            command.addModel(newModel);
            command.setGroup(layers.selectLayer.getCurrentGroup());
            command.addUpdater(tree.panelUpdater);
            CommandController.instance.addCommand(command);
          }
          break;
        }
        case Buttons.RIGHT:
        {
          clearAddModel();
          break;
        }
      }
      brushMode = false;
      newBrushModels.clear();
      return true;
    }

    if (translate)
    {
      float deltaX = mainSelectModel.getX() - bufferX;
      float deltaY = mainSelectModel.getY() - bufferY;
      
      if (deltaX != 0.0f || deltaY != 0.0f)
      {
        TranslateCommand transCommand = (TranslateCommand)command;
        transCommand.setDelta(deltaX, deltaY);
        CommandController.instance.addCommand(transCommand);
      }
    }
    else if (rotate)
    {
      float delta = mainSelectModel.getRotation() - bufferX;
      if (delta != 0)
      {
        RotateCommand rotateCommand = (RotateCommand)command;
        rotateCommand.setDelta(delta);
        CommandController.instance.addCommand(rotateCommand);
      }
    }
    else if (scale)
    {
      float deltaX = mainSelectModel.getScaleX() - bufferX;
      float deltaY = mainSelectModel.getScaleY() - bufferY;
      if (deltaX != 0.0f || deltaY != 0.0f)
      {
        ScaleCommand scaleCommand = (ScaleCommand)command;
        scaleCommand.setDelta(deltaX, deltaY);
        CommandController.instance.addCommand(scaleCommand);
      }
    }

    translate = false;
    rotate = false;
    scale = false;
    command = null;
    properties.updateProperties();
    mouseMoved(screenX, screenY);

    return true;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer)
  {
    if (touchPointer != pointer)
      return false;
    
    if (addModel != null)
    {
      mouseMoved(screenX, screenY);
      
      if (brushMode)
      {
        float x = addModel.getX();
        float y = addModel.getY();
        for(Actor model : newBrushModels)
        {
          if (model.getX() == x &&
              model.getY() == y)
            return true;
        }

        EditModel newModel = addModel.copy();
        newBrushModels.add(newModel);
        
        AddCommand command = new AddCommand();
        command.addModel(newModel);
        command.setGroup(layers.selectLayer.getCurrentGroup());
        command.addUpdater(tree.panelUpdater);
        CommandController.instance.addCommand(command);
        
        return true;
      }
    }
    
    if (translate)
    {
      newTouch.set(mainScene.screenToSceneCoordinates(screenX, screenY));
      layers.selectLayer.getCurrentGroup().stageToLocalCoordinates(newTouch);

      float x = newTouch.x - buffer1.x;
      float y = newTouch.y - buffer1.y;

      if (shiftPress)
      {
        x = (float)Math.ceil(x / gridSize) * gridSize;
        y = (float)Math.ceil(y / gridSize) * gridSize;
      }
      
      x = x - mainSelectModel.getX();
      y = y - mainSelectModel.getY();

      Array<Actor> models = layers.selectLayer.getSelectedModels();
      for(int i = 0; i < models.size; i++)
      {
        models.get(i).moveBy(x, y);
      }
      return true;
    }
    
    if (rotate)
    {
      delta.set(newTouch);
      newTouch.set(mainScene.screenToSceneCoordinates(screenX, screenY));
      Array<Actor> models = layers.selectLayer.getSelectedModels();

      buffer1.set(delta);
      mainSelectModel.stageToLocalCoordinates(buffer1);

      buffer2.set(newTouch);
      mainSelectModel.stageToLocalCoordinates(buffer2);
      
      float deltaAngle = (MathUtils.atan2(buffer2.y, buffer2.x) - MathUtils.atan2(buffer1.y, buffer1.x)) * MathUtils.radiansToDegrees;

      for(int i = 0; i < models.size; i++)
      {
        Actor model = models.get(i);

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
      layers.selectLayer.getCurrentGroup().stageToLocalCoordinates(newTouch);
      delta.sub(newTouch);
      
      float oldScaleX = mainSelectModel.getScaleX();
      float oldScaleY = mainSelectModel.getScaleY();

      buffer1.set(delta);
      buffer1.scl(2.0f);
      buffer1.x /= oldScaleX;
      buffer1.y /= oldScaleY;
      float scaleX = oldScaleX / mainSelectModel.getWidth();
      float scaleY = oldScaleY / mainSelectModel.getHeight();

      switch(operation)
      {
        case SCALE_MINUS_X:
          scaleX *= (mainSelectModel.getWidth() + buffer1.x);
          mainSelectModel.setScaleX(scaleX);
          break;
        case SCALE_PLUS_X:
          scaleX *= (mainSelectModel.getWidth() - buffer1.x);
          mainSelectModel.setScaleX(scaleX);
          break;
        case SCALE_MINUS_Y:
          scaleY *= (mainSelectModel.getHeight() + buffer1.y);
          mainSelectModel.setScaleY(scaleY);
          break;
        case SCALE_PLUS_Y:
          scaleY *= (mainSelectModel.getHeight() - buffer1.y);
          mainSelectModel.setScaleY(scaleY);
          break;
        case SCALE_X0Y0:
        case SCALE_X1Y1:
        case SCALE_X1Y0:
        case SCALE_X0Y1:
          if (operation == Operation.SCALE_X0Y0 ||
              operation == Operation.SCALE_X0Y1)
          {
            scaleX *= (mainSelectModel.getWidth() + buffer1.x);
          }
          else
          {
            scaleX *= (mainSelectModel.getWidth() - buffer1.x);
          }
          
          if (operation == Operation.SCALE_X0Y0 ||
              operation == Operation.SCALE_X1Y0)
          {
            scaleY *= (mainSelectModel.getHeight() + buffer1.y);
          }
          else
          {
            scaleY *= (mainSelectModel.getHeight() - buffer1.y);
          }

          if (!shiftPress)
          {
            mainSelectModel.setScaleX(scaleX);
            mainSelectModel.setScaleY(scaleY);
          }
          else
            mainSelectModel.setScale((scaleX + scaleY) * 0.5f);

          break;
        default:
          break;
      }

      scaleX = mainSelectModel.getScaleX() - oldScaleX;
      scaleY = mainSelectModel.getScaleY() - oldScaleY;
      
      Array<Actor> models = layers.selectLayer.getSelectedModels();
      models.removeValue(mainSelectModel, true);
      for(int i = 0; i < models.size; i++)
      {
        Actor model = models.get(i);
        model.scaleBy(scaleX, scaleY);
      }
      return true;
    }

    delta.set(newTouch);
    newTouch.set(mainScene.screenToSceneCoordinates(screenX, screenY));
    delta.sub(newTouch);
    
    if (delta.len() > minDelta)
      move = true;
    
    if (button == Buttons.RIGHT)
    {
      mainScene.getCamera().translate(delta.x, delta.y, 0);
      newTouch.add(delta);
    }

    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY)
  {
    mouseMove.set(mainScene.screenToSceneCoordinates(screenX, screenY));
    
    cursorPos.set(mouseMove);
    if (addModel != null)
    {
      float width = addModel.getBound().getWidth();
      float height = addModel.getBound().getHeight();
      if (brushMode)
      {
        cursorPos.x = brushStartPos.x + (float)Math.round((cursorPos.x - brushStartPos.x) / width) * width;
        cursorPos.y = brushStartPos.y + (float)Math.round((cursorPos.y - brushStartPos.y) / height) * height;
      }
      else if (shiftPress || altPress)
      {
        cursorPos.x = (float)Math.round(cursorPos.x / gridSize) * gridSize;
        cursorPos.y = (float)Math.round(cursorPos.y / gridSize) * gridSize;
        if (altPress)
        {
          brushStartPos.set(cursorPos);
        }
      }
    }
    
    OrthographicCamera camera = (OrthographicCamera)mainScene.getCamera();
    cursorText.setText(String.format(Locale.ENGLISH, "x: %.2f   y: %.2f   z: %.1f", cursorPos.x, cursorPos.y, camera.zoom));

    if (addModel != null)
    {
      addModel.setPosition(cursorPos);
      return true;
    }

    if (Gdx.app.getType() == ApplicationType.Desktop && command == null)
    {      
      Array<Actor> selected = layers.selectLayer.getSelectedModels();
      
      if (selected.size <= 0)
      {
        Gdx.input.setCursorImage(null, 0, 0);
        return false;
      }
      
      for(int i = 0; i < selected.size; i++)
      {
        SelectObject model = (SelectObject)selected.get(i);
        BoundingBox bb = model.getBoundingBox();
        operation = bb.getOperationType(mouseMove);

        guiBuffer.set(guiScene.screenToSceneCoordinates(screenX, screenY));
        
        if (guiScene.hit(guiBuffer.x, guiBuffer.y, false) != null)
          operation = Operation.NONE;

        switch(operation)
        {
          case SCALE_X0Y0:
          case SCALE_X1Y1:
            Gdx.input.setCursorImage(drCursor, 16, 16);
            return false;
          case SCALE_X1Y0:
          case SCALE_X0Y1:
            Gdx.input.setCursorImage(dlCursor, 16, 16);
            return false;
          case SCALE_PLUS_X:
          case SCALE_MINUS_X:
            Gdx.input.setCursorImage(hCursor, 16, 16);
            return false;
          case SCALE_PLUS_Y:
          case SCALE_MINUS_Y:
            Gdx.input.setCursorImage(vCursor, 16, 16);
            return false;
          case TRANSLATE:
            Gdx.input.setCursorImage(moveCursor, 16, 16);
            return false;
          default:
            break;
        }
      }
    }
    Gdx.input.setCursorImage(null, 0, 0);
    return false;
  }

  @Override
  public boolean scrolled(int amount)
  {
    return false;
  }

  public void setCurrentGroup(Group group)
  {
    clearSelection();
    layers.selectLayer.setCurrentGroup(group);
    tree.setGroup(group);
  }
  
  @Override
  public void dispose()
  {
    super.dispose();
    CommandController.instance.clear();
  }

  public void loadLevel(Element root) throws IOException
  {
    clearAddModel();
    clearSelection();
    mainScene.clear();
    Array<Element> elementsLayer = root.getChildrenByName("layer");
    for(Element element : elementsLayer)
    {
      Layer layer = new Layer();
      layer.load(element);
      mainScene.addActor(layer);
    }
    layers.rebuildUpdater.update();
    tree.setGroup(layers.selectLayer.getCurrentGroup());
  }

  public void saveLevel(XmlWriter xml) throws IOException
  {
    for(Actor actor : mainScene.getActors())
    {
      if (actor instanceof Layer)
        ((Layer)actor).save(xml);
    }
  }
  
  public void group()
  {
    if (layers.selectLayer.getSelectedModels().size <= 0)
      return;

    EditGroup group = new EditGroup("group");
    group.setSelection(true);
    AddGroupCommand command = new AddGroupCommand();
    command.addModels(layers.selectLayer.getSelectedModels());
    command.setGroup(group);
    command.addUpdater(propertiesUpdater);
    command.addUpdater(tree.panelUpdater);
    CommandController.instance.addCommand(command);
  }
  
  public void ungroup()
  {
    Command command = null;
    if (layers.selectLayer.getSelectedModels().size <= 0)
    {
      if (layers.selectLayer.getCurrentGroup() instanceof Layer)
        return;
      RemoveGroupCommand removeCommand = new RemoveGroupCommand();
      removeCommand.setGroup(layers.selectLayer.getCurrentGroup());
      command = removeCommand;
      setCurrentGroup(layers.selectLayer.getCurrentGroup().getParent());
    }
    else
    {
      GroupCommand groupCommand = new GroupCommand();
      for (Actor actor : layers.selectLayer.getSelectedModels())
      {
        if (actor instanceof EditGroup)
        {
          RemoveGroupCommand removeCommand = new RemoveGroupCommand();
          removeCommand.setGroup((EditGroup)actor);
          groupCommand.addCommand(removeCommand);
        }
      }
      
      if (groupCommand.getCommands().size <= 0)
        return;
      
      command = groupCommand;
    }
    command.addUpdater(propertiesUpdater);
    command.addUpdater(tree.panelUpdater);
    CommandController.instance.addCommand(command);
  }
  
  protected Vector2 bufferPosition = new Vector2();
  protected Vector2 bufferScale = new Vector2();
  
  public boolean copy()
  {
    if (layers.selectLayer == null)
      return false;

    Array<Actor> selectedModels = layers.selectLayer.getSelectedModels();
    
    if (selectedModels.size <= 0)
      return false;

    copyModels.clear();
    
    for(int i = 0; i < selectedModels.size; i++)
    {
      Actor model = selectedModels.get(i);
      Actor copyModel = null;
      if (model instanceof EditModel)
      {
        copyModel = ((EditModel)model).copy();
      }
      else if (model instanceof EditGroup)
      {
        copyModel = ((EditGroup)model).copy();
      }
      
      Group group = layers.selectLayer.getCurrentGroup();
      if (!(group instanceof Layer))
      {
        bufferPosition.set(copyModel.getX(), copyModel.getY());
        group.localToStageCoordinates(bufferPosition);
        
        bufferScale.set(copyModel.getX() + 1.0f, copyModel.getY());
        group.localToStageCoordinates(bufferScale);
        bufferScale.sub(bufferPosition);
        float angle = MathUtils.atan2(bufferScale.y, bufferScale.x) * MathUtils.radiansToDegrees;
        float scaleX = bufferScale.len();

        bufferScale.set(copyModel.getX(), copyModel.getY() + 1.0f);
        group.localToStageCoordinates(bufferScale);
        bufferScale.sub(bufferPosition);
        float scaleY = bufferScale.len();

        copyModel.setPosition(bufferPosition.x, bufferPosition.y);
        copyModel.rotateBy(angle);
        copyModel.setScale(model.getScaleX() * scaleX, model.getScaleY() * scaleY);
      }

      copyModel.setZIndex(group.getChildren().indexOf(model, true) + 1);
      copyModels.add(copyModel);
    }
    return true;
  }
  
  public void cut()
  {
    if (copy())
    {
      DelCommand command = new DelCommand();
      command.addModels(layers.selectLayer.getSelectedModels());
      command.addUpdater(propertiesUpdater);
      command.addUpdater(tree.panelUpdater);
      CommandController.instance.addCommand(command);
    }
  }
  
  public void paste()
  {
    if (layers.selectLayer == null || copyModels.size <= 0)
      return;
    
    clearSelection();

    Group group = layers.selectLayer.getCurrentGroup();
    AddCommand command = new AddCommand();
    command.setGroup(group);
    for(int i = copyModels.size - 1; i >= 0; i--)
    {
      Actor model = copyModels.get(i);
      if (model instanceof EditModel)
      {
        EditModel newModel = ((EditModel)model).copy();
        newModel.setSelection(true);
        command.addModel(newModel, model.getZIndex());
      }
      else if (model instanceof EditGroup)
      {
        EditGroup newGroup = ((EditGroup)model).copy();
        newGroup.setSelection(true);
        command.addModel(newGroup, model.getZIndex());
      }
    }
    command.addUpdater(tree.panelUpdater);
    CommandController.instance.addCommand(command);
  }

  public void UpDownElement(int delatIndex)
  {
    Array<Actor> selectedModels = layers.selectLayer.getSelectedModels();
    
    if (selectedModels.size <= 0 || delatIndex == 0)
      return;
    
    GroupCommand groupCommand = new GroupCommand();
    if (delatIndex < 0)
    {
      for (int i = 0; i < selectedModels.size; i++)
      {
        UpDownCommand command = new UpDownCommand();
        command.setDeltaIndex(delatIndex);
        command.setModel(selectedModels.get(i));
        groupCommand.addCommand(command);
      }
    }
    else
    {
      for (int i = selectedModels.size - 1; i >= 0; --i)
      {
        UpDownCommand command = new UpDownCommand();
        command.setDeltaIndex(delatIndex);
        command.setModel(selectedModels.get(i));
        groupCommand.addCommand(command);
      }
    }
    groupCommand.addUpdater(tree.panelUpdater);
    CommandController.instance.addCommand(groupCommand);
  }
  
  public void UpDownLayer(int delatIndex)
  {
    if (layers.selectLayer == null || delatIndex == 0)
      return;

    UpDownCommand command = new UpDownCommand();
    command.setDeltaIndex(delatIndex);
    command.setModel(layers.selectLayer);
    command.addUpdater(layers.rebuildUpdater);
    CommandController.instance.addCommand(command);
  }
  
  @Override
  public void render(float deltaTime)
  {
    Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    //draw grid
    if (showGrid)
    {
      shapeRenderer.setProjectionMatrix(mainScene.getBatch().getProjectionMatrix());
  
      OrthographicCamera camera = (OrthographicCamera)mainScene.getCamera();
      float minX = camera.position.x - width * 0.5f * camera.zoom;
      float maxX = camera.position.x + width * 0.5f * camera.zoom;
      float minY = camera.position.y - height * 0.5f * camera.zoom;
      float maxY = camera.position.y + height * 0.5f * camera.zoom;

      shapeRenderer.setColor(0.35f, 0.35f, 0.35f, 0.5f);
      shapeRenderer.begin(ShapeType.Line);
      float start = (float)Math.ceil(minX / gridSize) * gridSize; 
      for(float i = start; i < maxX; i += gridSize)
        shapeRenderer.line(i, minY, i, maxY);
      
      start = (float)Math.ceil(minY / gridSize) * gridSize; 
      for(float i = start; i < maxY; i += gridSize)
        shapeRenderer.line(minX, i, maxX, i);
      shapeRenderer.end();
    }
    
    //draw scene
    update(deltaTime);
    draw(deltaTime);
    
    if (command instanceof RotateCommand)
    {
      shapeRenderer.begin(ShapeType.Filled);
      shapeRenderer.setColor(0.8f, 0.8f, 0.8f, 1);
      shapeRenderer.circle(newTouch.x, newTouch.y, 10);
      shapeRenderer.end();

      shapeRenderer.begin(ShapeType.Line);
      shapeRenderer.line(mainSelectModel.localToStageCoordinates(new Vector2(0, 0)), newTouch);
      shapeRenderer.end();
    }
    
    //draw cusor selection
    if (button != Buttons.LEFT || command != null || addModel != null)
      return;
    
    shapeRenderer.begin(ShapeType.Line);
    shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1);
    shapeRenderer.rect(touch.x, touch.y, newTouch.x - touch.x, newTouch.y - touch.y);
    shapeRenderer.end();
  }
}
