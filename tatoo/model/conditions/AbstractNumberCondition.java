package tatoo.model.conditions;

import javax.swing.event.EventListenerList;

public abstract class AbstractNumberCondition implements NumberCondition {

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
      if (listeners[i] == ConditionListener.class) {
        ((ConditionListener) listeners[i + 1]).valueChanged();
      }
    }
  }
  
  @Override
  public String toString() {
    return getValue().toString();
  }

  @Override
  public int compareTo(NumberCondition numbCond) {
    Integer thisNum = this.getValue();
    Integer anotherNum = numbCond.getValue();
    return (thisNum<anotherNum ? -1 : (thisNum==anotherNum ? 0 : 1));    
  }
  
  public void setOwner(Object o){
    this.owner = o;
  }
  
  public Object getOwner(){
    return owner;
  }


}