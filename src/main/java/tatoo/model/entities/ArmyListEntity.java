package tatoo.model.entities;

import tatoo.model.conditions.Condition;
import tatoo.model.conditions.Condition.ConditionTypes;

public class ArmyListEntity extends AbstractEntity
{
    /**
     * Erzeugt ein neues ArmyListEntity vom Typ NODE.
     */
    public ArmyListEntity()
    {
        this(EntityType.NODE, "");
    }

    /**
     * Erzeugt ein neues ArmyListEntity mit dem übergebenen Typen und Namen.
     */
    public ArmyListEntity(EntityType type, String name)
    {
        this(type, name, 0);
    }

    /**
     * Erzeugt ein neues ArmyListEntity mit dem übergebenen Typen, Namen und
     * Preis.
     */
    public ArmyListEntity(EntityType type, String name, int price)
    {
        super(type);
        setName(name);
        setAttribute(price, ConditionTypes.PRICE);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public ArmyListEntity cloneFor(AbstractEntity parent) throws CloneNotSupportedException
    {
        ArmyListEntity new_entity = new ArmyListEntity();

        if (parent == null && this.getType() != EntityType.ARMYLIST)
            throw new CloneNotSupportedException("Get null as parent but cloning a non-root node.");
        else new_entity.setParent(parent);

        // typ und Name setzen
        new_entity.type = this.type;
        new_entity.setName(this.getName());

        // dann die Conditions Klonen
        for (ConditionTypes attType : ConditionTypes.values()) {
            Condition conditionClone = this.getAttribute(attType).cloneFor(getEntityNode(new_entity));
            conditionClone.addChangeListener(new_entity);
            new_entity.setAttribute(conditionClone, attType);
        }

        // zum Schluss die entities durchgehen und wenn es sich NICHT um
        // ARMYLIST, CATEGORY, NODE Entitys handelt Klonen:
        for (AbstractEntity ae : entities) {
            if (ae.getType() != EntityType.ARMYLIST && ae.getType() != EntityType.CATEGORY
                && ae.getType() != EntityType.NODE) {
                new_entity.addEntity(ae.cloneFor(new_entity));
            }
        }

        return new_entity;
    }

    public int getTotalPrice()
    {
        int total = getAttribute(ConditionTypes.PRICE).getValue();
        for (AbstractEntity ae : entities)
            total += ((ArmyListEntity) ae).getTotalPrice();
        return total;
    }

    @Override
    public String toString()
    {
        return getName();
    }
}