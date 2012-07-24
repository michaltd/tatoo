package tatoo.model.entities;

import tatoo.model.conditions.Condition.ConditionTypes;

public class ArmyListEntity extends AbstractEntity {

  /**
   * Erzeugt ein neues ArmyListEntity vom Typ NODE.
   */
  public ArmyListEntity() {
    this(EntityType.NODE, "");
  }

  /**
   * Erzeugt ein neues ArmyListEntity mit dem übergebenen Typen und Namen.
   */
  public ArmyListEntity(EntityType type, String name) {
    this(type, name, 0);
  }

  /**
   * Erzeugt ein neues ArmyListEntity mit dem übergebenen Typen, Namen und
   * Preis.
   */
  public ArmyListEntity(EntityType type, String name, int price) {
    super(type);
    setName(name);
    setAttribute(price, ConditionTypes.PRICE);
  }

  public ArmyListEntity clone() {

    ArmyListEntity e = new ArmyListEntity();
    
    // typ und Name setzen
    e.type = this.type;
    e.setName(this.getName());

    // dann die Conditions Klonen
    try {
      // Klon des Attributes PRICE erzeugen.
      ConditionTypes attType = ConditionTypes.PRICE;
      e.setAttribute(this.getAttribute(attType).clone(), attType);

      // Klon des Attributes COUNT erzeugen.
      attType = ConditionTypes.COUNT;
      e.setAttribute(this.getAttribute(attType).clone(), attType);

      // Klon des Attributes MIN_COUNT erzeugen.
      attType = ConditionTypes.MIN_COUNT;
      e.setAttribute(this.getAttribute(attType).clone(), attType);

      // Klon des Attributes MAX_COUNT erzeugen.
      attType = ConditionTypes.MAX_COUNT;
      e.setAttribute(this.getAttribute(attType).clone(), attType);

    } catch (CloneNotSupportedException e1) {
      e1.printStackTrace();
    }
    
    // zum Schluss die entities durchgehen und wenn es sich nicht um 
    // ROOT, CATEGORY, NODE Entitys handelt Klonen:
    for (AbstractEntity ae : entities){
      if ( ae.getType() != EntityType.ROOT && ae.getType() != EntityType.CATEGORY &&  ae.getType() != EntityType.NODE)
      {
        e.addEntity(ae.clone());        
      }
    }
    
    return e;
  }

  public int getTotalPrice() {
    int total = (Integer) getAttribute(ConditionTypes.PRICE).getValue();
    for (AbstractEntity ae : entities)
      total += ((ArmyListEntity) ae).getTotalPrice();
    return total;
  }

  @Override
  public String toString() {
    // String returnString = "";
    // for (AbstractEntity ae : entities)
    // returnString += ae.toString();
    String priceString = "";
    // if (price > 0)
    // priceString = "->" + price;
    return getName() + priceString; // + returnString;
  }

}