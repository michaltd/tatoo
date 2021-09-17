package tatoo.model.conditions;

import tatoo.model.entities.AbstractEntity;

/**
 * Eine Condition stellt die Grundlage für Attribute von Entitys dar welche von
 * anderen ArmyListEntity-Attributen abhängen. Sie ermöglicht somit eine
 * Berechnung zur Laufzeit aufgrund von Attributen desselben oder anderer
 * Entitys.
 *
 * @author mkortz
 */
public interface Condition<T> extends Cloneable
{
    enum ConditionTypes
    {
        COUNT,
        PRICE,
        MAX_COUNT,
        MIN_COUNT
    }

    /**
     * Gibt den Wert der Condition zurück.
     *
     * @return Der Wert der Condition.
     */
    Integer getValue();

    /**
     * Setzt den Wert der Condition.
     *
     * @param val Der Wert der Condition
     */
    void setValue(T val);

    /**
     * Erstellt einen Klon für die Condition. Der Klon wird dem übergebenen
     * Entity zugeordnet.
     *
     * @param entity Das Entity dem der Klon zugeordnet werden soll.
     * @return Die geklonte Condition
     * @throws CloneNotSupportedException
     */
    //TODO: cloneFor muss aus den Conditions entfernt und in eine Factory oder so ausgelagert werden. 
    // Dann sind wir auch diesen ganzen Owner quatsch los!
    Condition<T> cloneFor(AbstractEntity entity) throws CloneNotSupportedException;

    /**
     * Fügt einen Listener hinzu der auf Veränderungen in der NumberCondition
     * lauscht.
     *
     * @param l Der Listener
     */
    void addChangeListener(ConditionListener l);

    @SuppressWarnings("rawtypes")
    void fireValueChanged(Condition source);

    /**
     * Entfernt einen Listener aus der Liste der Listener.
     *
     * @param l Der Listener der entfernt werden soll.
     */
    void removeChangeListener(ConditionListener l);

    void setOwner(AbstractEntity entityNode);

    AbstractEntity getOwnerNode();
}
