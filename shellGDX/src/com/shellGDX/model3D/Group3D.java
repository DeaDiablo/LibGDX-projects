package com.shellGDX.model3D;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;

public class Group3D extends ModelObject3D
{
  private final SnapshotArray<ModelObject3D> children = new SnapshotArray<ModelObject3D>(true, 4, ModelObject3D.class);
  public  int   visibleCount;

  public Group3D()
  {
    super();
  }

  public Group3D(Model model)
  {
    super(model);
  }

  @Override
  public boolean update(float delta)
  {
    super.update(delta);
    ModelObject3D[] models = children.begin();
    for (int i = 0, n = children.size; i < n; i++)
    {
      models[i].update(delta);
    }
    children.end();
    return isVisible();
  }

  /**
   * Draws the group and its children. The default implementation calls
   * {@link #applyTransform(Batch, Matrix4)} if needed, then
   * {@link #drawChildren(Batch, float)}, then {@link #resetTransform(Batch)} if
   * needed.
   */
  
  @Override
  public void draw(ModelBatch modelBatch, Environment environment, Shader shader)
  {
    if (model != null)
      super.draw(modelBatch, environment, shader);
    drawChildren(modelBatch, environment, shader);
  }

  public void drawChildren(ModelBatch modelBatch, Environment environment, Shader shader)
  {
    Shader renderShader = shader;
    if (this.shader != null)
      renderShader = this.shader;
    
    // modelBatch.render(children, environment); maybe faster
    SnapshotArray<ModelObject3D> children = this.children;
    ModelObject3D[] models = children.begin();
    visibleCount = 0;
    for (int i = 0, n = children.size; i < n; i++)
    {
      ModelObject3D child = models[i];
      if (!child.isVisible())
        continue;
      
      if (models[i] instanceof Group3D)
      {
        ((Group3D) models[i]).drawChildren(modelBatch, environment, renderShader);
      }
      else
      {
        child.translate(x, y, z);
        child.scale(scaleX, scaleY, scaleZ);
        child.rotate(yaw, pitch, roll);

        if (child.isCullable(modelBatch.getCamera()))
        {
          child.draw(modelBatch, environment, shader);
          visibleCount++;
        }
        
        child.translate(-x, -y, -z);
        child.scale(-scaleX, -scaleY, -scaleZ);
        child.rotate(-yaw, -pitch, -roll);
      }
    }
    children.end();
  }

  /**
   * Adds an model as a child of this group. The model is first removed from its
   * parent group, if any.
   * 
   * @see #remove()
   */
  public void addModel3D(ModelObject3D model3D)
  {
    model3D.remove();
    children.add(model3D);
    model3D.setParent(this);
    model3D.setScene(getScene3D());
    childrenChanged();
  }

  /**
   * Removes an model from this group. If the model will not be used again and
   * has actions, they should be {@link Model#clearActions3d() cleared} so the
   * actions will be returned to their
   * {@link Action#setPool(com.badlogic.gdx.utils.Pool) pool}, if any. This is
   * not done automatically.
   */
  public boolean removeModel3D(ModelObject3D model3D)
  {
    if (!children.removeValue(model3D, true))
      return false;
    Scene3D scene = getScene3D();
    if (scene != null)
      scene.unfocus(model3D);
    model3D.setParent(null);
    model3D.setScene(null);
    childrenChanged();
    return true;
  }

  /** Called when models are added to or removed from the group. */
  protected void childrenChanged()
  {
  }

  /** Removes all models from this group. */
  public void clearChildren()
  {
    ModelObject3D[] models = children.begin();
    for (int i = 0, n = children.size; i < n; i++)
    {
      ModelObject3D child = models[i];
      child.setScene(null);
      child.setParent(null);
    }
    children.end();
    children.clear();
    childrenChanged();
  }

  /** Removes all children, actions, and listeners from this group. */
  public void clear()
  {
    super.clear();
    clearChildren();
  }

  /**
   * Returns the first model found with the specified name. Note this
   * recursively compares the name of every model in the group.
   */
  public ModelObject3D findModel(String name)
  {
    Array<ModelObject3D> children = this.children;
    for (int i = 0, n = children.size; i < n; i++)
      if (name.equals(children.get(i).getName()))
        return children.get(i);
    for (int i = 0, n = children.size; i < n; i++)
    {
      ModelObject3D child = children.get(i);
      if (child instanceof Group3D)
      {
        ModelObject3D model = ((Group3D) child).findModel(name);
        if (model != null)
          return model;
      }
    }
    return null;
  }

  @Override
  protected void setScene(Scene3D scene3d)
  {
    super.setScene(scene3d);
    Array<ModelObject3D> children = this.children;
    for (int i = 0, n = children.size; i < n; i++)
      children.get(i).setScene(scene3d);
  }

  /** Returns an ordered list of child models in this group. */
  public SnapshotArray<ModelObject3D> getChildren()
  {
    return children;
  }

  public boolean hasChildren()
  {
    return children.size > 0;
  }

  /** Prints the model hierarchy recursively for debugging purposes. */
  public void print()
  {
    print("");
  }

  private void print(String indent)
  {
    ModelObject3D[] models = children.begin();
    for (int i = 0, n = children.size; i < n; i++)
    {
      System.out.println(indent + models[i]);
      if (models[i] instanceof Group3D)
        ((Group3D) models[i]).print(indent + "|  ");
    }
    children.end();
  }

  @Override
  public void dispose()
  {
    super.dispose();
    for (ModelObject3D model3D : children)
      model3D.dispose();
  }
}
