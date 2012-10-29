package tatoo.db.sql;

import java.lang.reflect.Field;

import java.util.ArrayList;

import javax.swing.event.EventListenerList;

import org.jdom.Attribute;
import org.jdom.Element;

import tatoo.db.Dataset;

/**
 * Abbildung des Set-Elements aus der Schemadatei. Ein Set bildet 1:n oder n:m
 * Assoziationen auf Tabellen ab.
 * 
 * @author mkortz
 */
public class DBSchemaSetPattern extends DBSchemaPattern {

    /**
     * Name der Tabelle in die das Attribut gespeichert werden soll.
     */
    private String tableName;
    /**
     * Der Typ des Attributes. Werte können z.B. ArrayList uä. sein.
     */
    private String type;

    /**
     * Istantiiert eine Set-Schablone aus dem übergebenen Element.
     * 
     * @param el
     * Das XML-Element aus dem die Set-Schablone instaniiert wird.
     */
    public DBSchemaSetPattern (Element el) {

        this.definitonType = SchemaType.SET;

        Attribute setNameAttrib = el.getAttribute ("name");
        this.name = setNameAttrib.getValue ();
        Attribute tableNameAttrib = el.getAttribute ("table");
        this.tableName = tableNameAttrib.getValue ();
        Attribute typeAttrib = el.getAttribute ("type");
        this.type = typeAttrib.getValue ();

    }

    /**
     * Gibt den Namen der Tabelle zurück in die das set gespeichert wird.
     * 
     * @return Der Name der Tabelle
     */
    public String getTableName () {
        return tableName;
    }

    /**
     * Gibt den Typ des Attributes zurück in welchem die Objekte gespeichert
     * sind.
     * 
     * @return Der Typ des Attributes (ArrayList, ListenerList usw.)
     */
    public String getType () {
        return this.type;
    }

    /**
     * Gibt den Inhalt des Attributes des Datasets als ein Array aus Objekten
     * zurück.
     * 
     * @return ein Array aus Objekten
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public Object[] getContent (Class <?> c, Dataset ds) throws SecurityException, NoSuchFieldException,
                    IllegalArgumentException, IllegalAccessException {
        Field fld = c.getDeclaredField (getName ());
        fld.setAccessible (true);
        Object o = fld.get (c.cast (ds));
        if (type.equalsIgnoreCase ("ArrayList")) { return ((ArrayList) o).toArray (); }
        if (type.equalsIgnoreCase ("EventListenerList")) { return ((EventListenerList) o).getListenerList (); }
        return null;
    }
}
