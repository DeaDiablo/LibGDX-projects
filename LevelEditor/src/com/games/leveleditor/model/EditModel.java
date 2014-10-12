package com.games.leveleditor.model;

import java.io.IOException;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.shellGDX.manager.ResourceManager;
import com.shellGDX.model2D.ModelObject2D;

public class EditModel extends ModelObject2D implements SelectObject
{
  protected BoundingBox bb = null;
  
  public EditModel()
  {
    this("", null);
  }
  
  public EditModel(String name, TextureRegion region)
  {
    this(name, region, 0, 0);
  }
  
  public EditModel(String name, TextureRegion region, float x, float y)
  {
    super(region, x, y);
    setName(name);
    bb = new BoundingBox(getBound());
  }

  public EditModel copy()
  {
    EditModel newModel = new EditModel(getName(), region, getX(), getY());
    newModel.setVisible(isVisible());
    newModel.setRotation(getRotation());
    newModel.setScale(getScaleX(), getScaleY());
    newModel.setAlign(getHorzAlign(), getVertAlign());
    return newModel;
  }
  
  @Override
  public void load(Element element) throws IOException
  {
    setName(element.get("name"));
    setVisible(element.getBoolean("visible"));
    
    {
      Element texture = element.getChildByName("texture");
      String file = texture.get("file");
      int x = texture.getInt("x");
      int y = texture.getInt("y");
      int width = texture.getInt("width");
      int height = texture.getInt("height");
      setTextureRegion(ResourceManager.instance.getTextureRegion(file, x, y, width, height));
    }
    
    {
      Element position = element.getChildByName("position");
      setX(position.getFloat("x"));
      setY(position.getFloat("y"));
    }
    
    setRotation(element.getFloat("rotation"));

    {
      Element scale = element.getChildByName("scale");
      setScaleX(scale.getFloat("x"));
      setScaleY(scale.getFloat("y"));
    }
    
    setAlign(element.getInt("horzAlign"), element.getInt("vertAlign"));
  }
  
  @Override
  public void save(XmlWriter xml) throws IOException
  {
    xml.element("model");
    xml.element("name", getName());
    xml.element("visible", isVisible());
    
    {
      xml.element("texture");
      xml.element("file", ((FileTextureData)getTextureRegion().getTexture().getTextureData()).getFileHandle().path());
      xml.element("x", getTextureRegion().getRegionX());
      xml.element("y", getTextureRegion().getRegionY());
      xml.element("width", getTextureRegion().getRegionWidth());
      xml.element("height", getTextureRegion().getRegionHeight());
      xml.pop();
    }
    
    {
      xml.element("position");
      xml.element("x", getX());
      xml.element("y", getY());
      xml.pop();
    }
    
    xml.element("rotation", getRotation());
    
    {
      xml.element("scale");
      xml.element("x", getScaleX());
      xml.element("y", getScaleY());
      xml.pop();
    }
    
    xml.element("horzAlign", getHorzAlign());
    xml.element("vertAlign", getVertAlign());
    xml.pop();
  }

  @Override
  public BoundingBox getBoundingBox()
  {
    return bb;
  }

  protected boolean select = false;
  
  @Override
  public void setSelection(boolean select)
  {
    this.select = select;
  }
  
  @Override
  public boolean isSelected()
  {
    return select;
  }

  @Override
  public void drawBound()
  {
    if (!select)
      return;
    
    bb.draw();
  }
}
