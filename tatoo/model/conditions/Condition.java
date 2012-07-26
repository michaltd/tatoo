package tatoo.model.conditions;

import tatoo.model.entities.AbstractEntity;

/**
 * Eine Condition stellt die Grundlage für Attribute von Entitys dar welche von
 * anderen ArmyListEntity-Attributen abhängen. Sie ermöglicht somit eine Berechnung zur
 * Laufzeit aufgrund von Attributen desselben oder anderer Entitys.
 * 
 * @author mkortz
 * 
 */
public interface Condition<T> extends Cloneable {

  
  public static enum ConditionTypes
  {
    COUNT, PRICE, MAX_COUNT, MIN_COUNT
  }
  
  /**
   * Gibt den Wert der Condition zurück.
   * 
   * @return Der Wert der Condition.
   */
  public T getValue();

  /**
   * Setzt den Wert der Condition.
   * 
   * @param val
   *          Der Wert der Condition
   */
  public void setValue(T val);
  
  /**
   * Erstellt einen Klon für die Condition. Der Klon wird dem übergebenen Entity zugeordnet.
   * @param entity Das Entity dem der Klon zugeordnet werden soll.
   * @return Die geklonte Condition
   * @throws CloneNotSupportedException
   */
  public abstract Condition<T> cloneFor(AbstractEntity entity) throws CloneNotSupportedException;
  
  /**
   * Fügt einen Listener hinzu der auf Veränderungen in der NumberCondition lauscht.
   * @param l Der Listener
   */
  public void addChangeListener(ConditionListener l);
  
  public void fireValueChanged();

  /**
   * Entfernt einen Listener aus der Liste der Listener.
   * @param l Der Listener der entfernt werden soll.
   */
  public void removeChangeListener(ConditionListener l);

  public void setOwner(AbstractEntity entityNode);


}
