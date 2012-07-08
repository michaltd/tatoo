package tatoo.db;

import java.util.Hashtable;

import tatoo.db.sql.DBSchema;

/**
 * Abstrakte Oberklasse für Datendefinitionen (Datenbeschreibungssprache).
 * Die Klasse ist die Grundlage für das erstellen, löschen und verändern von Datenstrukturen.
 * @author mkortz
 *
 */
public abstract class DataDefinition extends DataHandler{

  /**
   * Der Name der Datenstruktur. In Datenbanksichtweise wäre dies der Name einer Tabelle
   */
	protected String t_Name;
	/**
	 * Nimmt die Datenfelder auf, welche von der Datenoperation betroffen sind.
	 * Der Schlüssel enthält den Datenfeldname in der Datenstrukturen und der Wert den Datenfeldtypen.
	 */
	protected Hashtable<String, String> t_columns = new Hashtable<String, String>();
	/**
	 * Nimmt die Pimärschlüsseldefinitionen auf. 
	 * Der Schlüssel enthält den Datenfeldname in der Datenstrukturen und der Wert den Datenfeldtypen.
	 */
	protected Hashtable<String, String> t_pk = new Hashtable<String, String>();

	/**
	 * @param schema Das Datenbankschema welches für die Datenoperation benutzt wird.
	 * @see DataHandler#schema
	 */
	public DataDefinition(DBSchema schema) {
	  super(schema);
  }

	/**
	 * Erzeugt eine Datenstruktur mit den zuvor eingefügten Feldern.
	 * @return true wenn die Datenstruktur angelegt werden konnte. False sonst.
	 */
  protected abstract boolean create();
  /**
   * Löscht die Datenstruktur.
   * @return True wenn die Datenstruktur gelöscht werden konnte. False sonst.
   */
	protected abstract boolean drop();

	/**
	 * Fügt ein Datenfeld hinzu. Der übergebene String muss das folgende Format besitzen: "name:typ". Hat er das nicht wird eine 
	 * {@link DataDefinitionException} ausgelöst. Gibt diese Datendefinition als {@link DataHandler} zurück.
	 * @param Der übergebene String muss das folgende Format besitzen: "name:typ". 
	 * @return Das Objekt dieser DataDefinition
	 */
	public DataHandler addColumn(String column) {
		String columnSignature[] = column.split(":");
		if (columnSignature.length != 2)
			throw new DataDefinitionException("Wrong Column format") ;
		t_columns.put(columnSignature[0], columnSignature[1]);
		return this;
	}
	/**
   * Fügt mehrere Datenfelder hinzu. Die übergebenen String müssen das folgende Format besitzen: "name:typ". 
   * Haben sie das nicht wird eine {@link DataDefinitionException} ausgelöst. Gibt diese Datendefinition als {@link DataHandler} zurück.
   * @param Der übergebene String muss das folgende Format besitzen: "name:typ". 
   * @return Das Objekt dieser DataDefinition
   */
	public DataHandler addColumns(String... columns) {
		for (String colString : columns){
			addColumn(colString);
		}
		return this;
	}

	/**
	 * Fügt der Datenstruktur einen Primärschlüssel hinzu. Primärschlüssel sind Datenfelder, welche die einzelnen Datensätze in der 
	 * Datenstruktur identifizieren. Ein Primärschlüssel darf nicht in mehreren Datensätzen einer Datenstruktur vorkommen. 
	 * @param string Der übergebene String muss das folgende Format besitzen: "name:typ". 
	 * @return Das Objekt dieser DataDefinition
	 */
	public DataHandler addPrimaryKey(String string) {
		String columnSignature[] = string.split(":");
		if (columnSignature.length != 2)
			return null;//TODO throw someException
		t_pk.put(columnSignature[0], columnSignature[1]);
		return this;
	}

	/**
	 * Setzt den Namen der Datenstruktur.
	 * @param name Der Name als String
	 * @return Das Objekt dieser DataDefinition
	 */
	public DataHandler setTableName(String name) {
		this.t_Name = name;
		return this;
	}

}
