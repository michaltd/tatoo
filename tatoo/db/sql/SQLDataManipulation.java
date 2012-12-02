package tatoo.db.sql;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import tatoo.db.DataManipulation;
import tatoo.db.Dataset;
import tatoo.db.sql.DBSchemaPattern.SchemaType;

/**
 * Implementierung der DataManipulation für die Verbindung mit der SQL-Datenbank
 * H2.
 * 
 * @see tatoo.db.DataManipulation
 * @author mkortz
 */
public class SQLDataManipulation extends DataManipulation {

    Connection                                                dbconn;

    private String                                            t_name;
    private String                                            t_class;
    private Hashtable <String, String>                        t_values            = new Hashtable <String, String> ();
    private String                                            condition;

    protected static HashMap <Integer, ArrayList <Class <?>>> itemsInWriteProcess = new HashMap <Integer, ArrayList <Class <?>>> ();

    public SQLDataManipulation (Connection sqlConnection, DBSchema schema) {
        super (schema);
        dbconn = sqlConnection;
    }

    @Override
    public DataManipulation setTableName (String name) {
        t_name = name;
        return this;
    }

    @Override
    public DataManipulation setCondition (String condition) {
        this.condition = condition;
        return this;
    }

    @Override
    public DataManipulation alterValues (String... columnValues) {
        for (String valString : columnValues) {
            alterValue (valString);
        }
        return this;
    }

    @Override
    public DataManipulation alterValue (String columnValue) {
        String columnSignature[] = columnValue.split ("=");
        if (columnSignature.length != 2)
            ;// TODO throw someException
        t_values.put (columnSignature[0], columnSignature[1]);
        return this;
    }

    @Override
    public boolean delete () {
        String sql = new String ("DELETE FROM  \"" + t_name + "\" ");
        if (condition.length () > 0)
            sql += "WHERE " + condition;
        boolean result = false;
        try {
            result = execute (sql) > 0 ? true : false;
        }
        catch (SQLException e) {
            e.printStackTrace ();
        }
        return result;
    }

    @Override
    public boolean delete (Dataset dataset) {
        collectFieldParameters (dataset, dataset.getClass ());
        return delete ();
    }

    /**
     * Fügt die übergebenen Daten in die Datenbank ein. Sollte nicht aufgerufen
     * werden wenn das einfügen über die Methode {@link #insert(Dataset)}
     * möglich ist.
     * 
     * @throws SQLException
     */
    private int insert () throws SQLException {
        if (t_name == null || t_values.size () == 0)
            return -1;
        String insertColumnNames = new String ("");
        String insertColumnValues = new String ("");
        String selectString = "";
        for (String columnName : t_values.keySet ()) {
            if (insertColumnNames.length () > 0)
                insertColumnNames += ", ";
            if (insertColumnValues.length () > 0)
                insertColumnValues += ", ";
            if (selectString.length () > 0)
                selectString += " and ";
            insertColumnNames += columnName;
            insertColumnValues += t_values.get (columnName);
            selectString += columnName + " = " + t_values.get (columnName);
        }
        String sql = "";
        if (t_name.equalsIgnoreCase ("dataset"))
            sql = new String ("INSERT INTO \"dataset\" (type) values ('" + t_class + "');");
        else sql = new String ("INSERT INTO \"" + t_name + "\" (" + insertColumnNames + ") values ("
                        + insertColumnValues + ");");
        int result = -1;
        Statement stmt = dbconn.createStatement ();
        String queryStmt = "select * from \"" + t_name + "\" where " + selectString + ";";
        stmt.execute (queryStmt);
        ResultSet rsltSet = stmt.getResultSet ();
        if (rsltSet == null || !rsltSet.first())
            result = execute (sql);
        else
            result = -2;
        return result;
    }

    /**
     * Fügt das Dataset in die Datenbank ein und vergibt eine eindeutige ID für
     * das Objekt.
     */
    @Override
    public int insert (Dataset dataset) {
        if (dataset == null)
            return -1;
        Dataset datset = getFromCache (dataset.getId ());
        if (datset != null)
            return datset.getId ();
        else return insert (dataset, dataset.getClass ());
    }

    /**
     * Fügt das Objekt, welches der übergebenen Klasse entspricht in die
     * Datenbank ein. Da das einzufügende Dataset mitsamt seinen Parents
     * geschrieben werden muss, wird diese rekursive Methode benutzt. Sie ruft
     * sich selbst für jede Oberklasse des übergebenen Objekts aus.
     * 
     * @param dataset
     * @param c
     * @return
     */
    private int insert (Dataset dataset, Class <?> c) {

        this.t_class = dataset.getClass ().getName ();
        int id = dataset.getId ();

        // Das Dataset ist offensichtlich schon vorhanden
        if (id > 0)
            return id;

        Class <?> superclass = c.getSuperclass ();
        // solange rekursiv insert(Dataset, Class) mit der Oberklasse aufrufen
        // bis die Klasse Object erreicht ist.

        if (superclass != Object.class) {
            SQLDataManipulation insertStmt = new SQLDataManipulation (dbconn, schema);
            id = insertStmt.insert (dataset, superclass);
        }
        collectFieldParameters (dataset, c);

        // wenn die id == 0 ist handelt es sich in jedem Fall um die
        // Dataset-Klasse -> (c.getSimpleName() == "Dataset") da zuerst das
        // Dataset geschrieben wird:
        // wir kommen von Oben und gehen rekursiv in der Klassenhirarchie nach
        // unten zur Wurzel. Die Wurzel ist die Klasse Dataset!
        // dieses Objekt wird also sofort in den Cache geschoben, da es gerade
        // in die DB eingefügt wurde.
        if (id == 0) {
            try {
                id = insert ();
            }
            catch (SQLException sqle) {}

            // die id des Objektes setzen. Geschieht über Reflection weil setId
            // eine private Methode ist.
            try {
                Method setId = c.getDeclaredMethod ("setId", int.class);
                setId.setAccessible (true);
                setId.invoke (dataset, id);
            }
            catch (SecurityException e) {
                e.printStackTrace ();
            }
            catch (NoSuchMethodException e) {
                e.printStackTrace ();
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace ();
            }
            catch (IllegalAccessException e) {
                e.printStackTrace ();
            }
            catch (InvocationTargetException e) {
                e.printStackTrace ();
            }
            // das erzeugte Objekt wird zwecks Wiederverwendung (z.B. bei
            // Referenzen darauf) hier in den Cache gelegt
            addToCache (dataset);
        }
        else {
            // jede Tabelle (außer das Dataset selbst) bekommt die dataset_id
            // übergeben.
            alterValue ("dataset_id = " + id);

            try {
                insert ();
            }
            catch (SQLException sqle) {}
        }

        // // TODO: das folgende auslagern in eine eigene Methode?
        // // ist der Datensatz erst mal eingetragen müssen die Sets behandelt
        // // werden
        updateSets (dataset, c, true);
        return id;
    }

    @Override
    public boolean update () {
        if (t_name == null || t_values.size () == 0)
            return false;
        String setStatement = new String ("");
        for (String columnName : t_values.keySet ()) {
            setStatement += " " + columnName + " = " + t_values.get (columnName) + ", ";
        }
        String sql = "";
        try {
            sql = new String ("UPDATE \"" + t_name + "\" SET "
                            + setStatement.substring (0, setStatement.lastIndexOf (',')));
        }
        catch (IndexOutOfBoundsException e) {
            sql = "foobar";
        }
        if (condition.length () > 0)
            sql += " WHERE " + condition;
        sql += ";";
        boolean result = false;
        try {
            result = execute (sql) > 0 ? true : false;
        }
        catch (SQLException e) {
            e.printStackTrace ();
        }
        return result;
    }

    @Override
    public boolean update (Dataset dataset) {
        return update (dataset, dataset.getClass ());
    }

    private boolean update (Dataset dataset, Class <?> c) {
        boolean result = false;
        if ( !itemsInWriteProcess.containsKey (dataset.getId ())) {
            this.t_class = dataset.getClass ().getName ();

            Class <?> superclass = c.getSuperclass ();
            // solange rekursiv update(Dataset, Class) mit der Oberklasse
            // aufrufen
            // bis die Klasse Dataset erreicht ist.

            if (superclass != Dataset.class) {
                SQLDataManipulation insertStmt = new SQLDataManipulation (dbconn, schema);
                result = insertStmt.update (dataset, superclass);
            }
            collectFieldParameters (dataset, c);

            setCondition ("dataset_id = " + dataset.getId ());

            update ();

            if (itemsInWriteProcess.containsKey (dataset.getId ()))
                itemsInWriteProcess.get (dataset.getId ()).add (c);
            else {
                ArrayList <Class <?>> classList = new ArrayList <Class <?>> ();
                classList.add (c);
                itemsInWriteProcess.put (dataset.getId (), classList);
            }
            updateSets (dataset, c, false);
        }
        return result;
    }

    private void updateSets (Dataset dataset, Class <?> c, boolean isInsert) {

        String table = schema.getTableName (c);
        if (table != null) {
            DBSchemaPattern def = schema.getTable (c);
            if (def.getSchemaType () == SchemaType.CLASS) {
                // die Sets durchlaufen ...
                for (DBSchemaSetPattern set : ((DBSchemaClassPattern) def).getSets ()) {
                    // existiert die Tabelle für das Objekt? Sie wird auf jeden
                    // Fall angelegt:
                    SQLDataDefinition ddf = new SQLDataDefinition (dbconn, schema);
                    ddf.setTableName (set.getTableName ());
                    ddf.addColumns ("one_id:INTEGER", "to_many_id:INTEGER");
                    ddf.create ();

                    // ... und die einzelnen Objecte darin ...
                    Object[] objects = {};
                    try {
                        objects = set.getContent (c, dataset);
                    }
                    catch (SecurityException e) {
                        e.printStackTrace ();
                        continue;
                    }
                    catch (IllegalArgumentException e) {
                        e.printStackTrace ();
                        continue;
                    }
                    catch (NoSuchFieldException e) {
                        e.printStackTrace ();
                        continue;
                    }
                    catch (IllegalAccessException e) {
                        e.printStackTrace ();
                        continue;
                    }

                    for (Object item : objects) {
                        Dataset itemOfSet;
                        if (Dataset.class.isInstance (item)) {
                            itemOfSet = (Dataset) item;
                        }
                        else {
                            // objectClass = item.getClass();
                            continue;
                        }

                        int itemId = itemOfSet.getId ();
                        // ... in die Datenbank sichern.

                        DataManipulation insertItem = new SQLDataManipulation (dbconn, schema);
                        if (isInsert)
                            itemId = insertItem.insert (itemOfSet);
                        else {
                            // existiert das Dataset schon in der DB?
                            if (itemId > 0)
                                insertItem.update (itemOfSet);
                            else itemId = insertItem.insert (itemOfSet);
                        }
                        // ist das Objekt gesichert muss es in die entsprechende
                        // set-Tabelle eingetragen werden,
                        // um die Verbindung zu dem hier behandelten Datensatz
                        // zu schaffen
                        if (itemId > 0) {
                            SQLDataManipulation insertSet = new SQLDataManipulation (dbconn, schema);
                            insertSet.setTableName (set.getTableName ());

                            Class tmp_class = itemOfSet.getClass ();
                            String targetTable = schema.getTableName (tmp_class);
                            while (targetTable == null && tmp_class != null) {
                                tmp_class = tmp_class.getSuperclass ();
                                targetTable = schema.getTableName (tmp_class);
                            }
                            insertSet.alterValue ("to_many_id = " + itemOfSet.getId ());
                            insertSet.alterValue ("one_id = " + dataset.getId ());
                            try {
                                insertSet.insert ();
                            }
                            catch (SQLException sqle) {
                                // State 42S02 = Table not Found -> Sie muss
                                // angelegt werden!
                                if (sqle.getSQLState ().equals ("42S02")) {
                                    try {
                                        insertSet.insert ();
                                    }
                                    catch (SQLException e) {
                                        e.printStackTrace ();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    /**
     * Führt das übergebene SQL-Statement aus und gibt die erzeugte ID zurück.
     * Wenn keine ID erzeugt wurde, wird -1 zurückgeliefert.
     * 
     * @param sqlStatement
     * @return
     * @throws SQLException
     */
    private int execute (String sqlStatement) throws SQLException {
        Statement stmt = dbconn.createStatement ();
        stmt.execute (sqlStatement);
        if (stmt.getUpdateCount () == 1) {
            ResultSet rslt = stmt.getGeneratedKeys ();
            if (rslt.first ())
                return rslt.getInt (1);
            else return -1;
        }
        if (stmt.getUpdateCount () > 1) { throw new SQLException ("es wurden zu viele Ergebnisse geliefert."); }
        return -1;
    }

    /**
     * Baut aus dem Dataset die Parameter für das SQL-Statement zusammen. Dabei
     * werden nur die Attribute der zusätzlich übergebenen Klasse geschrieben.
     * 
     * @param dataset
     * das Objekt des Datasets
     * @param class Die Klasse die behandelt werden soll
     */
    private void collectFieldParameters (Dataset dataset, Class <?> c) {
        // zunächst den Tabellennamen heraussuchen
        t_name = schema.getTableName (c);
        // es kommt vor, dass die Klasse nicht im Schema auftaucht, weil sie
        // selbst keine speichernden Attribute
        // besitzt. Dann muss hier abgebrochen werden!
        if (t_name == null)
            return;

        DBSchemaPattern def = schema.getTable (c);
        if (def.getSchemaType () != SchemaType.CLASS)
            return;
        for (DBSchemaPattern field : ((DBSchemaClassPattern) def).getFields ()) {
            String value = "";
            try {
                // Den Wert aus des Attributs für das Feld aus dem
                // Dataset-Objekt holen
                Object oValue = ((DBSchemaPropertyPattern) field).getValue (c, dataset);
                if (oValue == null)
                    continue;
                // wenn es sich hier wiederum um ein Objekt handelt welches
                // wieder ein Dataset ist, muss
                // dieses zunächst selbst weggeschrieben werden. Ansonsten wird
                // einfach der Wert gesetzt
                // das funktioniert nicht für bidirektionale Beziehungen! In
                // einem solchen Fall dürfte es eine Enlosschleife geben
                // Da muss eventuell in Zukunft etwas mit "static" Listen hier
                // in der Klasse SQLDataManipulation gemacht werden in
                // der alle instanzen der zu speichernden Objekte Referenziert
                // sind, um doppelte einträge zu vermeiden.
                // Oder so.... irgendwie .... :)
                if (Dataset.class.isInstance (oValue)) {
                    SQLDataManipulation insertStmt = new SQLDataManipulation (dbconn, schema);
                    if (((Dataset) oValue).getId () == 0)
                        value = ((Integer) insertStmt.insert ((Dataset) oValue)).toString ();
                    else value = ((Integer) ((Dataset) oValue).getId ()).toString ();
                }
                else {
                    if (oValue.getClass ().isEnum () || oValue.getClass ().getSuperclass ().isEnum ())
                        value = ((Enum) oValue).name ();
                    else value = oValue.toString ();
                }
                // }
                if (oValue.getClass ().getSimpleName ().equalsIgnoreCase ("string") || oValue.getClass ().isEnum ()
                                || oValue.getClass ().getSuperclass ().isEnum ())
                    alterValue (((DBSchemaPropertyPattern) field).getDatabaseFieldName () + " = '" + value + "'");
                else alterValue (((DBSchemaPropertyPattern) field).getDatabaseFieldName () + " = " + value);
            }
            catch (SecurityException e) {
                e.printStackTrace ();
            }
            catch (NoSuchFieldException e) {
                e.printStackTrace ();
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace ();
            }
            catch (IllegalAccessException e) {
                e.printStackTrace ();
            }

        }
    }

}
