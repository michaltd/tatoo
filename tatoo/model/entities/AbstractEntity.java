package tatoo.model.entities;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.EventListenerList;

import tatoo.db.Dataset;
import tatoo.model.conditions.CalculatedNumber;
import tatoo.model.conditions.CalculatedNumber.Arithmetic;
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
 */
public abstract class AbstractEntity extends tatoo.db.Dataset implements EntityBase, ConditionListener {

    /**
     * Die möglichen Entitytypen. Ein ArmyListEntity kann einen typen annehmen.
     * Vom Typen hängt z.B. ab, wie das ArmyListEntity in der View dargestellt
     * wird oder ob und wenn ja welche, Unterknoten es haben darf.
     * 
     * @author mkortz
     */
    public static class EntityType extends Dataset {

        public static final EntityType ROOT;
        public static final EntityType CATEGORY;
        public static final EntityType NODE;
        public static final EntityType ANYOFUPGRADE;
        public static final EntityType ONEOFUPGRADE;
        public static final EntityType UPGRADE;

        private int                    ordinal;
        private String                 name;
        private static EntityType[]    values;
        private EntityType[]           possibleChildTypes;

        static {

            // jedes Attribut muss zunächst instanziiert und dann auf jeden Fall
            // mögliche Kinder zugewiesen bekommen!
            // Neue Attribute dürfen unter values nicht vergessen werden!
            UPGRADE = new EntityType (5, "UPGRADE");
            ONEOFUPGRADE = new EntityType (4, "ONEOFUPGRADE");
            ANYOFUPGRADE = new EntityType (3, "ANYOFUPGRADE");
            NODE = new EntityType (2, "NODE");
            CATEGORY = new EntityType (1, "CATEGORY");
            ROOT = new EntityType (0, "ROOT");

            UPGRADE.setChildTypes (new EntityType[] {ANYOFUPGRADE, ONEOFUPGRADE, UPGRADE});
            ONEOFUPGRADE.setChildTypes (new EntityType[] {ANYOFUPGRADE, ONEOFUPGRADE, UPGRADE});
            ANYOFUPGRADE.setChildTypes (new EntityType[] {ANYOFUPGRADE, ONEOFUPGRADE, UPGRADE});
            NODE.setChildTypes (new EntityType[] {ANYOFUPGRADE, ONEOFUPGRADE, UPGRADE});
            CATEGORY.setChildTypes (new EntityType[] {NODE});
            ROOT.setChildTypes (new EntityType[] {CATEGORY});

            values = new EntityType[] {ROOT, CATEGORY, NODE, ANYOFUPGRADE, ONEOFUPGRADE, UPGRADE};
        }

        // private EntityType (int ordinal, String name, EntityType[] t) {
        private EntityType (int ordinal, String name) {
            this.ordinal = ordinal;
            this.name = name;
        }
        
        //Konstruktor für das deserialisieren aus der DB
        public EntityType(){}

        private void setChildTypes (EntityType[] t) {
            this.possibleChildTypes = new EntityType[t.length];
            System.arraycopy (t, 0, possibleChildTypes, 0, t.length);
        }

        public String name () {
            return name;
        }

        public static EntityType[] values () {
            return values;
        }

        public int ordinal () {
            return ordinal;
        }

        public EntityType[] getChildTypes () {
            return possibleChildTypes;
        }

        public String toString () {
            return name;
        }

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
    private CalculatedNumber             attributes[] = new CalculatedNumber[Condition.ConditionTypes.values ().length];

    /** Liste von Listenern */
    private EventListenerList            listenerList = new EventListenerList ();

    /** Vater dieses Entities */
    private AbstractEntity               parent;

    protected ArrayList <AbstractEntity> entities     = new ArrayList <AbstractEntity> ();

    public AbstractEntity () {
        super ();
        init ();
    }

    public AbstractEntity (EntityType type) {
        this ();
        this.type = type;
    }

    /**
     * initialisiert das ArmyListEntity.
     */
    private void init () {

        // intitialisieren der Attribute
        for (int i = 0; i < Condition.ConditionTypes.values ().length; i++ ) {
            SimpleNumber simpleNo = new SimpleNumber (1);
            CalculatedNumber calcNo = new CalculatedNumber (simpleNo, 0, Arithmetic.MULTIPLY);
            calcNo.setOwner (this);
            attributes[i] = calcNo;
            attributes[i].addChangeListener (this);
        }

    }

    /**
     * Klont dieses Entity.
     * 
     * @param parent
     * Der Elternknoten. Der Parent-Knoten wird für das klonen der Attribute des
     * Entities gebraucht. Wird der Root Knoten geklont kann hier
     * <code>null</code> übergeben werden.
     * @return Das geklonte Entity
     * @throws CloneNotSupportedException
     */
    // TODO: das ist blöd hier. Eventuell durch eine Factory ersetzen oder so?
    public abstract AbstractEntity cloneFor (AbstractEntity parent) throws CloneNotSupportedException;

    /**
     * Gibt den Namen des Entities aus.
     */
    public abstract String toString ();

    /**
     * Setzt ein Attribut des Entitys.
     * 
     * @param attribute
     * Das zu setzende Attribut.
     * @param type
     * Der Typ des Attributes.
     */
    @SuppressWarnings ("rawtypes")
    public void setAttribute (Condition attribute, ConditionTypes type) {
        if (attribute.getOwnerNode () == null)
            attribute.setOwner (this);
        // keine Kreise erzeugen indem die CalculatedCondition sich selbst als
        // Source übergeben bekommt!
        if (attributes[type.ordinal ()] != attribute) {
            attributes[type.ordinal ()].setValue (attribute);
            fireAttribChanged (type);
        }
    }

    /**
     * Durchläuft den Entity-Baum nach oben in Richtung root und gibt das erste
     * Entity vom Typ <code>NODE</code> zurück. Handelt es sich bei dem
     * übergebenen Entity um ein Entity vom Typ CATEGORY oder ROOT wird dieses
     * Entity zurück gegeben.
     * 
     * @param e
     * Das Entity dessen Ursprung zurückgegeben werden soll
     * @return Das Ursprungsentity
     */
    protected AbstractEntity getEntityNode (AbstractEntity e) {
        if (e == null)
            return e;
        if (e.getType () == EntityType.NODE || e.getType () == EntityType.CATEGORY || e.getType () == EntityType.ROOT)
            return e;
        else return getEntityNode (e.getParent ());
    }

    /**
     * Durchläuft den Entity-Baum nach oben in Richtung root und gibt das erste
     * Entity vom Typ <code>NODE</code> zurück. Handelt es sich bei dem
     * übergebenen Entity um ein Entity vom Typ CATEGORY oder ROOT wird dieses
     * Entity zurück gegeben.
     * 
     * @return Das Ursprungsentity
     */
    public AbstractEntity getEntityNode () {
        return getEntityNode (this);
    }

    /**
     * Setzt ein Attribut des Entitys.
     * 
     * @param value
     * Der Wert des Attributes
     * @param type
     * Der Typ des Attributes.
     */
    public void setAttribute (int value, ConditionTypes type) {
        Condition <Integer> attribute = getAttribute (type);
        attribute.setValue (value);
        fireAttribChanged (type);
    }

    /**
     * Gibt das Attribut vom übergebenen Typ zurück.
     * 
     * @param type
     * Der Typ des Attributes welches gelesen werden soll
     * @return Das Attribut als Condition.
     */
    @SuppressWarnings ("rawtypes")
    public Condition getAttribute (ConditionTypes type) {
        if (attributes[type.ordinal ()] == null) { return null;
        // attributes[type.ordinal()] = new SimpleNumber( 0 );
        }
        attributes[type.ordinal ()].setOwner (this);
        return attributes[type.ordinal ()];
    }

    /**
     * Fügt der Liste von Listenern einen Listener hinzu
     * 
     * @param l
     * Der Listener, der hinzugefügt werden soll.
     */
    public void addEntityListener (EntityListener l) {
        listenerList.add (EntityListener.class, l);
    }

    /**
     * Entfernt einen Listener aus der Liste von Listenern.
     * 
     * @param l
     * der Listender, der entfernt werden soll.
     */
    public void removeEntityListener (EntityListener l) {
        listenerList.remove (EntityListener.class, l);
    }

    /**
     * Übergibt allen Listenern das AttribChangedEvent.
     * 
     * @param attribute
     * TODO
     */
    public void fireAttribChanged (ConditionTypes attribute) {
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == EntityListener.class) {
                ((EntityListener) listeners[i + 1]).AttribChanged (this, attribute);
            }
        }
    }

    /**
     * Gibt den Typ des Entitys zurück.
     * 
     * @return Der Typ des Entitys.
     */
    public EntityType getType () {
        return type;
    }

    /**
     * Setzt den Typ des Entitys
     * 
     * @param type
     * Der neue Typ des Entities
     */
    public void setType (EntityType type) {
        this.type = type;
    }

    @Override
    public String getName () {
        return name;
    }

    /**
     * Gibt den Namen samt Pfad zum Ursprungsknoten zurück.
     * 
     * @return
     */
    public String getFullName () {
        if (getParent () == null)
            return getName ();

        return getName () + "." + getParent ().toString ();
    }

    @Override
    public void setName (String name) {
        this.name = name;
        fireAttribChanged (null);
    }

    /**
     * Vergleicht zwei AbstractEntitys miteinander.
     */
    public boolean equals (Object obj) {
        if (obj == null)
            return false;
        AbstractEntity entity;
        try {
            entity = (AbstractEntity) obj;
        }
        catch (ClassCastException e) {
            return false;
        }

        String thisName = this.getName ();
        String hisName = entity.getName ();
        if (thisName == null)
            thisName = "";
        if (hisName == null)
            hisName = "";
        // ID darf nicht verglichen werden, weil ArmyListEntity A == Enitiy B
        // wenn die ID unterschiedlich ist.
        return thisName.equals (hisName)
                        && attributes[ConditionTypes.PRICE.ordinal ()].getValue () == entity.getAttribute (
                                        ConditionTypes.PRICE).getValue ()
                        && attributes[ConditionTypes.COUNT.ordinal ()].getValue () == entity.getAttribute (
                                        ConditionTypes.COUNT).getValue ();
    }

    @Override
    public void valueChanged (Condition source) {
        for (ConditionTypes type : ConditionTypes.values ())
            if (getAttribute (type) == source)
                fireAttribChanged (type);
    }

    public AbstractEntity getParent () {
        return parent;
    }

    public void setParent (AbstractEntity parent) {
        this.parent = parent;
    }

    public Boolean addEntity (AbstractEntity entity) {
        ((AbstractEntity) entity).setParent (this);
        return entities.add (entity);
    }

    public void removeEntity (AbstractEntity entity) {
        int idx = getIndexOf (entity);
        if (idx >= 0) {
            entities.remove (idx);
            ((AbstractEntity) entity).setParent (null);
        }
    }

    public AbstractEntity getEntityAt (int index) {
        return entities.get (new Integer (index));
    }

    /**
     * Gibt die Anzahl der Kinder zurück.
     * 
     * @return Die Anzahl der Kinder.
     */
    public int getChildCount () {
        return entities.size ();
    }

    public int getIndexOf (AbstractEntity e) {
        return entities.indexOf (e);
    }

    public boolean isLeaf () {
        return entities.isEmpty ();
    }

    /**
     * Gibt die Kinder des Entitys zurück.
     * 
     * @return Die Kinder des Entities.
     */
    public List <AbstractEntity> getChilds () {
        return entities;
    }

    public boolean hasChilds () {
        return !entities.isEmpty ();
    }
}
