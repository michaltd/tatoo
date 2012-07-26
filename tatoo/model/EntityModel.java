package tatoo.model;

import javax.swing.event.EventListenerList;

import tatoo.model.conditions.Condition;
import tatoo.model.conditions.NumberCondition;
import tatoo.model.conditions.Condition.ConditionTypes;
import tatoo.model.entities.AbstractEntity;
import tatoo.model.entities.ArmyListEntity;
import tatoo.model.entities.EntityListener;
import tatoo.model.entities.events.EntityModelEvent;

/**
 * Model für Entities.
 * 
 * @author mkortz
 * 
 */
public class EntityModel implements ArmyListEntityModel, EntityListener {

    /** Das ArmyListEntity welches von diesem Model gekapselt wird */
    AbstractEntity            entity       = null;
    /** Liste von Listenern welche an diesem Model lauschen. */
    private EventListenerList listenerList = new EventListenerList();

    /**
     * Instanziiert das Model mit dem Übergebenen ArmyListEntity.
     * 
     * @param o
     *            Das ArmyListEntity das von dem Model gekapselt werden soll.
     */
    public EntityModel( Object o ) {
        setSource( o );
    }

    /**
     * Instanziiert ein leeres Model.
     */
    public EntityModel() {

    }

    /**
     * Fügt dem Model einen Listener hinzu
     * 
     * @param l
     *            Der Listener
     */
    public void addEntityModelListener( EntityModelListener l ) {
        listenerList.add( EntityModelListener.class, l );
    }

    /**
     * Entfernt einen Listener aus der Liste der Listener
     * 
     * @param l
     *            der Listener der entfernt werden soll.
     */
    public void removeEntityModelListener( EntityModelListener l ) {
        listenerList.remove( EntityModelListener.class, l );
    }

    @Override
    public int getCount() {
        int realVal = (Integer) entity.getAttribute( ConditionTypes.COUNT ).getValue();
        int maxVal = (Integer) entity.getAttribute( ConditionTypes.MAX_COUNT ).getValue();
        int minVal = (Integer) entity.getAttribute( ConditionTypes.MIN_COUNT ).getValue();
        int resultVal = realVal;

        if ( realVal > maxVal )
            resultVal = maxVal;
        else if ( realVal < minVal )
            resultVal = minVal;

        return resultVal;
    }

    /**
     * Setzt die Anzahl des Entitys.
     * 
     * @param count
     *            Die neue Anzahl des Entitys.
     */
    public void setCount( int count ) {
        if ( ( (Integer) entity.getAttribute( ConditionTypes.MIN_COUNT ).getValue() ) <= count
                        && ( (Integer) entity.getAttribute( ConditionTypes.MAX_COUNT ).getValue() ) >= count ) {
            entity.setAttribute( count, ConditionTypes.COUNT );
            fireAttribChanged( new EntityModelEvent( this, "count" ) );
        }
    }

    @Override
    public int getMaxCount() {
        return (Integer) entity.getAttribute( ConditionTypes.MAX_COUNT ).getValue();
    }

    /**
     * setzt die Maximale Anzahl des Entitys.
     * 
     * @param maxCount
     *            die Maximale Anzahl.
     */
    public void setMaxCount( int maxCount ) {
        entity.setAttribute( maxCount, ConditionTypes.MAX_COUNT );
    }

    @Override
    public int getMinCount() {
        return (Integer) entity.getAttribute( ConditionTypes.MIN_COUNT ).getValue();
    }

    /**
     * Setzt die Minimale Anzahl des Entitys
     * 
     * @param minCount
     *            die Minimale Anzahl.
     */
    public void setMinCount( int minCount ) {
        entity.setAttribute( minCount, ConditionTypes.MIN_COUNT );
    }

    @Override
    public String getName() {
        return entity.getName();
    }

    /**
     * Setzt den Namen des Entitys.
     * 
     * @param name
     *            Der Name des Entitys.
     */
    public void setName( String name ) {
        entity.setName( name );
        EntityModelEvent e = new EntityModelEvent( entity, "name" );
        fireAttribChanged( e );
    }

    @Override
    public int getPrice() {
        return (Integer) entity.getAttribute( ConditionTypes.PRICE ).getValue();
    }

    /**
     * Setzt den Preis des Entitys.
     * 
     * @param price
     *            Der Preis
     */
    public void setPrice( int price ) {
        entity.setAttribute( price, ConditionTypes.PRICE );
    }

    @Override
    public int getTotalPrice() {
        return ( (ArmyListEntity) entity ).getTotalPrice();
    }

    /**
     * Setzt das gekapselte Entiy des Models. Löst ein SourceChanged Event aus.
     * 
     * @param o
     *            Das ArmyListEntity welches von dem Model gekapselt werden
     *            soll.
     */
    public void setSource( Object o ) {
        try {
            entity = (AbstractEntity) o;
            entity.addEntityListener( this );
            fireSourceChanged();
        }
        catch ( ClassCastException e ) {
            System.err.println( "Objekt: \"" + o.toString() + "\" is not an ArmyListEntity!" );
        }
    }

    /**
     * Gibt das gekapselte ArmyListEntity zurück.
     * 
     * @return Das gekapselte ArmyListEntity als Object.
     */
    public Object getSource() {
        return entity;
    }

    /**
     * Informiert alle Listener und löst dort das SourceChangedEvent aus.
     */
    public void fireSourceChanged() {
        Object[] listeners = listenerList.getListenerList();
        for ( int i = listeners.length - 2; i >= 0; i -= 2 ) {
            if ( listeners[i] == EntityModelListener.class ) {
                ( (EntityModelListener) listeners[i + 1] ).SourceChanged();
            }
        }
    }

    /**
     * Informiert alle Listener und löst dort das AttribChangedEvent aus.
     * 
     * @param e
     *            Das Event, das übergeben werden soll.
     */
    public void fireAttribChanged( EntityModelEvent e ) {
        Object[] listeners = listenerList.getListenerList();
        for ( int i = listeners.length - 2; i >= 0; i -= 2 ) {
            if ( listeners[i] == EntityModelListener.class ) {
                ( (EntityModelListener) listeners[i + 1] ).AttribChanged( e );
            }
        }
    }

    @Override
    public void AttribChanged( String attrib ) {
        EntityModelEvent e = new EntityModelEvent( entity, "name" );
        fireAttribChanged( e );
    }

    public int getNodeType() {
        return entity.getType().ordinal();
    }

}
