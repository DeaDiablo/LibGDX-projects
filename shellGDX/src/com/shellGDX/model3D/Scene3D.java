package com.shellGDX.model3D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.SnapshotArray;

public class Scene3D extends InputAdapter implements Disposable
{
  private float             width, height;
  private final ModelBatch  modelBatch;
  private Environment       environment;
  protected boolean         resize = false;

  private PerspectiveCamera camera;

  private final Group3D     root;
  private ModelObject3D           scrollFocus;
  private ModelObject3D           keyboardFocus;

  public Touchable          touchable = Touchable.disabled;
  private int               selecting = -1;

  private boolean           canHit    = false;

  /**
   * Creates a scene with a {@link #setViewport(float, float, boolean) viewport}
   * equal to the device screen resolution. The scene will use its own
   * {@link SpriteBatch}.
   */
  public Scene3D()
  {
    this(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
    resize = true;
  }

  /**
   * Creates a scene with the specified
   * {@link #setViewport(float, float, boolean) viewport} that doesn't keep the
   * aspect ratio. The scene will use its own {@link SpriteBatch}, which will be
   * disposed when the scene is disposed.
   */
  public Scene3D(float width, float height)
  {
    this(width, height, false);
    resize = false;
  }

  public Scene3D(float width, float height, boolean keepAspectRatio)
  {
    this.width = width;
    this.height = height;

    root = new Group3D();
    root.setScene(this);

    modelBatch = new ModelBatch();

    camera = new PerspectiveCamera();
    environment = new Environment();
    environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.9f, 0.9f, 0.9f, 1f));
    environment.add(new DirectionalLight().set(0.8f, 0f, 0f, -1f, -0.8f, -0.2f));

    setViewport(width, height, keepAspectRatio);
    resize = false;
  }

  public Scene3D(float width, float height, PerspectiveCamera camera)
  {
    this.width = width;
    this.height = height;
    root = new Group3D();
    root.setScene(this);
    modelBatch = new ModelBatch();
    this.camera = camera;
    resize = false;
  }

  public Scene3D(float width, float height, PerspectiveCamera camera, Environment environment)
  {
    this.width = width;
    this.height = height;
    root = new Group3D();
    root.setScene(this);
    modelBatch = new ModelBatch();
    this.camera = camera;
    this.environment = environment;
    resize = false;
  }

  public void setViewport(float width, float height)
  {
    setViewport(width, height, false, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
  }

  /**
   * Sets up the scene size using a viewport that fills the entire screen.
   * 
   * @see #setViewport(float, float, boolean, float, float, float, float)
   */
  public void setViewport(float width, float height, boolean keepAspectRatio)
  {
    setViewport(width, height, keepAspectRatio, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
  }

  /**
   * Sets up the scene size and viewport. The viewport is the glViewport
   * position and size, which is the portion of the screen used by the scene.
   * The scene size determines the units used within the scene, depending on
   * keepAspectRatio:
   * <p>
   * If keepAspectRatio is false, the scene is stretched to fill the viewport,
   * which may distort the aspect ratio.
   * <p>
   * If keepAspectRatio is true, the scene is first scaled to fit the viewport
   * in the longest dimension. Next the shorter dimension is lengthened to fill
   * the viewport, which keeps the aspect ratio from changing. The
   * {@link #getGutterWidth()} and {@link #getGutterHeight()} provide access to
   * the amount that was lengthened.
   * 
   * @param viewportX
   *          The top left corner of the viewport in glViewport coordinates (the
   *          origin is bottom left).
   * @param viewportY
   *          The top left corner of the viewport in glViewport coordinates (the
   *          origin is bottom left).
   * @param viewportWidth
   *          The width of the viewport in pixels.
   * @param viewportHeight
   *          The height of the viewport in pixels.
   */
  public void setViewport(float sceneWidth, float sceneHeight, boolean keepAspectRatio,
                          float viewportX, float viewportY,
                          float viewportWidth, float viewportHeight)
  {
    if (keepAspectRatio)
    {
      if (viewportHeight / viewportWidth < sceneHeight / sceneWidth)
      {
        float toViewportSpace = viewportHeight / sceneHeight;
        float toSceneSpace = sceneHeight / viewportHeight;
        float deviceWidth = sceneWidth * toViewportSpace;
        float lengthen = (viewportWidth - deviceWidth) * toSceneSpace;
        this.width = sceneWidth + lengthen;
        this.height = sceneHeight;
      }
      else
      {
        float toViewportSpace = viewportWidth / sceneWidth;
        float toSceneSpace = sceneWidth / viewportWidth;
        float deviceHeight = sceneHeight * toViewportSpace;
        float lengthen = (viewportHeight - deviceHeight) * toSceneSpace;
        this.height = sceneHeight + lengthen;
        this.width = sceneWidth;
      }
    }
    else
    {
      this.width = sceneWidth;
      this.height = sceneHeight;
    }
    camera.viewportWidth = this.width;
    camera.viewportHeight = this.height;
  }
  
  public void setShader(Shader shader)
  {
    root.shader = shader;
  }
  
  public Shader getShader()
  {
    return root.shader;
  }

  public void draw()
  {
    camera.update();
    if (!root.isVisible())
      return;
    modelBatch.begin(camera);
    root.draw(modelBatch, environment, root.shader);
    modelBatch.end();
  }

  public void setResize(boolean resize)
  {
    this.resize = resize;
  }

  public boolean getResize()
  {
    return resize;
  }

  /** Calls {@link #act(float)} with {@link Graphics#getDeltaTime()}. */
  public void act()
  {
    act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
  }

  /**
   * Calls the {@link Model#act(float)} method on each model in the scene.
   * Typically called each frame. This method also fires enter and exit events.
   * 
   * @param delta
   *          Time in seconds since the last frame.
   */
  public void act(float delta)
  {
    root.update(delta);
  }

  /**
   * Adds an model to the root of the scene.
   * 
   * @see Group#addModel(Model)
   * @see Model#remove()
   */
  public void addModel3D(ModelObject3D model)
  {
    root.addModel3D(model);
  }

  /**
   * Adds an action to the root of the scene.
   * 
   * @see Group#addAction3D(Action)
   */
  public void addAction3D(Action3D action)
  {
    root.addAction3D(action);
  }

  /**
   * Returns the root's child models.
   * 
   * @see Group#getChildren()
   */
  public Array<ModelObject3D> getModels3d()
  {
    return root.getChildren();
  }

  /**
   * Adds a listener to the root.
   * 
   * @see Model#addListener(EventListener)
   */
  public boolean addListener(Event3DListener listener)
  {
    return root.addListener(listener);
  }

  /**
   * Removes a listener from the root.
   * 
   * @see Model#removeListener(EventListener)
   */
  public boolean removeListener(Event3DListener listener)
  {
    return root.removeListener(listener);
  }

  /** Removes the root's children, actions, and listeners. */
  public void clear()
  {
    unfocusAll();
    root.dispose();
    root.clear();
  }

  /** Removes the touch, keyboard, and scroll focused models. */
  public void unfocusAll()
  {
    scrollFocus = null;
    keyboardFocus = null;
    // cancelTouchFocus();
  }

  /**
   * Removes the touch, keyboard, and scroll focus for the specified model and
   * any descendants.
   */
  public void unfocus(ModelObject3D model)
  {
    if (scrollFocus != null && scrollFocus.isDescendantOf(model))
      scrollFocus = null;
    if (keyboardFocus != null && keyboardFocus.isDescendantOf(model))
      keyboardFocus = null;
  }

  /**
   * Sets the model that will receive key events.
   * 
   * @param model
   *          May be null.
   */
  public void setKeyboardFocus(ModelObject3D model)
  {
    if (keyboardFocus == model)
      return;
  }

  /**
   * Gets the model that will receive key events.
   * 
   * @return May be null.
   */
  public ModelObject3D getKeyboardFocus()
  {
    return keyboardFocus;
  }

  /**
   * Sets the model that will receive scroll events.
   * 
   * @param model
   *          May be null.
   */
  public void setScrollFocus(ModelObject3D model)
  {
    if (scrollFocus == model)
      return;
  }

  /**
   * Gets the model that will receive scroll events.
   * 
   * @return May be null.
   */
  public ModelObject3D getScrollFocus()
  {
    return scrollFocus;
  }

  public ModelBatch getModelBatch()
  {
    return modelBatch;
  }

  public PerspectiveCamera getCamera()
  {
    return camera;
  }

  /**
   * Sets the scene's camera. The camera must be configured properly or
   * {@link #setViewport(float, float, boolean)} can be called after the camera
   * is set. {@link Scene#draw()} will call {@link Camera#update()} and use the
   * {@link Camera#combined} matrix for the SpriteBatch
   * {@link SpriteBatch#setProjectionMatrix(com.badlogic.gdx.math.Matrix4)
   * projection matrix}.
   */
  public void setCamera(PerspectiveCamera camera)
  {
    this.camera = camera;
  }

  /** Returns the root group which holds all models in the scene. */
  public Group3D getRoot()
  {
    return root;
  }

  public void setEnvironment(Environment environment)
  {
    this.environment = environment;
  }

  public Environment getEnvironment()
  {
    return environment;
  }

  public void enableHit()
  {
    canHit = true;
  }

  public void disableHit()
  {
    canHit = false;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button)
  {
    if (canHit)
    {
      ModelObject3D model3d = getObject(screenX, screenY);
      selecting = model3d != null ? 1 : -1;
      if (model3d != null && model3d.getName() != null)
        Gdx.app.log("", "" + model3d.getName());
    }
    return selecting > 0;
    // return false;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button)
  {
    if (selecting >= 0)
    {
      // setSelected(getObject(screenX, screenY));
      selecting = -1;
      return true;
    }
    return false;
    // if(touchable == Touchable.enabled)
    // hit(screenX, screenY);
    // return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer)
  {
    return selecting >= 0;
  }

  Vector3 position = new Vector3();
  int     result   = -1;
  float   distance = -1;

  public ModelObject3D getObject(int screenX, int screenY)
  {
    ModelObject3D temp = null;
    SnapshotArray<ModelObject3D> children = root.getChildren();
    ModelObject3D[] models = children.begin();
    for (int i = 0, n = children.size; i < n; i++)
    {
      temp = hit3d(screenX, screenY, models[i]);
      if (models[i] instanceof Group3D)
        temp = hit3d(screenX, screenY, (Group3D) models[i]);
    }
    children.end();
    return temp;
  }

  public ModelObject3D hit3d(int screenX, int screenY, ModelObject3D model3d)
  {
    Ray ray = camera.getPickRay(screenX, screenY);
    float distance = -1;
    final float dist2 = model3d.intersects(ray);
    if (dist2 >= 0f && (distance < 0f || dist2 <= distance))
    {
      distance = dist2;
      return model3d;
    }
    return null;
  }

  public ModelObject3D hit3d(int screenX, int screenY, Group3D group3d)
  {
    ModelObject3D temp = null;
    SnapshotArray<ModelObject3D> children = group3d.getChildren();
    ModelObject3D[] models = children.begin();
    for (int i = 0, n = children.size; i < n; i++)
    {
      temp = hit3d(screenX, screenY, models[i]);
      if (models[i] instanceof Group3D)
        temp = hit3d(screenX, screenY, (Group3D) models[i]);
    }
    children.end();
    return temp;
  }

  @Override
  public void dispose()
  {
    modelBatch.dispose();
    clear();
  }
}
