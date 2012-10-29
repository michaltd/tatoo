package tatoo.db.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.jdom.Attribute;
import org.jdom.Element;

/**
 * Schablone für die Abbildung einer Klasse auf die Datenbankstruktur. Wird aus
 * der Schemadatei aufgebaut.
 * 
 * @author mkortz
 */
public class DBSchemaClassPattern extends DBSchemaPattern {

    /**
     * Enthält die Propertys aus der Schemadatei. Der Schlüssel ist der
     * Attributname der Klasse. Der Wert das entsprechende
     * {@link DBSchemaPropertyPattern} dazu.
     */
    private HashMap <String, DBSchemaPropertyPattern> fields = new HashMap <String, DBSchemaPropertyPattern> ();
    /**
     * Der Tabellenname in welche die Klasse gespeichert wird.
     */
    private String                                    tableName;
    /**
     * Enthält die Sets aus der Schemadatei. Der Schlüssel ist der Attributname
     * der Klasse. Der Wert das entsprechende {@link DBSchemaSetPattern} dazu.
     */
    private HashMap <String, DBSchemaSetPattern>      sets   = new HashMap <String, DBSchemaSetPattern> ();

    /**
     * Instanziiert das Pattern mit einem XML-Element aus der Schemadatei.
     * 
     * @param el
     */
    public DBSchemaClassPattern (Element el) {
        this.definitonType = SchemaType.CLASS;

        Attribute tableNameAttrib = el.getAttribute ("table");
        // if (tableNameAttrib == null)
        // throw someexception
        this.tableName = tableNameAttrib.getValue ();
        Attribute classNameAttrib = el.getAttribute ("name");
        this.name = classNameAttrib.getValue ();
        // über die einzelnen Elemente iterieren:
        for (Element child : (List <Element>) el.getChildren ()) {
            if (child.getName ().equalsIgnoreCase ("property")) {
                DBSchemaPropertyPattern prop = new DBSchemaPropertyPattern (child);
                fields.put (prop.getName (), prop);
            }
            if (child.getName ().equalsIgnoreCase ("set")) {
                DBSchemaSetPattern set = new DBSchemaSetPattern (child);
                this.sets.put (set.getName (), set);
            }
        }
    }

    /**
     * Gibt den Tabellennamen zurück in die die Klasse gespeichert wird.
     * 
     * @return der Tabellenname
     */
    public String getTableName () {
        return tableName;
    }

    /**
     * Gibt das zu speichernde Attribut zurück.
     * 
     * @param name
     * Der Name des Attributs
     * @return Die Attribute als DBSchemaPropertyPattern. Gibt <code>null</code>
     * zurück wenn das Attribut nicht gefunden werden kann.
     */
    public DBSchemaPropertyPattern getField (String name) {
        return fields.get (name);
    }

    /**
     * Gibt eine Liste aller zu speichernden Attribute zurück.
     * 
     * @return Eine Liste von Attributen als LIste aus DBSchemaPropertyPattern
     */
    public Collection <DBSchemaPropertyPattern> getFields () {
        return fields.values ();
    }

    /**
     * Gibt ein Set zurück.
     * 
     * @param name
     * Der Name des Attributes welches vom Set repräsentiert wird.
     * @return Das gesuchte Set. Gibt <code>null</code> zurück wenn das Set
     * nicht gefunden werden kann.
     */
    public DBSchemaSetPattern getSet (String name) {
        return sets.get (name);
    }

    /**
     * Gibt alle Sets zurück welche für die Klasse in der Schemadatei angegeben
     * sind.
     * 
     * @return Die Sets als Liste aus DBSchemaSetPattern. Gibt <code>null</code>
     * zurück wenn das Set nicht gefunden wird.
     */
    public Collection <DBSchemaSetPattern> getSets () {
        return sets.values ();
    }
}
