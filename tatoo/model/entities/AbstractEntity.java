package tatoo.model.entities;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.EventListenerList;

import tatoo.model.conditions.Condition;
import tatoo.model.conditions.Condition.ConditionTypes;
import tatoo.model.conditions.ConditionListener;
import tatoo.model.conditions.SimpleNumber;

/**
 * Abstrakte Oberklasse aller Entities. <br />
 * Bei einem ArmyListEntity handelt es sich um eine beliebige Instanz innerhalb
 * einer Armeeliste oder eines Codex. Diese (Armeeliste und Codex) werden als
 * Bäume von Entitys dargestellt. Ein ArmyListEntity stellt dabei einen Knoten
 * dar. Demzufolge kann ein ArmyListEntity verschiedene Knoten repräsentieren.
 * Um welche Art von Knoten es sich handelt wird durch das Attribut
 * {@link AbstractEntity#type} festgelgt.
 * 
 * @author mkortz
 * 
 */
public abstract class AbstractEntity extends tatoo.db.Dataset implements EntityBase, ConditionListener {

    /**
     * Die möglichen Entitytypen Ein ArmyListEntity kann einen typen annehmen.
     * Vom Typen hängt z.B. ab, wie das ArmyListEntity in der View dargestellt
     * wird oder ob es Unterknoten haben darf.
     * 
     * @author mkortz
     * 
     */
    public enum EntityType {
        ROOT,
        CATEGORY,
        NODE,
        ANYOFUPGRADE,
        ONEOFUPGRADE,
        UPGRADE
    }

    /**
     * Der Typ des Entitys. Dieser Wert muss in einer von AbstractEntity
     * erbenden Klasse im Konstruktor gesetzt werden.
     */
    protected EntityType                 type;

    /** Der Name des Entitys */
    private String                       name;

    /**
     * Enthält die Attribute des Entitys. Für jeden Condition-Typ aus der Klasse
     * Condition muss hier ein Speicherplatz im Array bereitgehalten werden.
     */
    @SuppressWarnings( "rawtypes" )
    private Condition                    attributes[] = new Condition[Condition.ConditionTypes.values().length];

    /** Liste von Listenern */
    private EventListenerList            listenerList = new EventListenerList();

    private AbstractEntity               parent;

    protected ArrayList <AbstractEntity> entities     = new ArrayList <AbstractEntity>();

    public AbstractEntity() {
        super();
        init();
    }

    public AbstractEntity( EntityType type ) {
        this();
        this.type = type;
    }

    /**
     * initialisiert das ArmyListEntity.
     */
    private void init() {

        SimpleNumber sn = new SimpleNumber( 0 );
        setAttribute( sn, ConditionTypes.COUNT );

        sn = new SimpleNumber( 0 );
        setAttribute( sn, ConditionTypes.PRICE );

    }

    /**
     * Klont dieses Entity.
     * 
     * @param parent
     *            Der Elternknoten. Der
     *            Parent-Knoten wird für das klonen der Attribute des Entities
     *            gebraucht. Wird der Root Knoten geklont kann hier
     *            <code>null</code> übergeben werden.
     * 
     * @return Das geklonte Entity
     * @throws CloneNotSupportedException
     */
    public abstract AbstractEntity cloneFor( AbstractEntity parent ) throws CloneNotSupportedException;

    /**
     * Gibt den Namen des Entities aus.
     */
    public abstract String toString();

    /**
     * Setzt ein Attribut des Entitys.
     * 
     * @param attribute
     *            Das zu setzende Attribut.
     * @param type
     *            Der Typ des Attributes.
     */
    @SuppressWarnings( "rawtypes" )
    public void setAttribute( Condition attribute, ConditionTypes type ) {
        attribute.setOwner( getEntityNode( this ) );
        attributes[type.ordinal()] = attribute;
        attribute.fireValueChanged();
    }

    /**
     * Durchläuft den Entity-Baum nach oben in Richtung root und gibt das erste
     * Entity vom Typ <code>NODE</code> zurück.
     * 
     * @param e
     * @return
     */
    protected AbstractEntity getEntityNode( AbstractEntity e ) {
        if ( e == null )
            return e;
        if ( e.getType() == EntityType.NODE || e.getType() == EntityType.CATEGORY || e.getType() == EntityType.ROOT )
            return e;
        else return getEntityNode( e.getParent() );
    }

    /**
     * Setzt ein Attribut des Entitys.
     * 
     * @param value
     *            Der Wert des Attributes
     * @param type
     *            Der Typ des Attributes.
     */
    public void setAttribute( int value, ConditionTypes type ) {
        Condition <Integer> attribute = getAttribute( type );
        attribute.setValue( value );
        setAttribute( attribute, type );
    }

    /**
     * Gibt das Attribut vom übergebenen Typ zurück.
     * 
     * @param type
     *            Der Typ des Attributes welches gelesen werden soll
     * @return Das Attribut als Condition.
     */
    @SuppressWarnings( "rawtypes" )
    public Condition getAttribute( ConditionTypes type ) {
        if ( attributes[type.ordinal()] == null ) {
            attributes[type.ordinal()] = new SimpleNumber( 0 );
        }
        return attributes[type.ordinal()];
    }

    /**
     * Fügt der Liste von Listenern einen Listener hinzu
     * 
     * @param l
     *            Der Listener, der hinzugefügt werden soll.
     */
    public void addEntityListener( EntityListener l ) {
        listenerList.add( EntityListener.class, l );
    }

    /**
     * Entfernt einen Listener aus der Liste von Listenern.
     * 
     * @param l
     *            der Listender, der entfernt werden soll.
     */
    public void removeEntityListener( EntityListener l ) {
        listenerList.remove( EntityListener.class, l );
    }

    /**
     * Übergibt allen Listenern das AttribChangedEvent.
     */
    public void fireAttribChanged() {
        Object[] listeners = listenerList.getListenerList();
        for ( int i = listeners.length - 2; i >= 0; i -= 2 ) {
            if ( listeners[i] == EntityListener.class ) {
                ( (EntityListener) listeners[i + 1] ).AttribChanged( "someAttrib" );
            }
        }
    }

    /**
     * Gibt den Typ des Entitys zurück.
     * 
     * @return Der Typ des Entitys.
     */
    public EntityType getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Vergleicht zwei AbstractEntitys miteinander.
     */
    public boolean equals( Object obj ) {
        if ( obj == null )
            return false;
        AbstractEntity entity;
        try {
            entity = (AbstractEntity) obj;
        }
        catch ( ClassCastException e ) {
            return false;
        }

        String thisName = this.getName();
        String hisName = entity.getName();
        if ( thisName == null )
            thisName = "";
        if ( hisName == null )
            hisName = "";
        // ID darf nicht verglichen werden, weil ArmyListEntity A == Enitiy B
        // wenn
        // die ID
        // unterschiedlich ist.
        return thisName.equals( hisName )
                        && attributes[ConditionTypes.PRICE.ordinal()].getValue() == entity.getAttribute(
                                        ConditionTypes.PRICE ).getValue()
                        && attributes[ConditionTypes.COUNT.ordinal()].getValue() == entity.getAttribute(
                                        ConditionTypes.COUNT ).getValue();
    }

    @Override
    public void valueChanged() {
        fireAttribChanged();
    }

    public AbstractEntity getParent() {
        return parent;
    }

    public void setParent( AbstractEntity parent ) {
        this.parent = parent;

        // wenn der parent gesetzt wird, müssen die owner der attribute neu
        // angepasst werden:
        for ( ConditionTypes attType : ConditionTypes.values() ) {
            if ( attributes[attType.ordinal()] != null )
                attributes[attType.ordinal()].setOwner( getEntityNode( parent ) );
        }

        // Die Owner der Kinder müssen auch neu angepasst werden:
        for ( AbstractEntity child : entities ) {
            child.setParent( this );
        }
    }

    public Boolean addEntity( AbstractEntity entity ) {
        ( (AbstractEntity) entity ).setParent( this );
        return entities.add( entity );
    }

    public void removeEntity( AbstractEntity entity ) {
        int idx = getIndexOf( entity );
        if ( idx >= 0 ) {
            entities.remove( idx );
            ( (AbstractEntity) entity ).setParent( null );
        }
    }

    public AbstractEntity getEntityAt( int index ) {
        return entities.get( new Integer( index ) );
    }

    public int getChildCount() {
        return entities.size();
    }

    public int getIndexOf( AbstractEntity e ) {
        return entities.indexOf( e );
    }

    public boolean isLeaf() {
        return entities.isEmpty();
    }

    public List <AbstractEntity> getChilds() {
        return entities;
    }
}
