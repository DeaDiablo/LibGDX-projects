package com.shellGDX.model3D;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.Disposable;

public class ModelObject3D extends ModelInstance implements Disposable
{
  protected Scene3D                                  scene;
  protected Group3D                                  parent;

  private final DelayedRemovalArray<Event3DListener> listeners      = new DelayedRemovalArray<Event3DListener>(0);
  private final Array<Action3D>                      actions        = new Array<Action3D>(0);

  public final Vector3                               center         = new Vector3();
  public final Vector3                               dimensions     = new Vector3();
  private BoundingBox                                boundBox       = new BoundingBox();
  public final float                                 radius;

  private String                                     name;
  private boolean                                    visible        = true;

  protected float                                    x, y, z;
  protected float                                    scaleX         = 1, scaleY = 1, scaleZ = 1;
  protected float                                    maxScale       = 1;
  protected float                                    yaw            = 0f, pitch = 0f, roll = 0f;
  protected Matrix4                                  rotationMatrix = new Matrix4();
  private AnimationController                        animation;

  public ModelObject3D()
  {
    this(new Model());
    setScale(0, 0, 0);
  }

  public ModelObject3D(Model model)
  {
    this(model, 0f, 0f, 0f);
  }

  public ModelObject3D(Model model, float x, float y, float z)
  {
    super(model);
    setPosition(x, y, z);
    // boundBox = model.meshes.get(0).calculateBoundingBox();
    calculateBoundingBox(boundBox);
    center.set(boundBox.getCenter());
    dimensions.set(boundBox.getDimensions());
    radius = dimensions.len() / 2f;
    animation = new AnimationController(this);
  }

  /**
   * Updates the model3d based on time. Typically this is called each frame by
   * {@link Scene3D#update(float)}.
   * <p>
   * The default implementation calls {@link Action3D#update(float)} on each action
   * and removes actions that are complete.
   * 
   * @param delta
   *          Time in seconds since the last frame.
   */
  public boolean update(float delta)
  {
    for (int i = 0; i < actions.size; i++)
    {
      Action3D action3d = actions.get(i);
      if (action3d.act(delta) && i < actions.size)
      {
        actions.removeIndex(i);
        action3d.setModel3D(null);
        i--;
      }
    }
    if (animation.inAction)
      animation.update(delta);
    
    return isVisible();
  }
  
  protected Shader shader = null;
  
  public void setShader(Shader shader)
  {
    this.shader = shader;
  }
  
  public Shader getShader()
  {
    return shader;
  }
  
  public void draw(ModelBatch modelBatch, Environment environment, Shader shader)
  {
    Shader renderShader = shader;
    if (this.shader != null)
      renderShader = this.shader;
    
    if (renderShader != null)
    {
      modelBatch.render(this, renderShader);
      return;
    }
    
    modelBatch.render(this, environment);
  }

  public ModelObject3D hit(float x, float y)
  {
    return null;
  }

  /**
   * Removes this model3d from its parent, if it has a parent.
   * 
   * @see Group#removeModel3D(ModelObject3D)
   */
  public boolean remove()
  {
    if (parent != null)
      return parent.removeModel3D(this);
    return false;
  }

  /**
   * Add a listener to receive events that {@link #hit(float, float, boolean)
   * hit} this model3d. See {@link #fire(Event)}.
   * 
   * @see InputListener
   * @see ClickListener
   */
  public boolean addListener(Event3DListener listener)
  {
    if (!listeners.contains(listener, true))
    {
      listeners.add(listener);
      return true;
    }
    return false;
  }

  public boolean removeListener(Event3DListener listener)
  {
    return listeners.removeValue(listener, true);
  }

  public Array<Event3DListener> getListeners()
  {
    return listeners;
  }

  public void addAction3D(Action3D action3D)
  {
    action3D.setModel3D(this);
    actions.add(action3D);
  }

  public void removeAction3d(Action3D action)
  {
    if (actions.removeValue(action, true))
      action.setModel3D(null);
  }

  public Array<Action3D> getActions3d()
  {
    return actions;
  }

  /** Removes all actions on this model3d. */
  public void clearActions3d()
  {
    for (int i = actions.size - 1; i >= 0; i--)
      actions.get(i).setModel3D(null);
    actions.clear();
  }

  /** Removes all listeners on this model3d. */
  public void clearListeners()
  {
    listeners.clear();
  }

  /** Removes all actions and listeners on this model3d. */
  public void clear()
  {
    clearActions3d();
    clearListeners();
  }

  /**
   * Called by the framework when this model3d or any parent is added to a group
   * that is in the scene3d.
   * 
   * @param scene3d
   *          May be null if the model3d or any parent is no longer in a scene.
   */
  protected void setScene(Scene3D scene3d)
  {
    this.scene = scene3d;
  }

  /**
   * Returns the scene3d that this model3d is currently in, or null if not in a
   * scene.
   */
  public Scene3D getScene3D()
  {
    return scene;
  }

  /**
   * Returns true if this model3d is the same as or is the descendant of the
   * specified model3d.
   */
  public boolean isDescendantOf(ModelObject3D model3d)
  {
    if (model3d == null)
      throw new IllegalArgumentException("model3d cannot be null.");
    ModelObject3D parent = this;
    while (true)
    {
      if (parent == null)
        return false;
      if (parent == model3d)
        return true;
      parent = parent.parent;
    }
  }

  /**
   * Returns true if this model3d is the same as or is the ascendant of the
   * specified model3d.
   */
  public boolean isAscendantOf(ModelObject3D model3d)
  {
    if (model3d == null)
      throw new IllegalArgumentException("model3d cannot be null.");
    while (true)
    {
      if (model3d == null)
        return false;
      if (model3d == this)
        return true;
      model3d = model3d.parent;
    }
  }

  /** Returns true if the model3d's parent is not null. */
  public boolean hasParent()
  {
    return parent != null;
  }

  /** Returns the parent model3d, or null if not in a scene. */
  public Group3D getParent()
  {
    return parent;

  }

  /**
   * Called by the framework when an model3d is added to or removed from a
   * group.
   * 
   * @param parent
   *          May be null if the model3d has been removed from the parent.
   */
  protected void setParent(Group3D parent)
  {
    this.parent = parent;
  }

  private static final Vector3 position = new Vector3();

  public boolean isCullable(final Camera cam)
  {
    return cam.frustum.sphereInFrustum(getTransform().getTranslation(position).add(center), radius * getMaxScale());
  }

  public boolean isVisible()
  {
    return visible;
  }

  /**
   * If false, the model3d will not be drawn and will not receive touch events.
   * Default is true.
   */
  public void setVisible(boolean visible)
  {
    this.visible = visible;
  }

  /**
   * @return -1 on no intersection, or when there is an intersection: the squared
   *         distance between the center of this object and the point on the ray
   *         closest to this object when there is intersection.
   */
  public float intersects(Ray ray)
  {
    transform.getTranslation(position).add(center);
    final float len = ray.direction.dot(position.x - ray.origin.x, position.y - ray.origin.y, position.z - ray.origin.z);
    if (len < 0f)
      return -1f;
    float dist2 = position.dst2(ray.origin.x + ray.direction.x * len, ray.origin.y + ray.direction.y * len, ray.origin.z + ray.direction.z * len);
    return (dist2 <= radius * radius) ? dist2 : -1f;
  }

  public void setPosition(float x, float y, float z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
    transform.setToTranslationAndScaling(this.x, this.y, this.z, scaleX, scaleY, scaleZ);
    transform.mul(rotationMatrix);
  }

  public void translate(float x, float y, float z)
  {
    this.x += x;
    this.y += y;
    this.z += z;
    transform.setToTranslationAndScaling(this.x, this.y, this.z, scaleX, scaleY, scaleZ);
    transform.mul(rotationMatrix);
  }

  /*
   * Set the model3d's rotation values to new yaw, pitch and roll
   * 
   * @param newYaw, newPitch, newRoll these values must be within 360 degrees
   */
  public void setRotation(float newYaw, float newPitch, float newRoll)
  {
    yaw = newYaw;
    pitch = newPitch;
    roll = newRoll;
    rotationMatrix = transform.setFromEulerAngles(yaw, pitch, roll).cpy();
    transform.setToTranslationAndScaling(x, y, z, scaleX, scaleY, scaleZ);
    transform.mul(rotationMatrix);
  }

  /*
   * Set the model3d's yaw
   * 
   * @param newYaw value must be within 360 degrees
   */
  public void setYaw(float newYaw)
  {
    yaw = newYaw;
    rotationMatrix = transform.setFromEulerAngles(yaw, pitch, roll).cpy();
    transform.setToTranslationAndScaling(x, y, z, scaleX, scaleY, scaleZ);
    transform.mul(rotationMatrix);
  }

  /*
   * Set the model3d's pitch
   * 
   * @param newPitch value must be within 360 degrees
   */
  public void setPitch(float newPitch)
  {
    pitch = newPitch;
    rotationMatrix = transform.setFromEulerAngles(yaw, pitch, roll).cpy();
    transform.setToTranslationAndScaling(x, y, z, scaleX, scaleY, scaleZ);
    transform.mul(rotationMatrix);
  }

  /*
   * Set the model3d's roll
   * 
   * @param newRoll value must be within 360 degrees
   */
  public void setRoll(float newRoll)
  {
    roll = newRoll;
    rotationMatrix = transform.setFromEulerAngles(yaw, pitch, roll).cpy();
    transform.setToTranslationAndScaling(x, y, z, scaleX, scaleY, scaleZ);
    transform.mul(rotationMatrix);
  }

  public static float normalizeDegrees(float degrees)
  {
    float newAngle = degrees;
    while (newAngle < -360)
      newAngle += 360;
    while (newAngle > 360)
      newAngle -= 360;
    return newAngle;
  }

  /*
   * Rotates the model3d by the amount of yaw, pitch and roll
   * 
   * @param amountYaw,amountPitch,amountRoll These values must be within 360
   * degrees
   */
  public void rotate(float amountYaw, float amountPitch, float amountRoll)
  {
    yaw = normalizeDegrees(yaw + amountYaw);
    pitch = normalizeDegrees(pitch + amountPitch);
    roll = normalizeDegrees(roll + amountRoll);
    rotationMatrix = transform.setFromEulerAngles(yaw, pitch, roll).cpy();
    transform.setToTranslationAndScaling(x, y, z, scaleX, scaleY, scaleZ);
    transform.mul(rotationMatrix);
  }

  public void rotateYaw(float amountYaw)
  {
    yaw = normalizeDegrees(yaw + amountYaw);
    rotationMatrix = transform.setFromEulerAngles(yaw, pitch, roll).cpy();
    transform.setToTranslationAndScaling(x, y, z, scaleX, scaleY, scaleZ);
    transform.mul(rotationMatrix);
  }

  public void rotatePitch(float amountPitch)
  {
    pitch = normalizeDegrees(pitch + amountPitch);
    rotationMatrix = transform.setFromEulerAngles(yaw, pitch, roll).cpy();
    transform.setToTranslationAndScaling(x, y, z, scaleX, scaleY, scaleZ);
    transform.mul(rotationMatrix);
  }

  public void rotateRoll(float amountRoll)
  {
    roll = normalizeDegrees(roll + amountRoll);
    rotationMatrix = transform.setFromEulerAngles(yaw, pitch, roll).cpy();
    transform.setToTranslationAndScaling(x, y, z, scaleX, scaleY, scaleZ);
    transform.mul(rotationMatrix);
  }

  public float getYaw()
  {
    return yaw;
  }

  public float getPitch()
  {
    return pitch;
  }

  public float getRoll()
  {
    return roll;
  }

  public void setScale(float scaleX, float scaleY, float scaleZ)
  {
    this.scaleX = scaleX;
    this.scaleY = scaleY;
    this.scaleZ = scaleZ;   
    transform.setToScaling(scaleX, scaleY, scaleZ);
    updateMaxScale();
  }

  public void setScale(float scale)
  {
    this.scaleX = scale;
    this.scaleY = scale;
    this.scaleZ = scale;
    transform.setToScaling(scaleX, scaleY, scaleZ);
    updateMaxScale();
  }

  /** Adds the specified scale to the current scale. */
  public void scale(float scale)
  {
    scaleX += scale;
    scaleY += scale;
    scaleZ += scale;
    transform.scl(scale); // re-implement this
    updateMaxScale();
  }

  public void scale(float scaleX, float scaleY, float scaleZ)
  {
    this.scaleX += scaleX;
    this.scaleY += scaleY;
    this.scaleZ += scaleZ;
    transform.scl(scaleX, scaleY, scaleZ); // re-implement this
    updateMaxScale();
  }

  public void setX(float x)
  {
    this.x = x;
    transform.setToTranslation(x, y, z);
  }

  public float getX()
  {
    return x;
  }

  public void setY(float y)
  {
    this.y = y;
    transform.setToTranslation(x, y, z);
  }

  public float getY()
  {
    return y;
  }

  public void setZ(float z)
  {
    this.z = z;
    transform.setToTranslation(x, y, z);
  }

  public float getZ()
  {
    return z;
  }

  public void setScaleX(float scaleX)
  {
    this.scaleX = scaleX;
    transform.scale(scaleX, scaleY, scaleZ);
  }

  public float getScaleX()
  {
    return scaleX;
  }

  public void setScaleY(float scaleY)
  {
    this.scaleY = scaleY;
    transform.scale(scaleX, scaleY, scaleZ);
  }

  public float getScaleY()
  {
    return scaleY;
  }

  public void setScaleZ(float scaleZ)
  {
    this.scaleY = scaleZ;
    transform.scale(scaleX, scaleY, scaleZ);
  }

  public float getScaleZ()
  {
    return scaleZ;
  }
  
  public void updateMaxScale()
  {
    maxScale = scaleX;
    if (scaleY > maxScale)
      maxScale = scaleY;
    if (scaleZ > maxScale)
      maxScale = scaleZ;
  }
  
  public float getMaxScale()
  {
    return maxScale;
  }

  /**
   * Sets a name for easier identification of the model3d in application code.
   * 
   * @see Group#findModel(String)
   */
  public void setName(String name)
  {
    this.name = name;

  }

  public String getName()
  {
    return name;
  }

  public String toString()
  {
    String name = this.name;
    if (name == null)
    {
      name = getClass().getName();
      int dotIndex = name.lastIndexOf('.');
      if (dotIndex != -1)
        name = name.substring(dotIndex + 1);
    }
    return name;
  }

  public Color getColor()
  {
    return ((ColorAttribute) getMaterial("Color").get(ColorAttribute.Diffuse)).color;
  }

  public void setColor(Color color)
  {
    ColorAttribute ca = new ColorAttribute(ColorAttribute.Diffuse, color);
    if (getMaterial("Color") != null)
      getMaterial("Color").set(ca);
    else
      materials.add(new Material("Color", ca));
    model.materials.add(new Material("Color", ca));
  }

  public Matrix4 getTransform()
  {
    return transform;
  }

  public void setTransform(Matrix4 transform)
  {
    this.transform = transform;
  }

  public BoundingBox getBoundingBox()
  {
    return boundBox;
  }

  public void setBoundingBox(BoundingBox box)
  {
    boundBox = box;
  }

  public AnimationController getAnimation()
  {
    return animation;
  }

  @Override
  public void dispose()
  {
  }
}