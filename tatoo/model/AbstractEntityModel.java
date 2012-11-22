package tatoo.model;

import javax.swing.event.EventListenerList;

import tatoo.model.conditions.Condition.ConditionTypes;
import tatoo.model.entities.AbstractEntity;
import tatoo.model.entities.EntityListener;
import tatoo.model.entities.events.EntityModelEvent;

public abstract class AbstractEntityModel implements EntityModel, EntityListener {

    /** Das ArmyListEntity welches von diesem Model gekapselt wird */
    protected AbstractEntity  entity       = null;
    /** Liste von Listenern welche an diesem Model lauschen. */
    private EventListenerList listenerList = new EventListenerList ();

    public AbstractEntityModel () {
//        super ();
    }
    
    /**
     * Fügt dem Model einen Listener hinzu.
     * @param l
     * Der Listener
     */
    public void addEntityModelListener (EntityModelListener l) {
        listenerList.add (EntityModelListener.class, l);
    }

    /**
     * Entfernt einen Listener aus der Liste der Listener
     * @param l
     * der Listener der entfernt werden soll.
     */
    public void removeEntityModelListener (EntityModelListener l) {
        listenerList.remove (EntityModelListener.class, l);
    }

    /**
     * Informiert alle Listener und löst dort das SourceChangedEvent aus.
     */
    public void fireSourceChanged () {
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == EntityModelListener.class) {
                ((EntityModelListener) listeners[i + 1]).SourceChanged ();
            }
        }
    }

    /**
     * Informiert alle Listener und löst dort das AttribChangedEvent aus.
     * 
     * @param e
     * Das Event, das übergeben werden soll.
     */
    public void fireAttribChanged (EntityModelEvent e) {
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == EntityModelListener.class) {
                ((EntityModelListener) listeners[i + 1]).AttribChanged (e);
            }
        }
    }

    @Override
    public void AttribChanged (AbstractEntity entity, ConditionTypes attrib) {
        EntityModelEvent e = new EntityModelEvent (entity, attrib);
        fireAttribChanged (e);
    }
    
    @Override
    public void ChildInserted (AbstractEntity entity, AbstractEntity child) {
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == EntityModelListener.class) {
                ((EntityModelListener) listeners[i + 1]).EntityInserted ();
            }
        }
    }

    @Override
    public void ChildRemoved (AbstractEntity entity, AbstractEntity child, int index) {
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == EntityModelListener.class) {
                ((EntityModelListener) listeners[i + 1]).EntityRemoved ();
            }
        }
        
    }

}