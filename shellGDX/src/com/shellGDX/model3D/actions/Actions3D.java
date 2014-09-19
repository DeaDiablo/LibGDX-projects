package com.shellGDX.model3D.actions;

import com.shellGDX.model3D.Action3D;
import com.shellGDX.model3D.ModelObject3D;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

public class Actions3D
{

  /** Returns a new or pooled action of the specified type. */
  static public <T extends Action3D> T action3D(Class<T> type)
  {
    Pool<T> pool = Pools.get(type);
    T action = pool.obtain();
    action.setPool(pool);
    return action;
  }

  static public AddAction addAction(Action3D action)
  {
    AddAction addAction = action3D(AddAction.class);
    addAction.setAction(action);
    return addAction;
  }

  static public AddAction addAction(Action3D action, ModelObject3D targetModel)
  {
    AddAction addAction = action3D(AddAction.class);
    addAction.setTargetModel(targetModel);
    addAction.setAction(action);
    return addAction;
  }

  static public RemoveAction removeAction(Action3D action)
  {
    RemoveAction removeAction = action3D(RemoveAction.class);
    removeAction.setAction(action);
    return removeAction;
  }

  static public RemoveAction removeAction(Action3D action, ModelObject3D targetModel)
  {
    RemoveAction removeAction = action3D(RemoveAction.class);
    removeAction.setTargetModel(targetModel);
    removeAction.setAction(action);
    return removeAction;
  }

  /** Moves the model instantly. */
  static public MoveToAction moveTo(float x, float y, float z)
  {
    return moveTo(x, y, z, 0, null);
  }

  static public MoveToAction moveTo(float x, float y, float z, float duration)
  {
    return moveTo(x, y, z, duration, null);
  }

  static public MoveToAction moveTo(float x, float y, float z, float duration, Interpolation interpolation)
  {
    MoveToAction action = action3D(MoveToAction.class);
    action.setPosition(x, y, z);
    action.setDuration(duration);
    action.setInterpolation(interpolation);
    return action;
  }

  /** Moves the model instantly. */
  static public MoveByAction moveBy(float amountX, float amountY, float amountZ)
  {
    return moveBy(amountX, amountY, amountZ, 0, null);
  }

  static public MoveByAction moveBy(float amountX, float amountY, float amountZ, float duration)
  {
    return moveBy(amountX, amountY, amountZ, duration, null);
  }

  static public MoveByAction moveBy(float amountX, float amountY, float amountZ, float duration, Interpolation interpolation)
  {
    MoveByAction action = action3D(MoveByAction.class);
    action.setAmount(amountX, amountY, amountZ);
    action.setDuration(duration);
    action.setInterpolation(interpolation);
    return action;
  }

  /** Scales the model instantly. */
  static public ScaleToAction scaleTo(float x, float y, float z)
  {
    return scaleTo(x, y, z, 0, null);
  }

  static public ScaleToAction scaleTo(float x, float y, float z, float duration)
  {
    return scaleTo(x, y, z, duration, null);
  }

  static public ScaleToAction scaleTo(float x, float y, float z, float duration, Interpolation interpolation)
  {
    ScaleToAction action = action3D(ScaleToAction.class);
    action.setScale(x, y, z);
    action.setDuration(duration);
    action.setInterpolation(interpolation);
    return action;
  }

  /** Scales the model instantly. */
  static public ScaleByAction scaleBy(float amountX, float amountY, float amountZ)
  {
    return scaleBy(amountX, amountY, amountZ, 0, null);
  }

  static public ScaleByAction scaleBy(float amountX, float amountY, float amountZ, float duration)
  {
    return scaleBy(amountX, amountY, amountZ, duration, null);
  }

  static public ScaleByAction scaleBy(float amountX, float amountY, float amountZ, float duration, Interpolation interpolation)
  {
    ScaleByAction action = action3D(ScaleByAction.class);
    action.setAmount(amountX, amountY, amountZ);
    action.setDuration(duration);
    action.setInterpolation(interpolation);
    return action;
  }

  /** Rotates the model instantly. */
  static public RotateToAction rotateTo(float yaw, float pitch, float roll)
  {
    return rotateTo(yaw, pitch, roll, 0, null);
  }

  static public RotateToAction rotateTo(float yaw, float pitch, float roll, float duration)
  {
    return rotateTo(yaw, pitch, roll, duration, null);
  }

  static public RotateToAction rotateTo(float yaw, float pitch, float roll, float duration, Interpolation interpolation)
  {
    RotateToAction action = action3D(RotateToAction.class);
    action.setRotation(yaw, pitch, roll);
    action.setDuration(duration);
    action.setInterpolation(interpolation);
    return action;
  }

  /** Rotates the model instantly. */
  static public RotateByAction rotateBy(float yaw, float pitch, float roll)
  {
    return rotateBy(yaw, pitch, roll, 0, null);
  }

  static public RotateByAction rotateBy(float yaw, float pitch, float roll, float duration)
  {
    return rotateBy(yaw, pitch, roll, duration, null);
  }

  static public RotateByAction rotateBy(float yaw, float pitch, float roll, float duration, Interpolation interpolation)
  {
    RotateByAction action = action3D(RotateByAction.class);
    action.setAmount(yaw, pitch, roll);
    action.setDuration(duration);
    action.setInterpolation(interpolation);
    return action;
  }

  static public VisibleAction show()
  {
    return visible(true);
  }

  static public VisibleAction hide()
  {
    return visible(false);
  }

  static public VisibleAction visible(boolean visible)
  {
    VisibleAction action = action3D(VisibleAction.class);
    action.setVisible(visible);
    return action;
  }

  static public RemoveModelAction removeModel(ModelObject3D removeModel)
  {
    RemoveModelAction action = action3D(RemoveModelAction.class);
    action.setRemoveModel(removeModel);
    return action;
  }

  static public DelayAction delay(float duration)
  {
    DelayAction action = action3D(DelayAction.class);
    action.setDuration(duration);
    return action;
  }

  static public DelayAction delay(float duration, Action3D delayedAction)
  {
    DelayAction action = action3D(DelayAction.class);
    action.setDuration(duration);
    action.setAction(delayedAction);
    return action;
  }

  static public TimeScaleAction timeScale(float scale, Action3D scaledAction)
  {
    TimeScaleAction action = action3D(TimeScaleAction.class);
    action.setScale(scale);
    action.setAction(scaledAction);
    return action;
  }

  static public SequenceAction sequence(Action3D action1)
  {
    SequenceAction action = action3D(SequenceAction.class);
    action.addAction(action1);
    return action;
  }

  static public SequenceAction sequence(Action3D action1, Action3D action2)
  {
    SequenceAction action = action3D(SequenceAction.class);
    action.addAction(action1);
    action.addAction(action2);
    return action;
  }

  static public SequenceAction sequence(Action3D action1, Action3D action2, Action3D action3)
  {
    SequenceAction action = action3D(SequenceAction.class);
    action.addAction(action1);
    action.addAction(action2);
    action.addAction(action3);
    return action;
  }

  static public SequenceAction sequence(Action3D action1, Action3D action2, Action3D action3, Action3D action4)
  {
    SequenceAction action = action3D(SequenceAction.class);
    action.addAction(action1);
    action.addAction(action2);
    action.addAction(action3);
    action.addAction(action4);
    return action;
  }

  static public SequenceAction sequence(Action3D action1, Action3D action2, Action3D action3, Action3D action4, Action3D action5)
  {
    SequenceAction action = action3D(SequenceAction.class);
    action.addAction(action1);
    action.addAction(action2);
    action.addAction(action3);
    action.addAction(action4);
    action.addAction(action5);
    return action;
  }

  static public SequenceAction sequence(Action3D... actions)
  {
    SequenceAction action = action3D(SequenceAction.class);
    for (int i = 0, n = actions.length; i < n; i++)
      action.addAction(actions[i]);
    return action;
  }

  static public SequenceAction sequence()
  {
    return action3D(SequenceAction.class);
  }

  static public ParallelAction parallel(Action3D action1)
  {
    ParallelAction action = action3D(ParallelAction.class);
    action.addAction(action1);
    return action;
  }

  static public ParallelAction parallel(Action3D action1, Action3D action2)
  {
    ParallelAction action = action3D(ParallelAction.class);
    action.addAction(action1);
    action.addAction(action2);
    return action;
  }

  static public ParallelAction parallel(Action3D action1, Action3D action2, Action3D action3)
  {
    ParallelAction action = action3D(ParallelAction.class);
    action.addAction(action1);
    action.addAction(action2);
    action.addAction(action3);
    return action;
  }

  static public ParallelAction parallel(Action3D action1, Action3D action2, Action3D action3, Action3D action4)
  {
    ParallelAction action = action3D(ParallelAction.class);
    action.addAction(action1);
    action.addAction(action2);
    action.addAction(action3);
    action.addAction(action4);
    return action;
  }

  static public ParallelAction parallel(Action3D action1, Action3D action2, Action3D action3, Action3D action4, Action3D action5)
  {
    ParallelAction action = action3D(ParallelAction.class);
    action.addAction(action1);
    action.addAction(action2);
    action.addAction(action3);
    action.addAction(action4);
    action.addAction(action5);
    return action;
  }

  static public ParallelAction parallel(Action3D... actions)
  {
    ParallelAction action = action3D(ParallelAction.class);
    for (int i = 0, n = actions.length; i < n; i++)
      action.addAction(actions[i]);
    return action;
  }

  static public ParallelAction parallel()
  {
    return action3D(ParallelAction.class);
  }

  static public RepeatAction repeat(int count, Action3D repeatedAction)
  {
    RepeatAction action = action3D(RepeatAction.class);
    action.setCount(count);
    action.setAction(repeatedAction);
    return action;
  }

  static public RepeatAction forever(Action3D repeatedAction)
  {
    RepeatAction action = action3D(RepeatAction.class);
    action.setCount(RepeatAction.FOREVER);
    action.setAction(repeatedAction);
    return action;
  }

  static public RunnableAction run(Runnable runnable)
  {
    RunnableAction action = action3D(RunnableAction.class);
    action.setRunnable(runnable);
    return action;
  }

  static public AfterAction after(Action3D action)
  {
    AfterAction afterAction = action3D(AfterAction.class);
    afterAction.setAction(action);
    return afterAction;
  }

}
