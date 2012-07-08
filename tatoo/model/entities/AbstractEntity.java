package tatoo.model.entities;

import java.util.List;

import javax.swing.event.EventListenerList;

import tatoo.model.conditions.ConditionListener;
import tatoo.model.conditions.NumberCondition;
import tatoo.model.conditions.SimpleNumber;

/**
 * Abstrakte Oberklasse aller Entities. <br />
 * Bei einem Entity handelt es sich um eine beliebige Instanz innerhalb einer
 * Armeeliste oder eines Codex. Diese (Armeeliste und Codex) werden als Bäume
 * von Entitys dargestellt. Ein Entity stellt dabei einen Knoten dar. Demzufolge
 * kann ein Entity verschiedene Knoten repräsentieren. Um welche Art von Knoten
 * es sich handelt wird durch das Attribut {@link tatoo.model.entities.type}
 * festgelgt.
 * 
 * @author mkortz
 * 
 */
public abstract class AbstractEntity extends tatoo.db.Dataset implements
    ArmyListEntity, ConditionListener {

  /**
   * Die möglichen Entitytypen
   * 
   * @author mkortz
   * 
   */
  public enum EntityType {
    ROOT,
    CATEGORY,
    NODE,
    ANYOFUPGRADE,
    ONEOFUPGRADE,
    UPGRADE
  }

  public AbstractEntity(EntityType type) { this.type = type; }
  
  public AbstractEntity() { super();}

  /**
   * Der Typ des Entitys. Dieser Wert muss in einer von AbstractEntity erbenden
   * Klasse im Konstruktor gesetzt werden.
   */
  protected EntityType type;
  /** Der Name des Entitys */
  private String name;
  /** Der Preis des Entitys */
  private NumberCondition<Integer> price;
  /** Die Anzahl des Entitys */
  private NumberCondition<Integer> count;

  /** Liste von Listenern */
  private EventListenerList listenerList = new EventListenerList();

  /**
   * initialisiert das Entity.
   */
  public void init() {
    count = new SimpleNumber(0);
    count.addChangeListener(this);

    price = new SimpleNumber(0);
    price.addChangeListener(this);
  }

  /**
   * Fügt der Liste von Listenern einen Listener hinzu
   * 
   * @param l
   *          Der Listener, der hinzugefügt werden soll.
   */
  public void addEntityListener(EntityListener l) {
    listenerList.add(EntityListener.class, l);
  }

  /**
   * Entfernt einen Listener aus der Liste von Listenern.
   * 
   * @param l
   *          der Listender, der entfernt werden soll.
   */
  public void removeEntityListener(EntityListener l) {
    listenerList.remove(EntityListener.class, l);
  }

  /**
   * Vergleicht zwei AbstractEntitys auf Wertgleichheit. Es werden Name und
   * Preis verglichen. Liefert true zurück wenn diese Werte gleich sind. False
   * sonst.
   * 
   * @param e Das Entity mit dem verglichen werden soll.
   * @return true wenn die Entitys wertmäßig gleich sind. False sonst.
   */
  public boolean equals(AbstractEntity e) {
    if (this.getName() == null)
      return false;
    boolean nameEqual, priceEqual;
    nameEqual = this.getName().equals(e.getName());
    priceEqual = (this.getPrice().getValue()) == (e.getPrice().getValue());
    return nameEqual && priceEqual;
  }

  /**
   * Übergibt allen Listenern das AttribChangedEvent.
   */
  public void fireAttribChanged() {
    Object[] listeners = listenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == EntityListener.class) {
        ((EntityListener) listeners[i + 1]).AttribChanged("someAttrib");
      }
    }
  }
  
  /**
   * Gibt den Typ des Entitys zurück.
   * @return Der Typ des Entitys.
   */
  public EntityType getType() {
    return type;
  }
  
  @Override
  public NumberCondition<Integer> getPrice() {
    return price;
  }

  @Override
  public void setPrice(int price) {
    this.price.setValue(price);
  }

  @Override
  public void setPrice(NumberCondition<Integer> price) {
    this.price = price;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public NumberCondition<Integer> getCount() {
    return count;
  }

  @Override
  public void setCount(int count) {
    int value = (Integer) getMinCount().getValue();
    if (count < value)
      this.count.setValue(value);
    if (count >= value && count <= (Integer) getMaxCount().getValue()) {
      this.count.setValue(count);
    }
  }

  @Override
  public void setCount(NumberCondition<Integer> count) {
    int value = (Integer) getMinCount().getValue();
    int countVal = (Integer) count.getValue();
    if (countVal < value)
      this.count.setValue(value);
    if (countVal >= value && countVal <= (Integer) getMaxCount().getValue()) {
      this.count.setValue(countVal);
    }
  }

  @Override
  public int hashCode() {
    String hashString = name + price;
    return hashString.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    AbstractEntity entity;
    try {
      entity = (AbstractEntity) obj;
    } catch (ClassCastException e) {
      return false;
    }
    
    String thisName = this.getName();
    String hisName = entity.getName();
    if (thisName == null)
      thisName = "";
    if (hisName == null)
      hisName = "";
    return thisName.equals(hisName)
        && this.getId() == entity.getId()
        && price.getValue() == entity.getPrice().getValue()
        && count.getValue() == entity.getCount().getValue();
  }

  @Override
  public void valueChanged() {
    fireAttribChanged();
  }

  public abstract NumberCondition<Integer> getMaxCount();

  public abstract void setMaxCount(int maxCount);

  public abstract void setMaxCount(NumberCondition<Integer> maxCount);

  public abstract int getTotalPrice();

  public abstract AbstractEntity getEntityAt(int index);

  public abstract Boolean addEntity(AbstractEntity entity);

  public abstract void removeEntity(AbstractEntity entity);

  public abstract void addUpgrade(AbstractUpgrade upgrade);

  public abstract String toString();

  public abstract int getChildCount();

  public abstract int getIndexOf(AbstractEntity e);

  public abstract boolean isLeaf();

  public abstract List<AbstractEntity> getChilds();

}
