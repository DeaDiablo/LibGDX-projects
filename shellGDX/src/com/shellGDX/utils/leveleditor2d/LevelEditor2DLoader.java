package com.shellGDX.utils.leveleditor2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.shellGDX.GameLog;

public class LevelEditor2DLoader extends AsynchronousAssetLoader<Editor2DLevel, LevelEditor2DLoader.LevelParameter > implements Disposable
{

  static public class LevelParameter extends AssetLoaderParameters<Editor2DLevel>
  {
  }

  private Editor2DLevel level = null;
  
  public LevelEditor2DLoader(FileHandleResolver resolver)
  {
    super(resolver);
  }

  @Override
  public void loadAsync(AssetManager manager, String fileName, FileHandle file, LevelParameter parameter)
  {    
    GameLog.instance.writeLog("Loading levelEditor2D file: " + fileName);
    
    try
    {
      // Parse xml document
      XmlReader xml = new XmlReader();
      Element root = xml.parse(Gdx.files.internal(fileName).reader("UTF-8"));
      
      if (root.getName().compareToIgnoreCase("LevelEditor2D") != 0)
      {
        GameLog.instance.writeError("Invalid file format: " + fileName);
        return;
      }
      
      level = new Editor2DLevel();
      
      // Load layers
      GameLog.instance.writeLog("Loading layers");
      Array<Element> elementsLayer = root.getChildrenByName("layer");
      for(Element element : elementsLayer)
      {
        Layer layer = new Layer();
        layer.load(element);
        level.addActor(layer);
      }
    }
    catch (Exception e)
    {
      GameLog.instance.writeLog("Error loading file: " + fileName + " " + e.getMessage());
    }
  }

  @Override
  public Editor2DLevel loadSync (AssetManager manager, String fileName, FileHandle file, LevelParameter parameter)
  {
    return level;
  }

  @SuppressWarnings({ "rawtypes" })
  @Override
  public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, LevelParameter parameter)
  {
    GameLog.instance.writeLog("Getting asset dependencies for: " + fileName);
    Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>();
    
    try
    {
      // Parse xml document
      XmlReader reader = new XmlReader();
      Element root = reader.parse(Gdx.files.internal(fileName));

      level = new Editor2DLevel();
      
      // Load layers
      GameLog.instance.writeLog("Loading layers");
      Array<Element> elementsLayer = root.getChildrenByName("layer");
      for(Element element : elementsLayer)
      {
        Layer layer = new Layer();
        layer.load(element);
        level.addActor(layer);
      }
      
    }
    catch (Exception e)
    {
      GameLog.instance.writeLog("Error loading asset dependencies: " + fileName + " " + e.getMessage());
    }
    
    return dependencies;
  }

  @Override
  public void dispose()
  {
  }
}
