package com.games.leveleditor.model;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Selection;
import com.badlogic.gdx.utils.Array;
import com.games.leveleditor.controller.Updater;
import com.games.leveleditor.screen.MainScreen;

public class PanelTree extends PanelScroll
{
  public    Tree  tree = null;
  protected Layer layer = null;
  protected Skin  skin = null;
  protected MainScreen screen = null;
  protected boolean updateNodes = false;
  
  public Updater panelUpdater = new Updater()
  {
    @Override
    public void update()
    {
      //rebuildTree();
    }
  };
  
  public PanelTree(String title, Skin skin, MainScreen mainScreen)
  {
    super(title, skin);
    this.skin = skin;
    align(Align.top);
    screen = mainScreen;
    
    tree = new Tree(skin);
    content.add(tree).fill().expand();
    
    tree.addListener(new ChangeListener()
    {
      @Override
      public void changed(ChangeEvent event, Actor actor)
      {
        if (updateNodes)
          return;
        
        screen.clearSelection();
        Selection<Node> nodes = tree.getSelection();
        for(Node node : nodes)
        {
          ((EditModel)node.getObject()).setSelection(true);
        }
        screen.selectionUpdater.update();
      }
    });
    
    setSize(450, 400);
  }

  public void addNode(EditModel newModel)
  {
    Node node = new Node(new Label(newModel.getName(), skin));
    node.setObject(newModel);
    tree.add(node);
  }
  
  protected void updateNodes(Array<Node> nodes, Selection<Node> selectionNodes)
  {
    for (int i = 0; i < nodes.size; i++)
    {
      Node node = nodes.get(i);
      if (((EditModel)node.getObject()).isSelected())
        selectionNodes.add(node);
    }
  }
  
  public void setLayer(Layer layer)
  {
    this.layer = layer;
    rebuildTree();
  }
  
  protected void rebuildTree()
  {
    updateNodes = true;
    
    tree.clearChildren();
    if (layer == null)
      return;
    
    Array<Node> nodes = getLayerNodes(layer);
    for (int i = 0; i < nodes.size; i++)
      tree.add(nodes.get(i));
    
    updateNodes = false;
  }
  
  protected Array<Node> getLayerNodes(Layer layer)
  {
    Array<Node> nodes = new Array<Node>();
    for (Actor actor : layer.getChildren())
      nodes.add(getActorNode(actor));
    return nodes;
  }

  protected Node getActorNode(Actor actor)
  {
    Node node = new Node(new Label(actor.getName(), skin));
    node.setObject(actor);
    if (actor instanceof Group)
    {
      Group group = (Group)actor;
      for(Actor child : group.getChildren())
        node.add(getActorNode(child));
    }
    return node;
  }
}
