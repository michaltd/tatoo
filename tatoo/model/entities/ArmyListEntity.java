package tatoo.model.entities;

import tatoo.model.conditions.Condition;
import tatoo.model.conditions.Condition.ConditionTypes;

public class ArmyListEntity extends AbstractEntity {

    /**
     * Erzeugt ein neues ArmyListEntity vom Typ NODE.
     */
    public ArmyListEntity() {
        this( EntityType.NODE, "" );
    }

    /**
     * Erzeugt ein neues ArmyListEntity mit dem übergebenen Typen und Namen.
     */
    public ArmyListEntity( EntityType type, String name ) {
        this( type, name, 0 );
    }

    /**
     * Erzeugt ein neues ArmyListEntity mit dem übergebenen Typen, Namen und
     * Preis.
     */
    public ArmyListEntity( EntityType type, String name, int price ) {
        super( type );
        setName( name );
        setAttribute( price, ConditionTypes.PRICE );
    }

    @Override
    public ArmyListEntity cloneFor( AbstractEntity parent ) throws CloneNotSupportedException {

        ArmyListEntity e = new ArmyListEntity();

        if ( parent == null && this.getType() != EntityType.ROOT )
            throw new CloneNotSupportedException( "Get null as parent but cloning a non-root node." );
        else e.setParent( parent );

        // typ und Name setzen
        e.type = this.type;
        e.setName( this.getName() );

        // dann die Conditions Klonen
        for ( ConditionTypes attType : ConditionTypes.values() ) {
            Condition conditionClone = this.getAttribute( attType ).cloneFor( getEntityNode( e ) );
            conditionClone.addChangeListener( e );
            e.setAttribute(conditionClone , attType );
            //e.getAttribute( attType ).addChangeListener( e );
        }

        // zum Schluss die entities durchgehen und wenn es sich NICHT um
        // ROOT, CATEGORY, NODE Entitys handelt Klonen:
        for ( AbstractEntity ae : entities ) {
            if ( ae.getType() != EntityType.ROOT && ae.getType() != EntityType.CATEGORY
                            && ae.getType() != EntityType.NODE ) {
                e.addEntity( ae.cloneFor( e ) );
            }
        }

        return e;
    }

    public int getTotalPrice() {
        int total = (Integer) getAttribute( ConditionTypes.PRICE ).getValue();
        for ( AbstractEntity ae : entities )
            total += ( (ArmyListEntity) ae ).getTotalPrice();
        return total;
    }

    
    
    @Override
    public String toString() {
        return getName();
    }

}