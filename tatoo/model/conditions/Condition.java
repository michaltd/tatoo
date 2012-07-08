package tatoo.model.conditions;

/**
 * Eine Condition stellt die Grundlage für Attribute von Entitys dar welche von
 * anderen Entity-Attributen abhängen. Sie ermöglicht somit eine Berechnung zur
 * Laufzeit aufgrund von Attributen desselben oder anderer Entitys.
 * 
 * @author mkortz
 * 
 */
public interface Condition<T> {

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

}
