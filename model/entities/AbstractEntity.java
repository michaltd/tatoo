package tatoo.model.entities;

import java.util.List;

import javax.swing.event.EventListenerList;

import tatoo.model.conditions.Condition;
import tatoo.model.conditions.ConditionListener;
import tatoo.model.conditions.NumberCondition;
import tatoo.model.conditions.SimpleNumber;

/**
 * @author mkortz
 * 
 */
public abstract class AbstractEntity extends tatoo.db.Dataset  implements ArmyListEntity, ConditionListener {

  
	//niemals direkt auf die attribute zugreifen! immer über die getter Methoden!
	/** name of the Entity */
  protected String name;
  /** price of the Entity */
  protected NumberCondition price;
  /** count of the Entity */
  protected NumberCondition count;
  
  /** List of Listeners */
  private EventListenerList listenerList = new EventListenerList();
  
  public void init(){
		count = new SimpleNumber(0);
		count.addChangeListener(this);
		
		price = new SimpleNumber(0);
		price.addChangeListener(this);
  }
  
  public void addEntityListener(EntityListener l){
  	listenerList.add(EntityListener.class, l);
  }
  
  public void removeEntityListener(EntityListener l){
  	listenerList.remove(EntityListener.class, l);
  }

  public NumberCondition getPrice() {
    return price;
  }
  
  public NumberCondition getPriceAsCondition(){
    return price;
  }
  
  public void setPrice(int price) {
    this.price.setValue(price);
  }
  
  public void setPrice(Condition price) {
    this.price = (NumberCondition)price;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public NumberCondition getCountAsCondition(){
  	return count;
  }

  public Condition<Integer> getCount() {
    return count;
  }

  public void setCount(int count) {
  	int value = (Integer)getMinCount().getValue();
  	if (count < value)
  		this.count.setValue(value);
  	if (count >= value && count <= getMaxCount().getValue()){
  		this.count.setValue(count);
  	}
  }
  
  public void setCount(Condition<Integer> count) {
  	int value = (Integer)getMinCount().getValue();
  	int countVal = count.getValue();
  	if (countVal < value)
  		this.count.setValue(value);
  	if (countVal >= value && countVal <= getMaxCount().getValue()){
  		this.count.setValue(countVal);
  	}
  }

  public boolean equals(AbstractEntity e) {
    if (this.getName() == null) return false;
    boolean nameEqual, priceEqual;
    nameEqual = this.getName().equals(e.getName());
    priceEqual = (this.getPrice().getValue()) == (e.getPrice().getValue());
    return nameEqual && priceEqual;
  }

  public int hashCode() {
    String hashString = name + price;
    return hashString.hashCode();
  }
  
  public boolean equals(Object obj) {
  	if (obj == null) return false;
  	AbstractEntity entity;
  	try{
  		entity = (AbstractEntity)obj;
  	}catch(ClassCastException e){
  		return false;
  	}
  	return this.getName() == entity.getName() && 
  			 price.getValue() == entity.getPrice().getValue() && 
  			 count.getValue() == entity.getCount().getValue();
  }
  
  public void fireAttribChanged() {
    Object[] listeners = listenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == EntityListener.class) {
        ((EntityListener) listeners[i + 1]).AttribChanged("someAttrib");
      }
    }
  }
  
  public void valueChanged(){
  	fireAttribChanged();
  }

  public abstract Condition<Integer> getMaxCount();

  public abstract void setMaxCount(int maxCount);
  
  public abstract void setMaxCount(Condition<Integer> maxCount);

  public abstract int getTotalPrice();

  //nicht umbenennen! sonst gibt es probleme mit dem JTree!
  public abstract AbstractEntity getEntityAt(int index);

  public abstract Boolean addEntity(AbstractEntity entity);

  public abstract void removeEntity(AbstractEntity entity);

  public abstract void getSpecialRules();

  public abstract void addUpgrade(AbstractUpgrade upgrade);

  public abstract String toString();

  public abstract int getChildCount();

  public abstract int getIndexOf(AbstractEntity e);

  public abstract boolean isLeaf();

  public abstract List<AbstractEntity> getChilds();

}
