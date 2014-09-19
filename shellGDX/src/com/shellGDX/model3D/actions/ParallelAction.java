package com.shellGDX.model3D.actions;

import com.shellGDX.model3D.Action3D;
import com.shellGDX.model3D.ModelObject3D;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * Executes a number of actions at the same time.
 * 
 * @author Nathan Sweet
 */
public class ParallelAction extends Action3D
{
  Array<Action3D> actions = new Array<Action3D>(4);
  private boolean complete;

  public ParallelAction()
  {
  }

  public ParallelAction(Action3D action1)
  {
    addAction(action1);
  }

  public ParallelAction(Action3D action1, Action3D action2)
  {
    addAction(action1);
    addAction(action2);
  }

  public ParallelAction(Action3D action1, Action3D action2, Action3D action3)
  {
    addAction(action1);
    addAction(action2);
    addAction(action3);
  }

  public ParallelAction(Action3D action1, Action3D action2, Action3D action3, Action3D action4)
  {
    addAction(action1);
    addAction(action2);
    addAction(action3);
    addAction(action4);
  }

  public ParallelAction(Action3D action1, Action3D action2, Action3D action3, Action3D action4, Action3D action5)
  {
    addAction(action1);
    addAction(action2);
    addAction(action3);
    addAction(action4);
    addAction(action5);
  }

  @Override
  public boolean act(float delta)
  {
    if (complete)
      return true;
    complete = true;
    Pool<Action3D> pool = getPool();
    setPool(null); // Ensure this action can't be returned to the pool while
                   // executing.
    try
    {
      Array<Action3D> actions = this.actions;
      for (int i = 0, n = actions.size; i < n && model3D != null; i++)
      {
        if (!actions.get(i).act(delta))
          complete = false;
        if (model3D == null)
          return true; // This action was removed.
      }
      return complete;
    } finally
    {
      setPool(pool);
    }
  }

  @Override
  public void restart()
  {
    complete = false;
    Array<Action3D> actions = this.actions;
    for (int i = 0, n = actions.size; i < n; i++)
      actions.get(i).restart();
  }

  @Override
  public void reset()
  {
    super.reset();
    actions.clear();
  }

  public void addAction(Action3D action)
  {
    actions.add(action);
    if (model3D != null)
      action.setModel3D(model3D);
  }

  @Override
  public void setModel3D(ModelObject3D model)
  {
    Array<Action3D> actions = this.actions;
    for (int i = 0, n = actions.size; i < n; i++)
      actions.get(i).setModel3D(model);
    super.setModel3D(model);
  }

  public Array<Action3D> getActions()
  {
    return actions;
  }

  @Override
  public String toString()
  {
    StringBuilder buffer = new StringBuilder(64);
    buffer.append(super.toString());
    buffer.append('(');
    Array<Action3D> actions = this.actions;
    for (int i = 0, n = actions.size; i < n; i++)
    {
      if (i > 0)
        buffer.append(", ");
      buffer.append(actions.get(i));
    }
    buffer.append(')');
    return buffer.toString();
  }
}