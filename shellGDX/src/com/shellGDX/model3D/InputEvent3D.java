package com.shellGDX.model3D;

import com.badlogic.gdx.math.Vector3;

/**
 * Event for model input: touch, mouse, keyboard, and scroll.
 * 
 * @see InputListener
 */
public class InputEvent3D extends Event3D
{
  private Type    type;
  private float   sceneX, sceneY, sceneZ;
  private int     pointer, button, keyCode, scrollAmount;
  private char    character;
  private ModelObject3D relatedModel;

  public void reset()
  {
    super.reset();
    relatedModel = null;
    button = -1;
  }

  /**
   * The scene x coordinate where the event occurred. Valid for: touchDown,
   * touchDragged, touchUp, mouseMoved, enter, and exit.
   */
  public float getSceneX()
  {
    return sceneX;
  }

  public void setSceneX(float sceneX)
  {
    this.sceneX = sceneX;
  }

  /**
   * The scene x coordinate where the event occurred. Valid for: touchDown,
   * touchDragged, touchUp, mouseMoved, enter, and exit.
   */
  public float getSceneY()
  {
    return sceneY;
  }

  public void setSceneY(float sceneY)
  {
    this.sceneY = sceneY;
  }

  /** The type of input event. */
  public Type getType()
  {
    return type;
  }

  public void setType(Type type)
  {
    this.type = type;
  }

  /**
   * The pointer index for the event. The first touch is index 0, second touch
   * is index 1, etc. Always -1 on desktop. Valid for: touchDown, touchDragged,
   * touchUp, enter, and exit.
   */
  public int getPointer()
  {
    return pointer;
  }

  public void setPointer(int pointer)
  {
    this.pointer = pointer;
  }

  /**
   * The index for the mouse button pressed. Always 0 on Android. Valid for:
   * touchDown and touchUp.
   * 
   * @see Buttons
   */
  public int getButton()
  {
    return button;
  }

  public void setButton(int button)
  {
    this.button = button;
  }

  /** The key code of the key that was pressed. Valid for: keyDown and keyUp. */
  public int getKeyCode()
  {
    return keyCode;
  }

  public void setKeyCode(int keyCode)
  {
    this.keyCode = keyCode;
  }

  /** The character for the key that was type. Valid for: keyTyped. */
  public char getCharacter()
  {
    return character;
  }

  public void setCharacter(char character)
  {
    this.character = character;
  }

  /** The amount the mouse was scrolled. Valid for: scrolled. */
  public int getScrollAmount()
  {
    return scrollAmount;
  }

  public void setScrollAmount(int scrollAmount)
  {
    this.scrollAmount = scrollAmount;
  }

  /**
   * The model related to the event. Valid for: enter and exit. For enter, this
   * is the model being exited, or null. For exit, this is the model being
   * entered, or null.
   */
  public ModelObject3D getRelatedModel()
  {
    return relatedModel;
  }

  /**
   * @param relatedModel
   *          May be null.
   */
  public void setRelatedModel(ModelObject3D relatedModel)
  {
    this.relatedModel = relatedModel;
  }

  /**
   * Sets modelCoords to this event's coordinates relative to the specified
   * model.
   * 
   * @param modelCoords
   *          Output for resulting coordinates.
   */
  public Vector3 toCoordinates(ModelObject3D model, Vector3 modelCoords)
  {
    modelCoords.set(sceneX, sceneY, sceneZ);
    // model.sceneToLocalCoordinates(modelCoords);
    return modelCoords;
  }

  /**
   * Returns true of this event is a touchUp triggered by
   * {@link Scene#cancelTouchFocus()}.
   */
  public boolean isTouchFocusCancel()
  {
    return sceneX == Integer.MIN_VALUE || sceneY == Integer.MIN_VALUE;
  }

  public String toString()
  {
    return type.toString();
  }

  /** Types of low-level input events supported by scene2d. */
  static public enum Type
  {
    /** A new touch for a pointer on the scene was detected */
    touchDown,
    /** A pointer has stopped touching the scene. */
    touchUp,
    /** A pointer that is touching the scene has moved. */
    touchDragged,
    /** The mouse pointer has moved (without a mouse button being active). */
    mouseMoved,
    /**
     * The mouse pointer or an active touch have entered (i.e.,
     * {@link Model#hit(float, float, boolean) hit}) an model.
     */
    enter,
    /** The mouse pointer or an active touch have exited an model. */
    exit,
    /** The mouse scroll wheel has changed. */
    scrolled,
    /** A keyboard key has been pressed. */
    keyDown,
    /** A keyboard key has been released. */
    keyUp,
    /** A keyboard key has been pressed and released. */
    keyTyped
  }
}