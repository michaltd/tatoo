package tatoo.model.conditions;

import java.util.EventListener;

import javax.swing.event.EventListenerList;

import tatoo.db.Dataset;
import tatoo.model.entities.AbstractEntity;

/**
 * Grundklasse aller Conditions welche in irgendeiner Form mit Zahlen zu tun
 * haben.
 * 
 * @author mkortz
 */
public abstract class AbstractNumberCondition<T> extends Dataset implements NumberCondition <T> {

    /** Liste der Listener */
    private EventListenerList listenerList = new EventListenerList ();

    private AbstractEntity    ownerNode;

    public void setOwner (AbstractEntity ownerNode) {
        if (this.ownerNode != ownerNode)
            this.ownerNode = ownerNode;
    }

    public AbstractEntity getOwnerNode () {
        return ownerNode;
    }

    /**
     * Fügt der Listenerliste einen weiteren Listener hinzu
     * 
     * @param l
     * Der Listener
     */
    @Override
    public void addChangeListener (ConditionListener l) {
        listenerList.add (ConditionListener.class, l);
        if (this.ownerNode == null && l instanceof AbstractEntity)
            listenerList.remove (ConditionListener.class, l);
    }

    /**
     * Entfernt einen Listener aus der LIstenerliste
     * 
     * @param l
     * der Listender der entfernt werden soll.
     */
    @Override
    public void removeChangeListener (ConditionListener l) {
        listenerList.remove (ConditionListener.class, l);
    }

    /**
     * Informiert alle Listener, dass ein ValueChanged Event ausgelöst wurde.
     */
    public void fireValueChanged (Condition source) {
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (EventListener.class.isAssignableFrom ((Class <?>) listeners[i])) {
                ((ConditionListener) listeners[i + 1]).valueChanged (this);
            }
        }
    }

    @Override
    public String toString () {
        return getValue ().toString ();
    }

    @Override
    public int compareTo (NumberCondition <T> numbCond) {
        Number thisNum = this.getValue ();
        Number anotherNum = numbCond.getValue ();
        return (thisNum.doubleValue () < anotherNum.doubleValue () ? -1 : (thisNum == anotherNum ? 0 : 1));
    }

}