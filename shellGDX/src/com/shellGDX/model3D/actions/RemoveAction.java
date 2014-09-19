package com.shellGDX.model3D.actions;

import com.shellGDX.model3D.Action3D;
import com.shellGDX.model3D.ModelObject3D;

/**
 * Removes an action from an model.
 * 
 * @author Nathan Sweet
 */
public class RemoveAction extends Action3D
{
  private ModelObject3D  targetModel;
  private Action3D action;

  @Override
  public boolean act(float delta)
  {
    (targetModel != null ? targetModel : model3D).removeAction3d(action);
    return true;
  }

  public ModelObject3D getTargetModel()
  {
    return targetModel;
  }

  /**
   * Sets the model to remove an action from. If null (the default), the
   * {@link #getModel() model} will be used.
   */
  public void setTargetModel(ModelObject3D model)
  {
    this.targetModel = model;
  }

  public Action3D getAction()
  {
    return action;
  }

  public void setAction(Action3D action)
  {
    this.action = action;
  }

  @Override
  public void reset()
  {
    super.reset();
    targetModel = null;
    action = null;
  }
}