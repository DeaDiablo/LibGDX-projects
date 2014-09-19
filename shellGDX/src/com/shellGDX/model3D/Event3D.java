package com.shellGDX.model3D;

import com.badlogic.gdx.utils.Pool.Poolable;

public class Event3D implements Poolable
{
  private Scene3D scene;
  private ModelObject3D targetModel;
  private ModelObject3D listenerModel;
  private boolean capture;       // true means event occurred during the
                                  // capture phase
  private boolean bubbles = true; // true means propagate to target's parents
  private boolean handled;       // true means the event was handled (the scene
                                  // will eat the input)
  private boolean stopped;       // true means event propagation was stopped
  private boolean cancelled;     // true means propagation was stopped and any
                                  // action that this event would cause should
                                  // not happen

  /**
   * Marks this event as handled. This does not affect event propagation inside
   * scene2d, but causes the {@link Scene} event methods to return false, which
   * will eat the event so it is not passed on to the application under the
   * scene.
   */
  public void handle()
  {
    handled = true;
  }

  /**
   * Marks this event cancelled. This {@link #handle() handles} the event and
   * {@link #stop() stops} the event propagation. It also cancels any default
   * action that would have been taken by the code that fired the event. Eg, if
   * the event is for a checkbox being checked, cancelling the event could
   * uncheck the checkbox.
   */
  public void cancel()
  {
    cancelled = true;
    stopped = true;
    handled = true;
  }

  /**
   * Marks this event has being stopped. This halts event propagation. Any other
   * listeners on the {@link #getListenerModel() listener model} are notified,
   * but after that no other listeners are notified.
   */
  public void stop()
  {
    stopped = true;
  }

  public void reset()
  {
    scene = null;
    targetModel = null;
    listenerModel = null;
    capture = false;
    bubbles = true;
    handled = false;
    stopped = false;
    cancelled = false;
  }

  /** Returns the model that the event originated from. */
  public ModelObject3D getTarget()
  {
    return targetModel;
  }

  public void setTarget(ModelObject3D targetModel)
  {
    this.targetModel = targetModel;
  }

  /** Returns the model that this listener is attached to. */
  public ModelObject3D getListenerModel()
  {
    return listenerModel;
  }

  public void setListenerModel(ModelObject3D listenerModel)
  {
    this.listenerModel = listenerModel;
  }

  public boolean getBubbles()
  {
    return bubbles;
  }

  /**
   * If true, after the event is fired on the target model, it will also be
   * fired on each of the parent models, all the way to the root.
   */
  public void setBubbles(boolean bubbles)
  {
    this.bubbles = bubbles;
  }

  /** {@link #handle()} */
  public boolean isHandled()
  {
    return handled;
  }

  /** @see #stop() */
  public boolean isStopped()
  {
    return stopped;
  }

  /** @see #cancel() */
  public boolean isCancelled()
  {
    return cancelled;
  }

  public void setCapture(boolean capture)
  {
    this.capture = capture;
  }

  /**
   * If true, the event was fired during the capture phase.
   * 
   * @see Model#fire(Event)
   */
  public boolean isCapture()
  {
    return capture;
  }

  public void setScene(Scene3D scene)
  {
    this.scene = scene;
  }

  /** The scene for the model the event was fired on. */
  public Scene3D getScene()
  {
    return scene;
  }
}