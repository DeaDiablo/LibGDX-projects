package com.shellGDX.model3D.actions;

import com.shellGDX.model3D.Action3D;
import com.shellGDX.model3D.ModelObject3D;

import com.badlogic.gdx.utils.Array;

/**
 * Executes an action only after all other actions on the model at the time this
 * action was added have finished.
 * 
 * @author Nathan Sweet
 */
public class AfterAction extends DelegateAction
{
  private Array<Action3D> waitForActions = new Array<Action3D>(false, 4);

  @Override
  public void setModel3D(ModelObject3D model3D)
  {
    if (model3D != null)
      waitForActions.addAll(model3D.getActions3d());
    super.setModel3D(model3D);
  }

  @Override
  public void restart()
  {
    super.restart();
    waitForActions.clear();
  }

  @Override
  protected boolean delegate(float delta)
  {
    Array<Action3D> currentActions = model3D.getActions3d();
    if (currentActions.size == 1)
      waitForActions.clear();
    for (int i = waitForActions.size - 1; i >= 0; i--)
    {
      Action3D action = waitForActions.get(i);
      int index = currentActions.indexOf(action, true);
      if (index == -1)
        waitForActions.removeIndex(i);
    }
    if (waitForActions.size > 0)
      return false;
    return action.act(delta);
  }
}