package com.games.leveleditor.model;

import java.io.File;
 
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.games.leveleditor.screen.MainScreen;
import com.shellGDX.GameLog;
import com.shellGDX.manager.ResourceManager;

public class PanelGraphics extends PanelScroll
{
  public static String defaultGraphicPath = "data/sprites";
  public static final float buttonSize = 128.0f;
  
  protected final int rowMaxSize = 7;
  protected int rowSize = 0;

  protected MainScreen screen = null;
  protected ImageButton activeButton = null;
  protected Skin skin = null;
  protected String path = "";
  protected TextureRegionDrawable folderDrawable = new TextureRegionDrawable(ResourceManager.instance.getTextureRegion("data/editor/folder.png"));
  
  public PanelGraphics(String title, Skin skin, MainScreen mainScreen)
  {
    super(title, skin);
    this.skin = skin;
    setSize(1470, 350);
    content.left().top();
    screen = mainScreen;
    
    parseFolder(defaultGraphicPath);
  }

  private void parseFolder(String path)
  {
    rowSize = 0;
    content.clearChildren();
    this.path = path;
    
    File directory = new File(path);
    
    if (!directory.isDirectory())
    {
      GameLog.instance.writeError("Directory \"" + path + "\" not found!");
      return;
    }

    if (path.compareToIgnoreCase(defaultGraphicPath) != 0)
      addElement(folderDrawable, "..");
    loadFolder(directory);
  }
  
  protected void loadFolder(File directory)
  {
    File[] files = directory.listFiles();

    for(int i = 0; i < files.length; i++)
    {
      File file = files[i];
      if (file.isDirectory())
      {
        addElement(folderDrawable, file.getName());
        continue;
      }
    }
    
    for(int i = 0; i < files.length; i++)
    {
      File file = files[i];
      if(file.isFile())
      {
        String pathFile = file.getPath();
        pathFile = pathFile.replace("\\", "/");
        int pointPosition = pathFile.lastIndexOf(".");
        if (pointPosition > 0)
        {
          String extension = pathFile.substring(pointPosition);
          if (extension.compareToIgnoreCase(".png") == 0 ||
              extension.compareToIgnoreCase(".jpg") == 0)
          {
            addElement(new TextureRegionDrawable(ResourceManager.instance.getTextureRegion(pathFile)), file.getName());
            continue;
          }
        }
      }
    }
  }
  
  protected void addElement(final TextureRegionDrawable drawable, final String name)
  {
    Table item = new Table(skin);
    
    final boolean isFolder = drawable == folderDrawable; 
    
    final ButtonStyle styleButton = skin.get(ButtonStyle.class);
    ImageButtonStyle style = new ImageButtonStyle(styleButton.up, styleButton.down, isFolder ? null : styleButton.down,
                                                  drawable, drawable, isFolder ? null : drawable);
    
    final ImageButton button = new ImageButton(style);
    button.addListener(new ClickListener()
    {
      @Override
      public void clicked(InputEvent event, float x, float y)
      {
        //folder
        if (isFolder)
        {
          screen.clearAddModel();
          clearActiveButton();

          if (name.compareTo("..") == 0)
          {
            int index = path.lastIndexOf('/');
            if (index > 0)
              parseFolder(path.substring(0, index));
          }
          else
          {
            parseFolder(String.format("%s/%s", path, name));
          }
        }
        //sprite
        else
        {
          if (activeButton == button)
          {
            screen.clearAddModel();
            clearActiveButton();
            return;
          }
          
          clearActiveButton();
          
          screen.setAddModel(name);
          activeButton = button;
        }
      }
    });
    
    item.add(button).width(buttonSize).height(buttonSize);
    
    item.row();
    
    String labelName = "";
    
    if (name.length() > 16)
      labelName = name.substring(0, 10) + "..." + name.substring(name.length() - 5, name.length());
    else
      labelName = name;
    
    item.add(labelName);

    if (rowSize >= rowMaxSize)
    {
      content.row();
      rowSize = 0;
    }

    content.add(item).width(0.95f * getWidth() / rowMaxSize);
    rowSize++;
  }
  
  public void clearActiveButton()
  {
    if (activeButton != null)
    {
      activeButton.setChecked(false);
      activeButton = null;
    }
  }
}
