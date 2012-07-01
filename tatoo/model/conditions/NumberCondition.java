package tatoo.model.conditions;

public interface NumberCondition<T> extends Condition<Number>, Comparable<NumberCondition<T>> {
  
  public abstract int compareTo(NumberCondition<T> numbCond);
  
  public void addChangeListener(ConditionListener l);
  
  public void removeChangeListener(ConditionListener l);

}