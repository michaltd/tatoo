package tatoo.model.entities;

import tatoo.model.conditions.Condition;
import tatoo.model.conditions.Condition.ConditionTypes;


public abstract interface EntityBase {

  /**
   * Gibt den Namen es Entitys zurück.
   * @return Der Name des Entitys.
   */
  public abstract String getName();

  /**
   * Setzt den Namen des Entitys.
   * @param name Der Name des Entitys.
   */
  public abstract void setName(String name);
  
  /**
   * Setzt ein Attribut des Entitys. 
   * @param attribute Das zu setzende Attribut.
   * @param type Der Typ des Attributes.
   */
  @SuppressWarnings("rawtypes")
  public void setAttribute(Condition attribute, ConditionTypes type);
  
  /**
   * Gibt das Attribut vom übergebenen Typ zurück.
   * @param type Der Typ des Attributes welches gelesen werden soll
   * @return Das Attribut als Condition.
   */
  @SuppressWarnings("rawtypes")
  public Condition getAttribute(ConditionTypes type);


}
