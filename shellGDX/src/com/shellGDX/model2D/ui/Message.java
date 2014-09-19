package com.shellGDX.model2D.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.shellGDX.model2D.Group2D;
import com.shellGDX.model2D.ModelObject2D;

public class Message extends Group2D
{
  public Message(TextureRegion region, float x, float y, Text text, float lifeSec)
  {
    super(x, y);
    addActor(new ModelObject2D(region));
    addActor(text);

    addListener(new InputListener() {
      @Override
      public void touchUp(InputEvent event, float x, float y, int pointer, int button)
      {
        remove();
      }
    });
    
    setTouchable(Touchable.enabled);
    
    Timer.schedule(new Task()
    {
      @Override
      public void run()
      {
        remove();
      }
    }, lifeSec);
  }
}
