package tatoo.model.entities;

import javax.swing.event.EventListenerList;

import tatoo.model.conditions.Condition;
import tatoo.model.conditions.NumberCondition;


public class EntityModel implements ArmyListEntityModel, EntityListener{

	/** the Entity this model hides */
  AbstractEntity entity = null;
  /** List of objects listening on Events from this Model. */
  private EventListenerList listenerList = new EventListenerList();

  /**
   * Add a Listener to this Model
   * @param l the ModelListener
   */
  public void addEntityModelListener(EntityModelListener l) {
    listenerList.add(EntityModelListener.class, l);
  }
  
  /**
   * remove a Listener from this Model
   * @param l the ModelListener
   */
  public void removeEntityModelListener(EntityModelListener l) {
    listenerList.remove(EntityModelListener.class, l);
  }

  public EntityModel(Object o) {
    setSource(o);
  }
  
  public EntityModel() {
   
  }

  @Override
  public Condition<Integer> getCount() {
    return entity.getCount();
  }
  
	public void setCount(int count) {
		entity.setCount(count);
//		fireAttribChanged(new EntityModelEvent(entity,"Count"));
	}

  @Override
  public Condition<Integer> getMaxCount() {
    return entity.getMaxCount();
  }

  public void setMaxCount(int maxCount) {
    entity.setMaxCount(maxCount);
  }

  @Override
  public Condition<Integer> getMinCount() {
    return entity.getMinCount();
  }

  public void setMinCount(int minCount) {
    entity.setMinCount(minCount);
  }

  @Override
  public String getName() {
    return entity.getName();
  }

  public void setName(String name) {
    entity.setName(name);
    EntityModelEvent e = new EntityModelEvent(entity, "name");
    fireAttribChanged(e);
  }

  @Override
  public Condition<Integer> getPrice() {
    return entity.getPrice();
  }

  public void setPrice(int price) {
    entity.setPrice(price);
  }
  
  public void setPrice(Condition price) {
    entity.setPrice(price);
  }

  @Override
  public int getTotalPrice() {
    return entity.getTotalPrice();
  }

  @Override
  public int getUpgrade(int index) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getUpgradeCount() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void setSource(Object o) {
    try {
      entity = (AbstractEntity) o;
      entity.addEntityListener(this);
      fireSourceChanged();
    } catch (ClassCastException e) {
      System.err.println("Objekt: \"" + o.toString() + "\" is not an Entity!");
    } 
  }
  
  @Override
  public Object getSource() {
    return entity;
  }

  public void fireSourceChanged() {
    Object[] listeners = listenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == EntityModelListener.class) {
        ((EntityModelListener) listeners[i + 1]).SourceChanged();
      }
    }
  }
  
  public void fireAttribChanged(EntityModelEvent e) {
    Object[] listeners = listenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == EntityModelListener.class) {
        ((EntityModelListener) listeners[i + 1]).AttribChanged(e);
      }
    }
  }

	@Override
	public void AttribChanged(String attrib) {
		EntityModelEvent e = new EntityModelEvent(entity, "name");
		fireAttribChanged(e);
	}

}
