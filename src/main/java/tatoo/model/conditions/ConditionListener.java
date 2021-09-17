package tatoo.model.conditions;

import java.util.EventListener;

/**
 * Oberklasse aller Klassen die auf Conditions lauschen.
 *
 * @author mkortz
 */
public interface ConditionListener extends EventListener
{
    /**
     * Horcht auf das ValueChangedEvent.
     *
     * @param source
     */
    @SuppressWarnings("rawtypes")
    void valueChanged(Condition source);
}
