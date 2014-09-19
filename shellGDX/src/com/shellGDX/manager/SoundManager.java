package com.shellGDX.manager;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.shellGDX.GameInstance;

public enum SoundManager
{
  instance;
  
  //use sounds
  protected boolean                 soundOn      = true;
  protected HashMap<String, Sound>  sounds       = new HashMap<String, Sound>();

  public Sound getSound(String soundName)
  {
    return sounds.get(soundName);
  }

  public void playSound(String soundName)
  {
    playSound(soundName, 1.0f);
  }

  public void playSound(String soundName, float soundVolume)
  {
    if (!soundOn)
      return;
    
    Sound sound = sounds.get(soundName);
    if (sound == null)
      return;
    
    sound.play(soundVolume);
  }
  
  public void resumeSounds()
  {
    for (Sound sound : sounds.values())
      sound.resume();
  }
  
  public void pauseSounds()
  {
    for (Sound sound : sounds.values())
      sound.pause();
  }

  public void stopSounds()
  {
    for (Sound sound : sounds.values())
      sound.stop();
  }

  public void stopSound(String soundName)
  {
    Sound sound = sounds.get(soundName);
    if (sound == null)
      return;
    sound.stop();
  }

  public void setSoundOn(boolean on)
  {
    soundOn = on;
    if (!soundOn)
      stopSounds();
    GameInstance.game.config.putBoolean("soundOn", on);
    GameInstance.game.config.flush();
  }

  public boolean getSoundOn()
  {
    return soundOn;
  }

  //musics
  protected boolean                 musicOn      = true;
  protected boolean                 musicStop    = true;

  public boolean                    musicAlways  = false;
  public float                      musicVolume  = 1.0f;

  protected int                     indexTrack   = -1;
  protected Music                   currentTrack = null;

  protected HashMap<String, Music>  musics       = new HashMap<String, Music>();
  protected Vector<Music>           musicsVec    = new Vector<Music>();

  public Music getMusic(String trackName)
  {
    return musics.get(trackName);
  }
  
  public void playMusic()
  {
    if (!musicOn)
      return;
    musicStop = false;
    SoundManager.instance.musicAlways = true;
  }

  public void playMusic(String trackName)
  {
    if (!musicOn)
      return;
    musicStop = false;
    Music music = musics.get(trackName);
    if (music == null)
      return;
    music.setVolume(musicVolume);
    music.play();
  }
  
  public void resumeMusics()
  {
    if (!musicStop && currentTrack != null)
      currentTrack.play();
  }
  
  public void pauseMusics()
  {
    for (Music music : musics.values())
      music.pause();
  }

  public void stopMusics()
  {
    SoundManager.instance.musicAlways = false;
    musicStop = true;
    if (currentTrack != null)
      currentTrack.stop();
  }

  public void setMusicOn(boolean on)
  {
    musicOn = on;
    if (!on)
      stopMusics();
    GameInstance.game.config.putBoolean("musicOn", on);
    GameInstance.game.config.flush();
  }

  public boolean getMusicOn()
  {
    return musicOn;
  }

  public void update()
  {
    if (musicOn && musicAlways && !musics.isEmpty())
    {
      if (currentTrack == null || !currentTrack.isPlaying())
      {
        indexTrack++;
        if (indexTrack >= musicsVec.size())
          indexTrack = 0;
        currentTrack = musicsVec.get(indexTrack);
        currentTrack.setVolume(musicVolume);
        currentTrack.play();
      }
    }
  }

  //music and sounds
  public void resumeAllAudio()
  {
    resumeSounds();
    resumeMusics();
  }
  
  public void pauseAllAudio()
  {
    pauseSounds();
    pauseMusics();
  }

  public void stopAllAudio()
  {
    stopSounds();
    stopMusics();
  }

  //clear
  protected void clear()
  {
    musicAlways = false;
    indexTrack = -1;
    currentTrack = null;
    musicsVec.clear();
    updateMusics.clear();
    updateSounds.clear();
    sounds.clear();
    musics.clear();
  }
  
  //load musics and sounds
  protected HashMap<String, String> updateSounds = new HashMap<String, String>();
  protected HashMap<String, String> updateMusics = new HashMap<String, String>();
  
  public void loadSound(String soundName, String fileName)
  {
    ResourceManager.instance.loadSound(fileName);
    updateSounds.put(soundName, fileName);
  }

  public void loadMusic(String trackName, String fileName)
  {
    ResourceManager.instance.loadMusic(fileName);
    updateMusics.put(trackName, fileName);
  }

  //update loading sounds and music
  protected void finishLoading()
  {
    for (Entry<String, String> updateElement : updateSounds.entrySet())
    {
      Sound sound = ResourceManager.instance.getSound(updateElement.getValue());
      if (sound != null)
        sounds.put(updateElement.getKey(), sound);
    }
    updateSounds.clear();

    for (Entry<String, String> updateElement : updateMusics.entrySet())
    {
      Music music = ResourceManager.instance.getMusic(updateElement.getValue());
      if (music != null)
      {
        musicsVec.add(music);
        musics.put(updateElement.getKey(), music);
      }
    }
    updateMusics.clear();
  }
}
