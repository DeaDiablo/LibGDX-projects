package com.shellGDX.manager;

import java.io.File;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.shellGDX.GameLog;
import com.shellGDX.utils.gleed.LevelGleed2D;
import com.shellGDX.utils.gleed.LevelGleed2DLoader;
import com.shellGDX.utils.leveleditor2d.Editor2DLevel;
import com.shellGDX.utils.leveleditor2d.LevelEditor2DLoader;

public class ResourceManager extends AssetManager
{
  public static volatile ResourceManager instance = new ResourceManager();

  public ResourceManager()
  {
    this(new InternalFileHandleResolver());
  }

  public ResourceManager(InternalFileHandleResolver resolver)
  {
    super(resolver);
    setLoader(TiledMap.class, new TmxMapLoader(resolver));
    setLoader(LevelGleed2D.class, new LevelGleed2DLoader(resolver));
    setLoader(Editor2DLevel.class, new LevelEditor2DLoader(resolver));
  }
  
  public void loadFolder(String folder)
  {
    File directory = new File(folder);
    
    if (!directory.isDirectory())
    {
      GameLog.instance.writeError("Directory \"" + folder + "\" not found!");
      return;
    }
   
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
        loadFolder(file);
        continue;
      }
      
      if(file.isFile())
      {
        String path = file.getPath();
        path = path.replace("\\", "/");
        int pointPosition = path.lastIndexOf(".");
        if (pointPosition > 0)
        {
          String extension = path.substring(pointPosition);
          if (extension.compareToIgnoreCase(".png") == 0 ||
              extension.compareToIgnoreCase(".jpg") == 0)
          {
            loadTexture(path);
          }
          else if (extension.compareToIgnoreCase(".tmx") == 0)
          {
            loadTiledMap(path);
          }
          else if (extension.compareToIgnoreCase(".obj") == 0)
          {
            loadModel(path);
          }
          else if (extension.compareToIgnoreCase(".p") == 0)
          {
            loadParticleEffect(path);
          }
        }
      }
    }
  }

  public FileHandle loadFile(String fileName)
  {
    return Gdx.files.internal(fileName);
  }

  public void loadTiledMap(String fileName)
  {
    load(fileName, TiledMap.class);
  }

  public TiledMap getTiledMap(String fileName)
  {
    return get(fileName, TiledMap.class);
  }

  public void loadGleed2DMap(String fileName)
  {
    load(fileName, LevelGleed2D.class);
  }

  public LevelGleed2D getGleed2DMap(String fileName)
  {
    return get(fileName, LevelGleed2D.class);
  }
  
  public void loadEditorLevel(String fileName)
  {
    load(fileName, Editor2DLevel.class);
  }

  public Editor2DLevel getEditorLevel(String fileName)
  {
    return get(fileName, Editor2DLevel.class);
  }

  public void loadTexture(String fileName)
  {
    TextureParameter param = new TextureParameter();
    param.minFilter = TextureFilter.Linear;
    param.genMipMaps = false;
    param.wrapU = TextureWrap.Repeat;
    param.wrapV = TextureWrap.Repeat;
    load(fileName, Texture.class, param);
  }
  
  public Texture getTexture(String fileName)
  {
    return get(fileName, Texture.class);
  }
  
  public void loadTextureAtlas(String fileName)
  {
    load(fileName, TextureAtlas.class);
  }
  
  public TextureAtlas getTextureAtlas(String fileName)
  {
    return get(fileName, TextureAtlas.class);
  }


  public TextureRegion getTextureRegion(String fileName)
  {
    return new TextureRegion(get(fileName, Texture.class));
  }

  public TextureRegion getTextureRegion(String fileName, float u, float v, float u2, float v2)
  {
    return new TextureRegion(get(fileName, Texture.class), u, v, u2, v2);
  }

  public TextureRegion getTextureRegion(String fileName, int x, int y, int width, int height)
  {
    return new TextureRegion(get(fileName, Texture.class), x, y, width, height);
  }

  protected void loadSound(String fileName)
  {
    load(fileName, Sound.class);
  }

  protected Sound getSound(String fileName)
  {
    return get(fileName, Sound.class);
  }

  protected void loadMusic(String fileName)
  {
    load(fileName, Music.class);
  }

  protected Music getMusic(String fileName)
  {
    return get(fileName, Music.class);
  }
  
  public void loadModel(String fileName)
  {
    load(fileName, Model.class);
  }

  public Model getModel(String fileName)
  {
    return get(fileName, Model.class);
  }
  
  public void loadSkin(String fileNameJSON, String fileNameAtlas)
  {
    loadSkin(fileNameJSON, fileNameAtlas, null);
  }
  
  public void loadSkin(String fileNameJSON, String fileNameAtlas, ObjectMap<String, Object> objects)
  {
    load(fileNameAtlas, TextureAtlas.class);

    int index = fileNameAtlas.lastIndexOf("/");    
    if (index != -1)
      fileNameAtlas = fileNameAtlas.substring(index + 1);

    SkinParameter param = new SkinParameter(fileNameAtlas, objects);
    load(fileNameJSON, Skin.class, param);
  }
  
  public Skin getSkin(String fileNameJSON)
  {
    return get(fileNameJSON, Skin.class);
  }
  
  final Array<String> particleFiles = new Array<String>();
  final HashMap<String, ParticleEffectPool> particlePools = new HashMap<String, ParticleEffectPool>();
  
  public void loadParticleEffect(String fileName)
  {
    load(fileName, ParticleEffect.class);
    particleFiles.add(convertFilename(fileName));
  }
  
  public PooledEffect getEffect(String fileName)
  {
    ParticleEffectPool pool = particlePools.get(convertFilename(fileName));
    if (pool != null)
      return pool.obtain();
    return null;
  }

  @Override
  public synchronized void clear()
  {
    FontManager.instance.clear();
    SoundManager.instance.clear();
    super.clear();
  }

  @Override
  public synchronized void dispose()
  {
    FontManager.instance.clear();
    SoundManager.instance.clear();
    super.dispose();
  }

  @Override
  public synchronized boolean update()
  {
    if (super.update())
    {
      endLoading();
      return true;
    }
    return false;
  }

  @Override
  public boolean update(int millis)
  {
    if (super.update(millis))
    {
      endLoading();
      return true;
    }
    return false;
  }

  @Override
  public void finishLoading()
  {
    super.finishLoading();
    endLoading();
  }
  
  protected void endLoading()
  {
    for(int i = 0; i < particleFiles.size; i++)
    {
      String fileName = particleFiles.get(i);
      ParticleEffect effect = get(fileName, ParticleEffect.class);
      if (effect != null)
        particlePools.put(fileName,  new ParticleEffectPool(effect, 4, 16));
    }
    particleFiles.clear();
    SoundManager.instance.finishLoading();
  }
  
  @Override
  public synchronized <T> T get(String fileName, Class<T> type)
  {
    return super.get(convertFilename(fileName), type);
  }
  
  @Override
  public synchronized <T> T get(String fileName)
  {
    return super.get(convertFilename(fileName));
  }

  @Override
  protected <T> void addAsset(final String fileName, Class<T> type, T asset)
  { 
    super.addAsset(convertFilename(fileName), type, asset);
  }
  
  protected String convertFilename(String fileName)
  {
    int index = fileName.lastIndexOf("/");    
    if (index != -1)
      fileName = fileName.substring(index + 1);
    return fileName;
  }
}
