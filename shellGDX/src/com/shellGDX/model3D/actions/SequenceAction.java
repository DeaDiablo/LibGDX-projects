package com.shellGDX.model3D.actions;

import com.shellGDX.model3D.Action3D;

import com.badlogic.gdx.utils.Pool;

/**
 * Executes a number of actions one at a time.
 * 
 * @author Nathan Sweet
 */
public class SequenceAction extends ParallelAction
{
  private int index;

  public SequenceAction()
  {
  }

  public SequenceAction(Action3D action1)
  {
    addAction(action1);
  }

  public SequenceAction(Action3D action1, Action3D action2)
  {
    addAction(action1);
    addAction(action2);
  }

  public SequenceAction(Action3D action1, Action3D action2, Action3D action3)
  {
    addAction(action1);
    addAction(action2);
    addAction(action3);
  }

  public SequenceAction(Action3D action1, Action3D action2, Action3D action3, Action3D action4)
  {
    addAction(action1);
    addAction(action2);
    addAction(action3);
    addAction(action4);
  }

  public SequenceAction(Action3D action1, Action3D action2, Action3D action3, Action3D action4, Action3D action5)
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
    if (index >= actions.size)
      return true;
    Pool<Action3D> pool = getPool();
    setPool(null); // Ensure this action can't be returned to the pool while
                   // executings.
    try
    {
      if (actions.get(index).act(delta))
      {
        if (model3D == null)
          return true; // This action was removed.
        index++;
        if (index >= actions.size)
          return true;
      }
      return false;
    } finally
    {
      setPool(pool);
    }
  }

  @Override
  public void restart()
  {
    super.restart();
    index = 0;
  }
}