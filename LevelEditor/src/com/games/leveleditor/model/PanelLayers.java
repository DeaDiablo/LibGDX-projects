package com.games.leveleditor.model;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
import com.shellGDX.manager.ResourceManager;
import com.shellGDX.model2D.Scene2D;

public class PanelLayers extends PanelScroll
{
  public Layer       selectLayer = null;
  public Tree        tree        = null;
  
  protected TextButton addButton    = null;
  protected TextButton removeButton = null;

  protected Skin skin = null;
  protected Scene2D scene = null;
  protected TextureRegionDrawable visibleDrawable = new TextureRegionDrawable(ResourceManager.instance.getTextureRegion("data/editor/visible.png"));
  
  public final Updater rebuildUpdater = new Updater()
  {
    @Override
    public void update()
    {      
      rebuildTree();

      if (tree.getNodes().size > 0)
      {
        Node node = tree.getNodes().get(0);
        tree.getSelection().add(node);
        selectLayer = (Layer)node.getObject();
      }
    }
  };
  
  public PanelLayers(String title, final Skin skin, final Scene2D scene)
  {
    super(title, skin);
    content.align(Align.bottom);
    this.scene = scene;
    this.skin = skin;
    
    tree = new Tree(skin);
    tree.getSelection().setMultiple(false);
    tree.getSelection().setRequired(true);
    
    tree.addListener(new ChangeListener()
    {
      @Override
      public void changed(ChangeEvent event, Actor actor)
      {
        Node node = tree.getSelection().getLastSelected();
        selectLayer = (Layer)node.getObject();
      }
    });
    
    content.add(tree).fill().expand();
    
    rebuildUpdater.update();

    content.row();

    Table buttons = new Table(skin);
    buttons.defaults().spaceBottom(10);
    buttons.defaults().space(10);
    
    addButton = new TextButton("add", skin);
    removeButton = new TextButton("remove", skin);
    
    addButton.addListener(new ClickListener()
    {
      @Override
      public void clicked(InputEvent event, float x, float y)
      {
        getStage().unfocusAll();
        
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
      }
    });
    
    removeButton.addListener(new ClickListener()
    {
      @Override
      public void clicked(InputEvent event, float x, float y)
      {
        getStage().unfocusAll();
        
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
              selectLayer(tree.getNodes().size - 1);
            }
            execute = !execute;
          }
        };
        
        command.addUpdater(removeUpdater);
        CommandController.instance.addCommand(command);
      }
    });
    
    buttons.add(addButton);
    buttons.add(removeButton);
    
    row();
    add(buttons);
    
    setSize(450, 350);
  }
  
  protected void rebuildTree()
  {
    tree.clearChildren();
    for(Actor actor : scene.getActors())
    {
      if (actor instanceof Layer)
        addLayer((Layer)actor);
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
        getStage().unfocusAll();
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
        if (c == '\r')
        {
          getStage().unfocusAll();
          return;
        }
        
        layer.setName(textField.getText());
      }
    });
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
