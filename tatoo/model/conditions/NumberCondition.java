package tatoo.model.conditions;

/**
 * Eine Condition welche eine Zahl repräsentiert.
 * 
 * @author mkortz
 * 
 * @param <T>
 */
public interface NumberCondition<T> extends Condition<Number>,
    Comparable<NumberCondition<T>> {

  @Override
  public abstract int compareTo(NumberCondition<T> numbCond);

  /**
   * Fügt einen Listener hinzu der auf Veränderungen in der NumberCondition lauscht.
   * @param l Der Listener
   */
  public void addChangeListener(ConditionListener l);

  /**
   * Entfernt einen Listener aus der Liste der Listener.
   * @param l Der Listener der entfernt werden soll.
   */
  public void removeChangeListener(ConditionListener l);

}