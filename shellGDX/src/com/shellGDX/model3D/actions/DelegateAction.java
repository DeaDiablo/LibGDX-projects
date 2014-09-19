package com.shellGDX.model3D.actions;

import com.shellGDX.model3D.Action3D;
import com.shellGDX.model3D.ModelObject3D;

import com.badlogic.gdx.utils.Pool;

/**
 * Base class for an action that wraps another action.
 * 
 * @author Nathan Sweet
 */
abstract public class DelegateAction extends Action3D
{
  protected Action3D action;

  /** Sets the wrapped action. */
  public void setAction(Action3D action)
  {
    this.action = action;
  }

  public Action3D getAction()
  {
    return action;
  }

  abstract protected boolean delegate(float delta);

  @Override
  public final boolean act(float delta)
  {
    Pool<Action3D> pool = getPool();
    setPool(null); // Ensure this action can't be returned to the pool inside
                   // the delegate action.
    try
    {
      return delegate(delta);
    } finally
    {
      setPool(pool);
    }
  }

  @Override
  public void restart()
  {
    if (action != null)
      action.restart();
  }

  @Override
  public void reset()
  {
    super.reset();
    action = null;
  }

  @Override
  public void setModel3D(ModelObject3D model3D)
  {
    if (action != null)
      action.setModel3D(model3D);
    super.setModel3D(model3D);
  }

  @Override
  public String toString()
  {
    return super.toString() + (action == null ? "" : "(" + action + ")");
  }
}