package com.shellGDX.model3D.actions;

import com.shellGDX.model3D.Action3D;
import com.shellGDX.model3D.ModelObject3D;

/**
 * Removes an model from the scene.
 * 
 * @author Nathan Sweet
 */
public class RemoveModelAction extends Action3D
{
  private ModelObject3D removeModel;
  private boolean removed;

  @Override
  public boolean act(float delta)
  {
    if (!removed)
    {
      removed = true;
      (removeModel != null ? removeModel : model3D).remove();
    }
    return true;
  }

  @Override
  public void restart()
  {
    removed = false;
  }

  @Override
  public void reset()
  {
    super.reset();
    removeModel = null;
  }

  public ModelObject3D getRemoveModel()
  {
    return removeModel;
  }

  /**
   * Sets the model to remove. If null (the default), the {@link #getModel()
   * model} will be used.
   */
  public void setRemoveModel(ModelObject3D removeModel)
  {
    this.removeModel = removeModel;
  }
}