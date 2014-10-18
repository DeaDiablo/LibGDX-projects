package com.games.leveleditor.model;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.games.leveleditor.controller.AddLayerCommand;
import com.games.leveleditor.controller.CommandController;
import com.games.leveleditor.controller.DelLayerCommand;
import com.games.leveleditor.controller.Updater;
import com.games.leveleditor.screen.MainScreen;
import com.shellGDX.manager.ResourceManager;
import com.shellGDX.model2D.Scene2D;

public class PanelLayers extends PanelScroll
{
  public Layer       selectLayer = null;
  public Tree        tree        = null;

  protected ImageButton addButton    = null;
  protected ImageButton removeButton = null;
  protected ImageButton upButton = null;
  protected ImageButton downButton = null;

  protected Skin skin = null;
  protected Scene2D scene = null;
  protected TextureRegionDrawable visibleDrawable = new TextureRegionDrawable(ResourceManager.instance.getTextureRegion("data/editor/visible.png"));
  protected TextureRegionDrawable plusDrawable = new TextureRegionDrawable(ResourceManager.instance.getTextureRegion("data/editor/plus.png"));
  protected TextureRegionDrawable minusDrawable = new TextureRegionDrawable(ResourceManager.instance.getTextureRegion("data/editor/minus.png"));
  protected TextureRegionDrawable upDrawable = new TextureRegionDrawable(ResourceManager.instance.getTextureRegion("data/editor/up.png"));
  protected TextureRegionDrawable downDrawable = new TextureRegionDrawable(ResourceManager.instance.getTextureRegion("data/editor/down.png"));
  
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
  
  public final Updater rebuildUpdater = new Updater()
  {
    @Override
    public void update()
    {      
      rebuildTree();
      
      if (selectLayer != null)
      {
        tree.getSelection().clear();
        for (Node node : tree.getNodes())
        {
          if (node.getObject() == selectLayer)
          {
            tree.getSelection().add(node);
            return;
          }
        }
      }

      selectLayer(0);
    }
  };
  
  public PanelLayers(String title, final Skin skin, final Scene2D scene, final MainScreen screen)
  {
    super(title, skin);
    content.align(Align.bottom);
    this.scene = scene;
    this.skin = skin;
    
    scroll.getListeners().removeIndex(0);
    
    tree = new Tree(skin);
    tree.getSelection().setMultiple(false);
    tree.getSelection().setRequired(true);
    content.add(tree).fill().expand();
    
    rebuildUpdater.update();

    content.row();

    Table buttons = new Table(skin);
    buttons.defaults().spaceBottom(5);
    buttons.defaults().space(10);

    Table layerButtons = new Table(skin);
    layerButtons.defaults().spaceBottom(5);
    layerButtons.defaults().space(10);
    
    final ButtonStyle styleButton = skin.get(ButtonStyle.class);

    ImageButtonStyle style = new ImageButtonStyle(styleButton.up, styleButton.down, null,
                                                  plusDrawable, plusDrawable, null);
    
    addButton = new ImageButton(style);
    
    style = new ImageButtonStyle(styleButton.up, styleButton.down, null,
                                 minusDrawable, minusDrawable, null);
    removeButton = new ImageButton(style);
    
    addButton.addListener(new ClickListener()
    {
      @Override
      public void clicked(InputEvent event, float x, float y)
      {
        getStage().setKeyboardFocus(null);
        
        int indexTemp = 0;
        Node lastLayer = tree.getSelection().getLastSelected();
        if (lastLayer != null)
          indexTemp = tree.getNodes().indexOf(lastLayer, true);
        
        final int index = indexTemp;
        
        final Layer layer = new Layer("layer");
        
        AddLayerCommand command = new AddLayerCommand();
        command.setScene(scene);
        command.setLayer(layer);
        
        final Updater addUpdater = new Updater()
        {
          protected boolean execute = true;
          @Override
          public void update()
          {
            if (execute)
            {
              addLayer(layer);
              selectLayer(tree.getNodes().size - 1);
            }
            else
            {
              removeLayer(layer);
              selectLayer(index);
            }
            execute = !execute;
          }
        };
        
        command.addUpdater(addUpdater);
        CommandController.instance.addCommand(command);
        getStage().setScrollFocus(content);
      }
    });
    
    removeButton.addListener(new ClickListener()
    {
      @Override
      public void clicked(InputEvent event, float x, float y)
      {
        getStage().setKeyboardFocus(null);
        
        if (tree.getNodes().size <= 1)
          return;
        
        Node lastLayer = tree.getSelection().getLastSelected();
        if (lastLayer == null)
          return;
        
        final int layerIndex = tree.getNodes().indexOf(lastLayer, true);
        
        final Layer layer = (Layer)lastLayer.getObject();
        
        DelLayerCommand command = new DelLayerCommand();
        command.setScene(scene);
        command.setLayer(layer);

        final Updater removeUpdater = new Updater()
        {
          protected boolean execute = true;
          @Override
          public void update()
          {
            if (execute)
            {
              removeLayer(layer);
              if (layerIndex < tree.getNodes().size)
                selectLayer(layerIndex);
              else
                selectLayer(tree.getNodes().size - 1);
            }
            else
            {
              insertLayer(layer, layerIndex);
              selectLayer(layerIndex);
            }
            execute = !execute;
          }
        };
        
        command.addUpdater(removeUpdater);
        CommandController.instance.addCommand(command);
      }
    });
    
    layerButtons.add(addButton).size(40, 40);
    layerButtons.add(removeButton).size(40, 40);
    buttons.add(layerButtons).space(100);

    Table updownButtons = new Table(skin);
    updownButtons.defaults().spaceBottom(5);
    updownButtons.defaults().space(10);

    style = new ImageButtonStyle(styleButton.up, styleButton.down, null,
                                 upDrawable, upDrawable, null);
    upButton = new ImageButton(style);
    upButton.addListener(new ClickListener()
    {
      @Override
      public void clicked(InputEvent event, float x, float y)
      {
        screen.UpDownLayer(-1);
      }
    });

    style = new ImageButtonStyle(styleButton.up, styleButton.down, null,
                                 downDrawable, downDrawable, null);
    downButton = new ImageButton(style);
    downButton.addListener(new ClickListener()
    {
      @Override
      public void clicked(InputEvent event, float x, float y)
      {
        screen.UpDownLayer(1);
      }
    });
    
    
    updownButtons.add(upButton).size(40, 40);
    updownButtons.add(downButton).size(40, 40);
    buttons.add(updownButtons);
    
    row();
    add(buttons);
    
    setSize(450, 350);

    tree.addListener(new ChangeListener()
    {
      @Override
      public void changed(ChangeEvent event, Actor actor)
      {
        Node node = tree.getSelection().getLastSelected();
        Layer layer = (Layer)node.getObject();
        if (layer != selectLayer)
        {
          screen.clearSelection();
          selectLayer = layer;
          screen.getTree().setGroup(selectLayer);
        }
      }
    });
    
  }
  
  protected void rebuildTree()
  {
    tree.clearChildren();
    for(Actor actor : scene.getActors())
    {
      if (actor instanceof Layer)
      {
        Layer layer = (Layer)actor;
        addLayer(layer);
      }
    }
  }

  protected void insertLayer(final Layer layer, final int index)
  {
    Table item = new Table(skin);
    
    final ButtonStyle styleButton = skin.get(ButtonStyle.class);
    ImageButtonStyle style = new ImageButtonStyle(styleButton.up, styleButton.down, styleButton.down,
                                                  visibleDrawable, visibleDrawable, visibleDrawable);
    ImageButton showButton = new ImageButton(style);
    showButton.setChecked(!layer.isVisible());
    showButton.addListener(new ClickListener()
    {
      @Override
      public void clicked(InputEvent event, float x, float y)
      {
        getStage().setKeyboardFocus(null);
        layer.setVisible(!layer.isVisible());
      }
    });
    item.add(showButton).space(20);
    
    TextField name = new TextField(layer.getName(), skin);
    name.setTextFieldListener(new TextFieldListener()
    {
      @Override
      public void keyTyped(TextField textField, char c)
      {
        if (c == '\r' || c == '\n')
        {
          getStage().setKeyboardFocus(null);
          return;
        }
        
        layer.setName(textField.getText());
      }
    });
    name.addListener(tabListener);
    item.add(name).width(300);
    
    Node node = new Node(item);
    node.setObject(layer);

    if (index < 0)
      tree.add(node);
    else
      tree.insert(index, node);
  }
  
  protected void addLayer(final Layer layer)
  {
    insertLayer(layer, tree.getNodes().size);
  }
  
  protected void removeLayer(Layer layer)
  {
    if (tree.getNodes().size <= 1)
      return;

    for (Node node : tree.getNodes())
    {
      if (node.getObject() == layer)
      {
        tree.remove(node);
        break;
      }
    }
  }
  
  protected void selectLayer(int num)
  {
    tree.getSelection().clear();
    Node node = tree.getNodes().get(num);
    if (node != null)
    {
      tree.getSelection().add(node);
      selectLayer = (Layer)node.getObject();
    }
  }
}
