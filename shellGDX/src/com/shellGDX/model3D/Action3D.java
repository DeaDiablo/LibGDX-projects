package com.shellGDX.model3D;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Actions attach to an {@link Model} and perform some task, often over time.
 * 
 * @author Nathan Sweet
 */
abstract public class Action3D implements Poolable
{
  /** The model this action is attached to, or null if it is not attached. */
  protected ModelObject3D model3D;

  private Pool<Action3D> pool;

  /**
   * Updates the action based on time. Typically this is called each frame by
   * {@link ModelObject3D#update(float)}.
   * 
   * @param delta
   *          Time in seconds since the last frame.
   * @return true if the action is done. This method may continue to be called
   *         after the action is done.
   */
  abstract public boolean act(float delta);

  /** Sets the state of the action so it can be run again. */
  public void restart()
  {
  }

  /** @return null if the action is not attached to an model. */
  public ModelObject3D getModel3D()
  {
    return model3D;
  }

  /**
   * Sets the model this action will be used for. This is called automatically
   * when an action is added to an model. This is also called with null when an
   * action is removed from an model. When set to null, if the action has a
   * {@link #setPool(Pool) pool} then the action is {@link Pool#free(Object)
   * returned} to the pool (which calls {@link #reset()}) and the pool is set to
   * null. If the action does not have a pool, {@link #reset()} is not called.
   * <p>
   * This method is not typically a good place for a subclass to query the
   * model's state because the action may not be executed for some time, eg it
   * may be {@link DelayAction delayed}. The model's state is best queried in the
   * first call to {@link #act(float)}. For a {@link TemporalAction}, use
   * TemporalAction#begin().
   */
  public void setModel3D(ModelObject3D model3D)
  {
    this.model3D = model3D;
    if (model3D == null)
    {
      if (pool != null)
      {
        pool.free(this);
        pool = null;
      }
    }
  }

  /**
   * Resets the optional state of this action to as if it were newly created,
   * allowing the action to be pooled and reused. State required to be set for
   * every usage of this action or computed during the action does not need to
   * be reset.
   * <p>
   * The default implementation calls {@link #restart()}.
   * <p>
   * If a subclass has optional state, it must override this method, call super,
   * and reset the optional state.
   */
  public void reset()
  {
    restart();
  }

  public Pool<Action3D> getPool()
  {
    return pool;
  }

  /**
   * Sets the pool that the action will be returned to when removed from the
   * model.
   * 
   * @param pool
   *          May be null.
   * @see #setModel3D(Model)
   */
  @SuppressWarnings("unchecked")
  public void setPool(Pool<? extends Action3D> pool)
  {
    this.pool = (Pool<Action3D>)pool;
  }

  public String toString()
  {
    String name = getClass().getName();
    int dotIndex = name.lastIndexOf('.');
    if (dotIndex != -1)
      name = name.substring(dotIndex + 1);
    if (name.endsWith("Action"))
      name = name.substring(0, name.length() - 6);
    return name;
  }
}