package com.games.leveleditor.model;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Selection;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.games.leveleditor.controller.CommandController;
import com.games.leveleditor.controller.GoInGroupCommand;
import com.games.leveleditor.controller.Updater;
import com.games.leveleditor.screen.MainScreen;
import com.shellGDX.controller.TimeController;
import com.shellGDX.manager.ResourceManager;

public class PanelTree extends PanelScroll
{
  public    Tree  tree = null;
  protected Group group = null;
  protected Skin  skin = null;
  protected MainScreen screen = null;
  protected boolean updateNodes = false;
  protected ImageButton groupButton = null;
  protected ImageButton ungroupButton = null;
  protected ImageButton upButton = null;
  protected ImageButton downButton = null;

  protected TextureRegionDrawable groupDrawable = new TextureRegionDrawable(ResourceManager.instance.getTextureRegion("data/editor/group.png"));
  protected TextureRegionDrawable ungroupDrawable = new TextureRegionDrawable(ResourceManager.instance.getTextureRegion("data/editor/ungroup.png"));
  protected TextureRegionDrawable upDrawable = new TextureRegionDrawable(ResourceManager.instance.getTextureRegion("data/editor/up.png"));
  protected TextureRegionDrawable downDrawable = new TextureRegionDrawable(ResourceManager.instance.getTextureRegion("data/editor/down.png"));
  
  public Updater panelUpdater = new Updater()
  {
    @Override
    public void update()
    {
      rebuildTree();
    }
  };
  
  public PanelTree(Skin skin, MainScreen mainScreen)
  {
    super("", skin);
    this.skin = skin;
    align(Align.top);
    screen = mainScreen;
    
    tree = new Tree(skin);
    content.add(tree).fill().expand();
    
    tree.addListener(new ClickListener(Input.Buttons.LEFT)
    {
      protected Node nodeClick = null;
      protected final float timeDelta = 0.5f;
      protected float timeClick = 0.0f;
      
      @Override
      public void clicked(InputEvent event, float x, float y)
      {
        if (updateNodes)
          return;
        
        updateButtons();
        
        Node groupNode = tree.getNodeAt(y);
        if (groupNode != null)
        {
          if (nodeClick == groupNode &&
              TimeController.globalTime - timeClick < timeDelta)
          {
            Object object = groupNode.getObject();
            if (object == null || object instanceof Group)
            {
              GoInGroupCommand command = new GoInGroupCommand();
              if (object == null)
                command.setGroup(group.getParent(), screen.getLayers().selectLayer.getCurrentGroup());
              else
                command.setGroup((Group)object, screen.getLayers().selectLayer.getCurrentGroup());
              command.addUpdater(panelUpdater);
              CommandController.instance.addCommand(command);
              return;
            }
          }

          nodeClick = groupNode;
          timeClick = TimeController.globalTime;
        }
        
        screen.clearSelection();
        Selection<Node> nodes = tree.getSelection();
        for(Node node : nodes)
        {
          Object object = node.getObject();
          if (object != null)
            ((SelectObject)node.getObject()).setSelection(true);
        }
        screen.propertiesUpdater.update();
      }
    });
    
    Table buttons = new Table(skin);
    buttons.defaults().spaceBottom(5);
    buttons.defaults().space(10);
    
    Table groupButtons = new Table(skin);
    groupButtons.defaults().spaceBottom(5);
    groupButtons.defaults().space(10);
    
    final ButtonStyle styleButton = skin.get(ButtonStyle.class);

    ImageButtonStyle style = new ImageButtonStyle(styleButton.up, styleButton.down, null,
                                                  groupDrawable, groupDrawable, null);
    
    groupButton = new ImageButton(style);
    groupButton.addListener(new ClickListener()
    {
      @Override
      public void clicked(InputEvent event, float x, float y)
      {
        screen.group();
      }
    });
    
    style = new ImageButtonStyle(styleButton.up, styleButton.down, null,
                                 ungroupDrawable, ungroupDrawable, null);
    
    ungroupButton = new ImageButton(style);
    ungroupButton.addListener(new ClickListener()
    {
      @Override
      public void clicked(InputEvent event, float x, float y)
      {
        screen.ungroup();
      }
    });
 
    groupButtons.add(groupButton).size(40, 40);;
    groupButtons.add(ungroupButton).size(40, 40);
    buttons.add(groupButtons).space(100);
    
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
        screen.UpDownElement(-1);
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
        screen.UpDownElement(1);
      }
    });
    
    updownButtons.add(upButton).size(40, 40);;
    updownButtons.add(downButton).size(40, 40);;
    buttons.add(updownButtons);
    
    row();
    add(buttons);
    
    setSize(450, 400);
  }

  public void addNode(EditModel newModel)
  {
    Node node = new Node(new Label(newModel.getName(), skin));
    node.setObject(newModel);
    tree.add(node);
  }
  
  public void setGroup(Group group)
  {
    this.group = group;
    rebuildTree();
  }
  
  protected void rebuildTree()
  {
    updateNodes = true;
    
    tree.getSelection().clear();
    tree.clearChildren();
    if (group == null)
      return;
    
    if (group instanceof EditGroup)
    {
      Node node = new Node(new Label("..", skin));
      node.setObject(null);
      tree.add(node);
    }
    
    for (Actor actor : group.getChildren())
    {
      Node node = new Node(new Label(actor.getName(), skin));
      node.setObject(actor);
      if (((SelectObject)actor).isSelected())
        tree.getSelection().add(node);
      tree.add(node);
    }

    updateButtons();
    
    updateNodes = false;
  }
  
  @Override
  public void act(float delta)
  {
    if (group != null)
      setTitle(group.getName());
    super.act(delta);
  }
  
  public void updateButtons()
  {
    boolean selectionEmpty = tree.getSelection().isEmpty();

    Color color = selectionEmpty ? disableColor : enableColor;
    
    groupButton.setColor(color);
    upButton.setColor(color);
    downButton.setColor(color);
    
    groupButton.setDisabled(selectionEmpty);
    upButton.setDisabled(selectionEmpty);
    downButton.setDisabled(selectionEmpty);
    
    color = group instanceof Layer ? disableColor : enableColor;
    
    ungroupButton.setDisabled(group instanceof Layer);
    ungroupButton.setColor(color);
    
    if (!selectionEmpty)
    {
      Selection<Node> nodes = tree.getSelection();
      for(Node node : nodes)
      {
        Object object = node.getObject();
        if (object instanceof Group)
        {
          ungroupButton.setDisabled(false);
          break;
        }
      }
    }
  }
}
