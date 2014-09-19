package com.shellGDX.model2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class ModelObject2D extends Actor
{
  protected TextureRegion region = null;
  protected int           zIndex = 0;

  public ModelObject2D()
  {
    this(0, 0, 0, 1, 1);
  }

  public ModelObject2D(float x, float y)
  {
    this(x, y, 0, 1, 1);
  }

  public ModelObject2D(float x, float y, float angle)
  {
    this(x, y, angle, 1, 1);
  }

  public ModelObject2D(float x, float y, float angle, float scaleX, float scaleY)
  {
    this(null, x, y, angle, scaleX, scaleY);
  }
  
  public ModelObject2D(TextureRegion textureRegion)
  {
    this(textureRegion, 0, 0, 0, 1, 1);
  }

  public ModelObject2D(TextureRegion textureRegion, float posX, float posY)
  {
    this(textureRegion, posX, posY, 0, 1, 1);
  }

  public ModelObject2D(TextureRegion textureRegion, float posX, float posY, float angle)
  {
    this(textureRegion, posX, posY, angle, 1, 1);
  }

  public ModelObject2D(TextureRegion textureRegion, float posX, float posY, float angle, float scaleX, float scaleY)
  {
    super();
    setTouchable(Touchable.disabled);
    setPosition(posX, posY);
    setRotation(angle);
    setScale(scaleX, scaleY);
    setTextureRegion(textureRegion);
  }
  
  public boolean isPhysicsModel()
  {
    return false;
  }

  //position
  public void setPosition(Vector2 position)
  {
    setPosition(position.x, position.y);
  }

  public Vector2 getPosition()
  {
    return new Vector2(getX(), getY());
  }
  
  //scale
  public void setScale(Vector2 scale)
  {
    setScale(scale.x, scale.y);
  }

  public Vector2 getScale()
  {
    return new Vector2(getScaleX(), getScaleY());
  }
  
  public void setTextureRegion(TextureRegion region)
  {
    if (region != null)
    {
      this.region = region;
      setSize(region.getRegionWidth(), region.getRegionHeight());
      calcOffset(region.getRegionWidth(), region.getRegionHeight());
    }
  }
  
  public TextureRegion getTextureRegion()
  {
    return region;
  }

  protected Rectangle bound = new Rectangle(0, 0, 0, 0);
  protected Vector2 bufferVec = new Vector2();

  public void updateBound()
  {
    bound.set(0, 0, 0, 0);

    if (region == null)
      return;

    float width = region.getRegionWidth();
    float height = region.getRegionHeight();
    
    bufferVec.set(offset.x, offset.y);
    bufferVec = localToStageCoordinates(bufferVec);
    bound.set(bufferVec.x, bufferVec.y, 0, 0);

    bufferVec.set(offset.x + width, offset.y);
    bufferVec = localToStageCoordinates(bufferVec);
    bound.merge(bufferVec);
    
    bufferVec.set(offset.x, offset.y + height);
    bufferVec = localToStageCoordinates(bufferVec);
    bound.merge(bufferVec);
    
    bufferVec.set(offset.x + width, offset.y + height);
    bufferVec = localToStageCoordinates(bufferVec);
    bound.merge(bufferVec);
  }
  
  public Rectangle getBound()
  {
    return bound;
  }
  
  @Override
  public void setZIndex(int zIndex)
  {
    this.zIndex = zIndex;
  }
  
  @Override
  public int getZIndex()
  {
    return this.zIndex;
  }
  
  //offset
  public class Align
  {
    public static final int NONE = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int CENTER = 3;
    public static final int TOP = 4;
    public static final int BOTTOM = 5;
  }

  protected int hAlign = Align.CENTER, vAlign = Align.CENTER;
  protected Vector2 offset = new Vector2();

  public void setAlign(int horzAlign, int vertAlign)
  {
    hAlign = horzAlign;
    vAlign = vertAlign;
    if (region != null)
      calcOffset(region.getRegionWidth(), region.getRegionHeight());
  }

  public int getHorzAlign()
  {
    return hAlign;
  }

  public int getVertAlign()
  {
    return vAlign;
  }
  
  public void setOffsetX(float x)
  {
    offset.x = x;
    hAlign = Align.NONE;
  }
  
  public void setOffsetY(float y)
  {
    offset.y = y;
    vAlign = Align.NONE;
  }
  
  public void setOffset(float x, float y)
  {
    offset.x = x;
    offset.y = y;
    hAlign = Align.NONE;
    vAlign = Align.NONE;
  }

  public float getOffsetX()
  {
    return offset.x;
  }
  
  public float getOffsetY()
  {
    return offset.y;
  }
  
  public final Vector2 getOffset()
  {
    return offset;
  }
  
  protected void calcOffset(float width, float height)
  {
    switch (hAlign)
    {
      case Align.LEFT:
        offset.x = 0.0f;
        break;
      case Align.RIGHT:
        offset.x = -width;
        break;
      default:
        offset.x = -width * 0.5f;
        break;
    }

    switch (vAlign)
    {
      case Align.BOTTOM:
        offset.y = 0.0f;
        break;
      case Align.TOP:
        offset.y = -height;
        break;
      default:
        offset.y = -height * 0.5f;
        break;
    }
  }
  
  //update and draw
  public boolean update(float deltaTime)
  {
    return isVisible();
  }
  
  @Override
  public void act(float delta)
  {
    super.act(delta);
    update(delta);  
  }
  
  protected Scene2D scene = null;
  
  @Override
  protected void setStage(Stage stage)
  {
    super.setStage(stage);
    if (stage instanceof Scene2D)
      scene = (Scene2D)stage;
  }

  @Override
  public void draw(Batch batch, float parentAlpha)
  {
    if (!isVisible() || region == null)
      return;
    
    if (scene != null)
    {
      updateBound();
      if (!scene.getCameraRectangle().overlaps(bound))
        return;
    }

    Color color = getColor();
    batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
    
    batch.draw(region,
               getX() + offset.x, getY() + offset.y,
               -offset.x, -offset.y,
               region.getRegionWidth(), region.getRegionHeight(),
               getScaleX(), getScaleY(),
               getRotation());
  }
  

  //touch
  protected InputListener inputListner = null;
  
  public void setTouchEnable(boolean enable)
  {
    if (enable)
    {
      setTouchable(Touchable.enabled);
      
      inputListner = new InputListener()
      {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
          return ModelObject2D.this.touchDown(event, x, y, pointer, button);
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
          ModelObject2D.this.touchUp(event, x, y, pointer, button);
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
          ModelObject2D.this.touchDragged(event, x, y, pointer);
        }

        @Override
        public boolean mouseMoved(InputEvent event, float x, float y) {
          return ModelObject2D.this.mouseMoved(event, x, y);
        }

        @Override
        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
          ModelObject2D.this.enter(event, x, y, pointer, fromActor);
        }

        @Override
        public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
          ModelObject2D.this.exit(event, x, y, pointer, toActor);
        }

        @Override
        public boolean scrolled(InputEvent event, float x, float y, int amount) {
          return ModelObject2D.this.scrolled(event, x, y, amount);
        }

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
          return ModelObject2D.this.keyDown(event, keycode);
        }

        @Override
        public boolean keyUp(InputEvent event, int keycode) {
          return ModelObject2D.this.keyUp(event, keycode);
        }

        @Override
        public boolean keyTyped(InputEvent event, char character) {
          return ModelObject2D.this.keyTyped(event, character);
        }
      };
      
      addListener(inputListner);
      return;
    }
    
    setTouchable(Touchable.disabled);
    removeListener(inputListner);
    inputListner = null;
  }
  
  public boolean getTouchEnable()
  {
    return getTouchable() == Touchable.enabled;
  }
  public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { return false; }
  public void touchUp(InputEvent event, float x, float y, int pointer, int button) {}
  public void touchDragged(InputEvent event, float x, float y, int pointer) {}
  public boolean mouseMoved(InputEvent event, float x, float y) { return false; }

  public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {}
  public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {}
  public boolean scrolled(InputEvent event, float x, float y, int amount) { return false; }

  public boolean keyDown(InputEvent event, int keycode) { return false; }
  public boolean keyUp(InputEvent event, int keycode) { return false; }
  public boolean keyTyped(InputEvent event, char character) { return false; }
}
