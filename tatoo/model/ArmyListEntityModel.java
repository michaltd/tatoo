package tatoo.model;

import tatoo.model.conditions.CalculatedNumber;
import tatoo.model.conditions.Condition;
import tatoo.model.conditions.Condition.ConditionTypes;
import tatoo.model.conditions.SimpleNumber;
import tatoo.model.entities.AbstractEntity;
import tatoo.model.entities.AbstractEntity.EntityType;
import tatoo.model.entities.ArmyListEntity;
import tatoo.model.entities.events.EntityModelEvent;

/**
 * Model für Entities.
 * 
 * @author mkortz
 */
public class ArmyListEntityModel extends AbstractEntityModel {

    /**
     * Instanziiert das Model mit dem Übergebenen ArmyListEntity.
     * 
     * @param o
     * Das ArmyListEntity das von dem Model gekapselt werden soll.
     */
    public ArmyListEntityModel (Object o) {
        setSource (o);
    }

    /**
     * Instanziiert ein leeres Model.
     */
    public ArmyListEntityModel () {

    }

    @Override
    public String getCount () {
        int realVal = (Integer) entity.getAttribute (ConditionTypes.COUNT).getValue ();
        int maxVal = (Integer) entity.getAttribute (ConditionTypes.MAX_COUNT).getValue ();
        int minVal = (Integer) entity.getAttribute (ConditionTypes.MIN_COUNT).getValue ();
        int resultVal = realVal;

        if (realVal > maxVal)
            resultVal = maxVal;
        else if (realVal < minVal)
            resultVal = minVal;

        return Integer.toString (resultVal);
    }

    /**
     * Setzt die Anzahl des Entitys.
     * 
     * @param count
     * Die neue Anzahl des Entitys.
     */
    public void setCount (int count) {
        if (((Integer) entity.getAttribute (ConditionTypes.MIN_COUNT).getValue ()) <= count
                        && ((Integer) entity.getAttribute (ConditionTypes.MAX_COUNT).getValue ()) >= count) {
            entity.setAttribute (count, ConditionTypes.COUNT);
            fireAttribChanged (new EntityModelEvent (entity, ConditionTypes.COUNT));
        }
    }

    public void setCount (Condition condition) {
        entity.setAttribute (condition, ConditionTypes.COUNT);
        fireAttribChanged (new EntityModelEvent (entity, ConditionTypes.COUNT));
    }

    @Override
    public String getMaxCount () {
        return ((Integer) entity.getAttribute (ConditionTypes.MAX_COUNT).getValue ()).toString ();
    }

    /**
     * setzt die Maximale Anzahl des Entitys.
     * 
     * @param maxCount
     * die Maximale Anzahl.
     */
    public void setMaxCount (String maxCount) throws NumberFormatException {
        int value = Integer.parseInt (maxCount);
        entity.setAttribute (value, ConditionTypes.MAX_COUNT);
    }

    public void setMaxCount (Condition condition) {
        entity.setAttribute (condition, ConditionTypes.MAX_COUNT);
    }

    @Override
    public String getMinCount () {
        return ((Integer) entity.getAttribute (ConditionTypes.MIN_COUNT).getValue ()).toString ();
    }

    /**
     * Setzt die Minimale Anzahl des Entitys
     * 
     * @param minCount
     * die Minimale Anzahl.
     */
    public void setMinCount (String minCount) throws NumberFormatException {
        int value = Integer.parseInt (minCount);
        entity.setAttribute (value, ConditionTypes.MIN_COUNT);
    }

    public void setMinCount (Condition condition) {
        entity.setAttribute (condition, ConditionTypes.MIN_COUNT);
    }

    @Override
    public String getName () {
        return entity.getName ();
    }

    /**
     * Setzt den Namen des Entitys.
     * 
     * @param name
     * Der Name des Entitys.
     */
    public void setName (String name) {
        entity.setName (name);
        EntityModelEvent e = new EntityModelEvent (entity, null);
        fireAttribChanged (e);
    }

    @Override
    public String getPrice () {
        return entity.getAttribute (ConditionTypes.PRICE).toString ();
    }

    /**
     * Setzt den Preis des Entitys.
     * 
     * @param val
     * Der Preis
     */
    public void setPrice (String val) {
        try {
            int value = Integer.parseInt (val);
            CalculatedNumber condition = (CalculatedNumber) entity.getAttribute (ConditionTypes.PRICE);
            condition.setValue (new SimpleNumber (value));
        }
        catch (NumberFormatException nfe) {}
    }

    public void setPrice (Condition price) {
        entity.setAttribute (price, ConditionTypes.PRICE);
    }

    @Override
    public int getTotalPrice () {
        return ((ArmyListEntity) entity).getTotalPrice ();
    }

    /**
     * Setzt das gekapselte Entiy des Models. Löst ein SourceChanged Event aus.
     * 
     * @param o
     * Das ArmyListEntity welches von dem Model gekapselt werden soll.
     */
    public void setSource (Object o) {
        try {
            entity = (AbstractEntity) o;
            entity.addEntityListener (this);
            fireSourceChanged ();
        }
        catch (ClassCastException e) {
            System.err.println ("Objekt: \"" + o.toString () + "\" is not an ArmyListEntity!");
        }
    }

    /**
     * Gibt das gekapselte ArmyListEntity zurück.
     * 
     * @return Das gekapselte ArmyListEntity als Object.
     */
    public Object getSource () {
        return entity;
    }

    public boolean isOneOfUpgrade () {
        return entity.getParent ().getType () == EntityType.ONEOFUPGRADE;
    }

    public EntityType getSourceType () {
        return entity.getType ();
    }

    // public int getNodeType () {
    // return entity.getType ().ordinal ();
    // }

}
