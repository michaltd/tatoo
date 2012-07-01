package tatoo.model.conditions;

import java.util.EventListener;

import javax.swing.event.EventListenerList;

import tatoo.db.Dataset;

public abstract class AbstractNumberCondition<T> extends Dataset implements NumberCondition<T> {

  Object owner;
  /** List of objects listening on Changes on this Condition. */
  private EventListenerList listenerList = new EventListenerList();
  
  /**
   * Add a Listener to this Condition
   * @param l the ModelListener
   */
  public void addChangeListener(ConditionListener l) {
    listenerList.add(ConditionListener.class, l);
  }
  
  /**
   * remove a Listener from this Condition
   * @param l the ModelListener
   */
  public void removeChangeListener(ConditionListener l) {
    listenerList.remove(ConditionListener.class, l);
  }
  
  public void fireValueChanged() {
    Object[] listeners = listenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
//      if (listeners[i] == ConditionListener.class) {
      if (EventListener.class.isAssignableFrom((Class<?>)listeners[i])) {
        ((ConditionListener) listeners[i + 1]).valueChanged();
      }
    }
  }
  
  @Override
  public String toString() {
    return getValue().toString();
  }

  @Override
  public int compareTo(NumberCondition<T> numbCond) {
    Number thisNum = this.getValue();
    Number anotherNum = numbCond.getValue();
    return ( thisNum.doubleValue() < anotherNum.doubleValue() ? -1 : (thisNum == anotherNum ? 0 : 1));    
  }
  
  public void setOwner(Object o){
    this.owner = o;
  }
  
  public Object getOwner(){
    return owner;
  }


}