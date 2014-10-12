package com.games.leveleditor.model;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.shellGDX.GameInstance;

public class BoundingBox
{
  protected final float sizeBigCube = 9.0f;
  protected final float deltaBigCube = (float)Math.ceil(sizeBigCube * 0.5f);
  protected final float sizeSmallCube = 7.0f;
  protected final float deltaSmallCube = (float)Math.ceil(sizeSmallCube * 0.5f);

  protected ShapeRenderer shapeRenderer = null;
  protected Rectangle bound = null;
  protected Array<Rectangle> scaleRectangles = new Array<Rectangle>(false, 8);

  public BoundingBox(Rectangle bound)
  {
    this.bound = bound;
    shapeRenderer = GameInstance.view.getShapeRenderer();
    for (int i = 0; i < 4; i ++)
      scaleRectangles.add(new Rectangle(0, 0, sizeBigCube, sizeBigCube));
    for (int i = 0; i < 4; i ++)
      scaleRectangles.add(new Rectangle(0, 0, sizeSmallCube, sizeSmallCube));
  }

  public void draw()
  {
    //big cubes
    scaleRectangles.get(0).setPosition(bound.x - deltaBigCube, bound.y - deltaBigCube);
    scaleRectangles.get(1).setPosition(bound.x + bound.width - deltaBigCube, bound.y + bound.height - deltaBigCube);
    scaleRectangles.get(2).setPosition(bound.x - deltaBigCube, bound.y + bound.height - deltaBigCube);
    scaleRectangles.get(3).setPosition(bound.x + bound.width - deltaBigCube, bound.y - deltaBigCube);
    //small cubes
    scaleRectangles.get(4).setPosition(bound.x + bound.width - deltaSmallCube, bound.y + bound.height * 0.5f - deltaSmallCube);
    scaleRectangles.get(5).setPosition(bound.x - deltaSmallCube, bound.y + bound.height * 0.5f - deltaSmallCube);
    scaleRectangles.get(6).setPosition(bound.x + bound.width * 0.5f  - deltaSmallCube, bound.y + bound.height - deltaSmallCube);
    scaleRectangles.get(7).setPosition(bound.x + bound.width * 0.5f - deltaSmallCube, bound.y - deltaSmallCube);

    shapeRenderer.setColor(0.8f, 0.8f, 0.8f, 1);

    shapeRenderer.begin(ShapeType.Line);
    shapeRenderer.rect(bound.x, bound.y, bound.width, bound.height);
    shapeRenderer.end();

    shapeRenderer.begin(ShapeType.Filled);
    for (int i = 0; i < scaleRectangles.size; i ++)
    {
      Rectangle rect = scaleRectangles.get(i);
      shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
    }
    shapeRenderer.end();
  }

  public Operation getOperationType(Vector2 touch)
  {
    if (scaleRectangles.get(0).contains(touch))
      return Operation.SCALE_X0Y0;
    if (scaleRectangles.get(1).contains(touch))
      return Operation.SCALE_X1Y1;
    
    if (scaleRectangles.get(2).contains(touch))
      return Operation.SCALE_X0Y1;
    if (scaleRectangles.get(3).contains(touch))
      return Operation.SCALE_X1Y0;
    
    if (scaleRectangles.get(4).contains(touch))
      return Operation.SCALE_PLUS_X;
    if (scaleRectangles.get(5).contains(touch))
      return Operation.SCALE_MINUS_X;
    
    if (scaleRectangles.get(6).contains(touch))
      return Operation.SCALE_PLUS_Y;
    if (scaleRectangles.get(7).contains(touch))
      return Operation.SCALE_MINUS_Y;
    
    if (bound.contains(touch))
      return Operation.TRANSLATE;
    
    return Operation.ROTATE;
  }
}
