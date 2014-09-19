package com.shellGDX.model3D.actions;

/**
 * Sets the model's rotation from its current value to a specific value.
 * 
 * @author Nathan Sweet
 */
public class RotateToAction extends TemporalAction
{
  private float startYaw, startPitch, startRoll;
  private float endYaw, endPitch, endRoll;

  @Override
  protected void begin()
  {
    if (model3D != null)
    {
      startYaw = model3D.getYaw();
      startPitch = model3D.getPitch();
      startRoll = model3D.getRoll();
    }
  }

  @Override
  protected void update(float percent)
  {
    model3D.setRotation(startYaw + (endYaw - startYaw) * percent, startPitch + (endPitch - startPitch) * percent,
                        startRoll + (endRoll - startRoll) * percent);
  }

  public void setRotation(float yaw, float pitch, float roll)
  {
    endYaw = yaw;
    endPitch = pitch;
    endRoll = roll;
  }

  public float getYaw()
  {
    return endYaw;
  }

  public void setYaw(float yaw)
  {
    endYaw = yaw;
  }

  public float getPitch()
  {
    return endPitch;
  }

  public void setPitch(float pitch)
  {
    endPitch = pitch;
  }

  public float getRoll()
  {
    return endRoll;
  }

  public void setRoll(float roll)
  {
    endRoll = roll;
  }
}