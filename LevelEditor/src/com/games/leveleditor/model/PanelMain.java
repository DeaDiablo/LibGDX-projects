package com.games.leveleditor.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.games.leveleditor.LevelEditor;
import com.games.leveleditor.screen.MainScreen;
import com.shellGDX.GameInstance;
import com.shellGDX.GameLog;
public class PanelMain extends Panel
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
  
  public TextButton  newButton  = null;
  public TextButton  openButton = null;
  public TextButton  saveButton = null;
  public TextButton  saveAsButton = null;
  public TextButton  exitButton = null;
  
  protected String       fileName   = "";
  protected JFileChooser chooser    = null;
  
  public PanelMain(String title, Skin skin)
  {
    super(title, skin);
    
    //visible
    newButton = new TextButton("new", skin);
    openButton = new TextButton("open", skin);
    saveButton = new TextButton("save", skin);
    saveAsButton = new TextButton("save as...", skin);
    exitButton = new TextButton("exit", skin);

    add(newButton);
    newButton.addListener(new ClickListener()
    {
      @Override
      public void clicked(InputEvent event, float x, float y)
      {
        newFile();
      }
    });
    
    add(openButton);
    openButton.addListener(new ClickListener()
    {
      @Override
      public void clicked(InputEvent event, float x, float y)
      {
        openFile();
      }
    });
    
    add(saveButton);
    final ClickListener saveListner = new ClickListener()
    {
      @Override
      public void clicked(InputEvent event, float x, float y)
      {
        saveFile(false);
      }
    };
    saveButton.addListener(saveListner);
    
    add(saveAsButton);
    saveAsButton.addListener(new ClickListener()
    {
      @Override
      public void clicked(InputEvent event, float x, float y)
      {
        saveFile(true);
      }
    });
    
    add(exitButton);
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
    }
    return chooser;
  }
  
  public void newFile()
  {
    fileName = "";
    LevelEditor.game.getScreen().dispose();
    LevelEditor.game.setScreen(new MainScreen());
  }

  
  public void openFile()
  {
    if(getFileChooser().showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
      return;

    newFile();

    fileName = getFileChooser().getSelectedFile().getAbsolutePath();
    fileName = fileName.replace("\\", "/");

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
      newFile();
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
