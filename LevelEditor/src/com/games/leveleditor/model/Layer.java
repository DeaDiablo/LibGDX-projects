package com.games.leveleditor.model;

import java.util.Vector;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.shellGDX.model2D.Group2D;

public class Layer extends Group2D
{
  private Vector<EditModel> models = new Vector<EditModel>();
  
  public Layer(String name)
  {
    super();
    setName(name);
  }
  
  public void saveLayer(Element elementLayer)
  {
    elementLayer.setAttribute("name", getName());
    for(int i = 0; i < models.size(); i++)
    {
      EditModel model = models.get(i);
      
      Element elementModel = new Element("Model", elementLayer);
      model.saveModel(elementModel);
      elementLayer.addChild(elementModel);
    }
  }
  
  public void addModel(EditModel model)
  {
    models.add(model);
    addActor(model);
  }
  
  public void removeModel(EditModel model)
  {
    models.remove(model);
    removeActor(model);
  }
  
  public Vector<EditModel> getModels()
  {
    return models;
  }
  
  public Vector<EditModel> getSelectedModels()
  {
    Vector<EditModel> selectModels = new Vector<EditModel>();
    for(int i = 0; i < models.size(); i++)
    {
      EditModel model = models.get(i);
      if (model.isSelected())
        selectModels.add(model);
    }
    return selectModels;
  }
  
  @Override
  public void draw(Batch batch, float parentAlpha)
  {
    super.draw(batch, parentAlpha);
    for(int i = 0; i < models.size(); i++)
    {
      EditModel model = models.get(i);
      if (model.isSelected())
        model.drawSelect(batch, parentAlpha);
    }
  }
}
