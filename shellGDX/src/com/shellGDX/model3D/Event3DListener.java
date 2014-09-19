package com.shellGDX.model3D;

/**
 * Low level interface for receiving events. Typically there is a listener class
 * for each specific event class.
 * 
 * @see InputListener
 * @see InputEvent
 */
public interface Event3DListener
{
  /**
   * Try to handle the given event, if it is applicable.
   * 
   * @return true if the event should be considered
   *         {@link ClickedListener#handle() handled} by scene2d.
   */
  public boolean handle(Event3D event);
}