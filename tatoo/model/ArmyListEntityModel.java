package tatoo.model;

import tatoo.Tatoo;
import tatoo.commands.CmdAlterConditionByCond;
import tatoo.commands.CmdAlterConditionByVal;
import tatoo.model.conditions.Condition;
import tatoo.model.conditions.Condition.ConditionTypes;
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
        if (o != null)
            setSource (o);
    }

    /**
     * Instanziiert ein leeres Model.
     */
    public ArmyListEntityModel () {

    }

    @Override
    public String getCount () {
        if (entity != null) {
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
        return null;
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
            Tatoo.cmdMgr.execute (new CmdAlterConditionByVal (entity.getAttribute (ConditionTypes.COUNT), count));
            fireAttribChanged (new EntityModelEvent (entity, ConditionTypes.COUNT));
        }
    }

    @SuppressWarnings ("rawtypes")
    public void setCount (Condition condition) {
        entity.setAttribute (condition, ConditionTypes.COUNT);
        Tatoo.cmdMgr.execute (new CmdAlterConditionByCond (entity, condition, ConditionTypes.COUNT));
        fireAttribChanged (new EntityModelEvent (entity, ConditionTypes.COUNT));
    }

    @Override
    public String getMaxCount () {
        if (entity != null)
            return ((Integer) entity.getAttribute (ConditionTypes.MAX_COUNT).getValue ()).toString ();
        return null;
    }

    /**
     * setzt die Maximale Anzahl des Entitys.
     * 
     * @param maxCount
     * die Maximale Anzahl.
     */
    public void setMaxCount (String maxCount) throws NumberFormatException {
        int value = Integer.parseInt (maxCount);
        Tatoo.cmdMgr.execute (new CmdAlterConditionByVal (entity.getAttribute (ConditionTypes.MAX_COUNT), value));
    }

    public void setMaxCount (Condition condition) {
        Tatoo.cmdMgr.execute (new CmdAlterConditionByCond (entity, condition, ConditionTypes.MAX_COUNT));
    }

    @Override
    public String getMinCount () {
        if (entity != null)
            return ((Integer) entity.getAttribute (ConditionTypes.MIN_COUNT).getValue ()).toString ();
        return null;
    }

    /**
     * Setzt die Minimale Anzahl des Entitys
     * 
     * @param minCount
     * die Minimale Anzahl.
     */
    public void setMinCount (String minCount) throws NumberFormatException {
        int value = Integer.parseInt (minCount);
        Tatoo.cmdMgr.execute (new CmdAlterConditionByVal (entity.getAttribute (ConditionTypes.MIN_COUNT), value));
    }

    public void setMinCount (Condition condition) {
        Tatoo.cmdMgr.execute (new CmdAlterConditionByCond (entity, condition, ConditionTypes.MIN_COUNT));
    }

    @Override
    public String getName () {
        if (entity != null)
            return entity.getName ();
        return null;

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
        if (entity != null)
            return entity.getAttribute (ConditionTypes.PRICE).toString ();
        return null;
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
            Tatoo.cmdMgr.execute (new CmdAlterConditionByVal (entity.getAttribute (ConditionTypes.PRICE), value));
        }
        catch (NumberFormatException nfe) {}
    }

    public void setPrice (Condition price) {
        Tatoo.cmdMgr.execute (new CmdAlterConditionByCond (entity, price, ConditionTypes.PRICE));
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
            if (entity != null)
                entity.removeEntityListener (this);
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

    public EntityType getSourceType () {
        if (entity != null)
            return entity.getType ();
        return null;
    }

}
