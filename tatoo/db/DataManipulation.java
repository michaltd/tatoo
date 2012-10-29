package tatoo.db;

import tatoo.db.sql.DBSchema;

/**
 * Manipuliert die gespeicherten Daten. Unter die Manipulation fallen das
 * einfügen, löschen und ändern von Datensätzen.
 * 
 * @author mkortz
 */
public abstract class DataManipulation extends DataHandler {

    /**
     * Eine DataManipulation wird normalerweise nicht manuell instanziiert.
     * Benutzen Sie dafür die Methode <code>createDataManipulation()</code> der
     * {@link DBConnection}. Diese kann über <code>
     * DBFactory.getInstance().getConnection()
     * </code> bezogen werden.
     * 
     * @param schema
     * Das Datenbankschema welches für die Datenmanipulation benutzt wird.
     * @see DBFactory
     * @see DBConnection
     */
    public DataManipulation (DBSchema schema) {
        super (schema);
    }

    /**
     * Setzt den Namen der Tabelle, die in der Datenmanipulation benutzt werden
     * soll.
     * 
     * @param string
     * Der Name der Tabelle
     * @return Ein Objekt dieser Datenmanipulation.
     */
    public abstract DataManipulation setTableName (String string);

    /**
     * Setzt eine Bedingung für die Datenmanipulation. Wenn z.B. nur Datensätze,
     * welche einem bestimmten Kriterium entsprechen geändert werden sollen,
     * wird eine Condition gesetzt. Es werden nur Datensätze geändert, welche
     * mit der Condition übereinstimmen.
     * 
     * @param string
     * Der String muss in der Form "name=bedingung" vorliegen. (dataset_id=5)
     * @return Ein Objekt dieser Datenmanipulation.
     */
    public abstract DataManipulation setCondition (String string);

    /**
     * Fügt eine Änderung eines Wertes in die Manipulation ein. Die Änderung
     * muss in der Form "Feld=Wert" übergeben werden. Es wird geprüft ob das
     * Feld in der Tabelle existiert. Wenn es nicht existiert wird eine
     * SQLException geworfen.
     * 
     * @param string
     * Das Feld und der Wert, den dieses annehmen soll.
     * @return Ein Objekt dieser Datenmanipulation.
     */
    public abstract DataManipulation alterValue (String string);

    /**
     * Fügt eine Änderung mehrerer Werte in die Manipulation ein. Jede Änderung
     * muss in der Form "Feld=Wert" übergeben werden. Es wird geprüft ob das
     * Feld in der Tabelle existiert. Wenn es nicht existiert wird eine
     * SQLException geworfen.
     * 
     * @param columnValues
     * Mehrere Felder mit jeweils dem Wert, den diese annehmen sollen.
     * @return Ein Objekt dieser Datenmanipulation.
     */
    public abstract DataManipulation alterValues (String... columnValues);

    // TODO: die Methoden delete und update müssen noch angepasst werden. Der
    // Rückgabewert sollte auf bool geändert werden.
    // Es ergibt keinen Sinnn jeweils eine (1) Id zurückgeben zu lassen, wenn
    // auch mehrere Datensätze betroffen sein können.
    // lediglich beim Insert muss die ID zurückgegeben werden. Beim Insert ist
    // aber auch nur ein Datensatz betroffen.
    /**
     * Fügt das übergebene dataset in die Datenbank ein.
     * 
     * @param dataset
     * Das Dataset wird in die Datenbank eingefügt.
     * @return die ID des neuen Datensatzes. Gibt -1 zurück wenn ein Fehler
     * aufgetreten ist.
     */
    public abstract int insert (Dataset dataset);

    /**
     * Löscht Datensätze mit den zuvor festgelegten Parametern. Werden keine
     * Parameter mit {@link #setCondition(String)} angegeben, so werden alle
     * Datensätze der Tabelle gelöscht.
     * 
     * @return True wenn das Löschen geklappt hat. False sonst.
     */
    public abstract boolean delete ();

    /**
     * Löscht das übergebene Dataset.
     * 
     * @param dataset
     * Das übergebene Dataset wird gelöscht.
     * @return True wenn das Löschen geklappt hat. False sonst.
     */
    public abstract boolean delete (Dataset dataset);

    /**
     * Ändert Datensätze mit den zuvor festgelegten Parametern. Werden keine
     * Parameter mit {@link #setCondition(String)} angegeben, so werden alle
     * Datensätze der Tabelle geändert.
     * 
     * @return True wenn das Ändern geklappt hat. False sonst.
     */
    public abstract boolean update ();

    /**
     * Ändert den Datensatz auf die neuen Werte des Datasets.
     * 
     * @param dataset
     * Das zu ändernde Dataset.
     * @return True wenn das Ändern geklappt hat. False sonst.
     */
    public abstract boolean update (Dataset dataset);

}
