package tatoo.model;

import java.util.EventListener;

import tatoo.model.entities.events.EntityModelEvent;

/**
 * Grundlage für alle Listener auf das ArmyListEntity Model
 * 
 * @author mkortz
 */
public interface EntityModelListener extends EventListener {

    /**
     * Wird ausgelöst wenn sich das ArmyListEntity des Models geändert hat.
     */
    public void SourceChanged ();

    /**
     * Wird ausgelöst wenn sich ein Attribut des gekapselten Entities geändert
     * hat.
     * 
     * @param e
     * Das Event
     */
    public void AttribChanged (EntityModelEvent e);
    
    public void EntityInserted ();
    
    public void EntityRemoved ();

}
