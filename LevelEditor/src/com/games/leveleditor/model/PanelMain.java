package com.games.leveleditor.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.games.leveleditor.LevelEditor;
import com.games.leveleditor.screen.MainScreen;
import com.shellGDX.GameInstance;
import com.shellGDX.GameLog;
import com.shellGDX.manager.ResourceManager;

public class PanelMain extends PanelScroll
{
  protected class FilterLevelEditor extends FileFilter
  {
    @Override
    public boolean accept(File f)
    {
      if (f.isDirectory())
        return true;
      String s = f.getName();
      int i = s.lastIndexOf('.');

      if (i > 0 && i < s.length() - 1)
        if (s.substring(i + 1).toLowerCase().equals("xml"))
          return true;

      return false;
    }

    @Override
    public String getDescription()
    {
      return "(*.xml) - File of LevelEditor2D";
    }
  }
  
  public ImageButton newButton  = null;
  public ImageButton openButton = null;
  public ImageButton saveButton = null;
  public ImageButton saveAsButton = null;
  public ImageButton cutButton = null;
  public ImageButton copyButton = null;
  public ImageButton pasteButton = null;
  public ImageButton exitButton = null;
  
  protected String       fileName   = "";
  protected JFileChooser chooser    = null;

  protected TextureRegionDrawable newDrawable = new TextureRegionDrawable(ResourceManager.instance.getTextureRegion("data/editor/new.png"));
  protected TextureRegionDrawable openDrawable = new TextureRegionDrawable(ResourceManager.instance.getTextureRegion("data/editor/open.png"));
  protected TextureRegionDrawable saveDrawable = new TextureRegionDrawable(ResourceManager.instance.getTextureRegion("data/editor/save.png"));
  protected TextureRegionDrawable saveAsDrawable = new TextureRegionDrawable(ResourceManager.instance.getTextureRegion("data/editor/saveAs.png"));
  protected TextureRegionDrawable cutDrawable = new TextureRegionDrawable(ResourceManager.instance.getTextureRegion("data/editor/cut.png"));
  protected TextureRegionDrawable copyDrawable = new TextureRegionDrawable(ResourceManager.instance.getTextureRegion("data/editor/copy.png"));
  protected TextureRegionDrawable pasteDrawable = new TextureRegionDrawable(ResourceManager.instance.getTextureRegion("data/editor/paste.png"));
  protected TextureRegionDrawable exitDrawable = new TextureRegionDrawable(ResourceManager.instance.getTextureRegion("data/editor/exit.png"));
  
  public PanelMain(String filename, Skin skin)
  {
    super(filename, skin);
    
    scroll.getListeners().removeIndex(0);
    
    this.fileName = filename;
    
    final ButtonStyle styleButton = skin.get(ButtonStyle.class);

    ImageButtonStyle style = new ImageButtonStyle(styleButton.up, styleButton.down, null,
                                                  newDrawable, newDrawable, null);
    newButton = new ImageButton(style);
    
    style = new ImageButtonStyle(styleButton.up, styleButton.down, null,
                                 openDrawable, openDrawable, null);
    openButton = new ImageButton(style);
    
    style = new ImageButtonStyle(styleButton.up, styleButton.down, null,
                                 saveDrawable, saveDrawable, null);
    saveButton = new ImageButton(style);

    style = new ImageButtonStyle(styleButton.up, styleButton.down, null,
                                 saveAsDrawable, saveAsDrawable, null);
    saveAsButton = new ImageButton(style);
    
    style = new ImageButtonStyle(styleButton.up, styleButton.down, null,
                                 cutDrawable, cutDrawable, null);
    cutButton = new ImageButton(style);
    
    style = new ImageButtonStyle(styleButton.up, styleButton.down, null,
                                 copyDrawable, copyDrawable, null);
    copyButton = new ImageButton(style);
    
    style = new ImageButtonStyle(styleButton.up, styleButton.down, null,
                                 pasteDrawable, pasteDrawable, null);
    pasteButton = new ImageButton(style);

    style = new ImageButtonStyle(styleButton.up, styleButton.down, null,
                                 exitDrawable, exitDrawable, null);
    exitButton = new ImageButton(style);

    content.add(newButton).size(40, 40);
    newButton.addListener(new ClickListener()
    {
      @Override
      public void clicked(InputEvent event, float x, float y)
      {
        newFile("");
      }
    });
    
    content.add(openButton).size(40, 40);
    openButton.addListener(new ClickListener()
    {
      @Override
      public void clicked(InputEvent event, float x, float y)
      {
        openFile();
      }
    });
    
    content.add(saveButton).size(40, 40);
    final ClickListener saveListner = new ClickListener()
    {
      @Override
      public void clicked(InputEvent event, float x, float y)
      {
        saveFile(false);
      }
    };
    saveButton.addListener(saveListner);
    
    content.add(saveAsButton).size(40, 40).spaceRight(25);
    saveAsButton.addListener(new ClickListener()
    {
      @Override
      public void clicked(InputEvent event, float x, float y)
      {
        saveFile(true);
      }
    });
    
    content.add(cutButton).size(40, 40);
    cutButton.addListener(new ClickListener()
    {
      @Override
      public void clicked(InputEvent event, float x, float y)
      {
        ((MainScreen)GameInstance.game.getScreen()).cut();
      }
    });
    
    content.add(copyButton).size(40, 40);
    copyButton.addListener(new ClickListener()
    {
      @Override
      public void clicked(InputEvent event, float x, float y)
      {
        ((MainScreen)GameInstance.game.getScreen()).copy();
      }
    });
    
    content.add(pasteButton).size(40, 40);
    pasteButton.addListener(new ClickListener()
    {
      @Override
      public void clicked(InputEvent event, float x, float y)
      {
        ((MainScreen)GameInstance.game.getScreen()).paste();
      }
    });
    
    content.add(exitButton).size(40, 40).spaceLeft(25);
    exitButton.addListener(new ClickListener()
    {
      @Override
      public void clicked(InputEvent event, float x, float y)
      {
        Gdx.app.exit();
      }
    });

    setSize(450, 80);
  }
  
  protected JFileChooser getFileChooser()
  {
    if (chooser == null)
    {
      chooser = new JFileChooser();
      chooser.setAcceptAllFileFilterUsed(false);
      chooser.addChoosableFileFilter(new FilterLevelEditor());
      chooser.setCurrentDirectory(new File("data"));
    }
    return chooser;
  }
  
  public void newFile(String fileName)
  {
    LevelEditor.game.getScreen().dispose();
    LevelEditor.game.setScreen(new MainScreen(fileName));
  }

  
  public void openFile()
  {
    if(getFileChooser().showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
      return;

    fileName = getFileChooser().getSelectedFile().getAbsolutePath();
    fileName = fileName.replace("\\", "/");

    newFile(fileName);

    try
    {
      XmlReader xml = new XmlReader();
      Element root = xml.parse(Gdx.files.internal(fileName).reader("UTF-8"));

      if (root.getName().compareToIgnoreCase("LevelEditor2D") != 0)
      {
        GameLog.instance.writeError("Invalid file format: " + fileName);
        return;
      }
      
      ((MainScreen)GameInstance.game.getScreen()).loadLevel(root);
    }
    catch (IOException e)
    {
      GameLog.instance.writeError("File not load: " + fileName);
      newFile("");
    }
  }
  
  public void saveFile(boolean saveAs)
  {
    if (saveAs || fileName.isEmpty())
    {
      if(getFileChooser().showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
        return;

      fileName = getFileChooser().getSelectedFile().getAbsolutePath();
      fileName.replace("\\", "/");
      if (fileName.lastIndexOf(".xml") != fileName.length() - 4)
        fileName += ".xml";
    }
    
    try
    {
      FileWriter writer = new FileWriter(fileName);
      XmlWriter xml = new XmlWriter(writer);
      
      xml.element("LevelEditor2D");      
      ((MainScreen)GameInstance.game.getScreen()).saveLevel(xml);
      xml.pop();

      writer.close();
    }
    catch (IOException e)
    {
      GameLog.instance.writeError("File not save: " + fileName);
    }
  }
}
