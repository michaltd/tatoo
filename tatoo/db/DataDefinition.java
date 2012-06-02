package tatoo.db;

import java.util.Hashtable;


public abstract class DataDefinition extends DataHandler{

	protected String t_Name;
	/**
	 * Hashtable of columns holding pairs of columnname -> datatype
	 */
	protected Hashtable<String, String> t_columns = new Hashtable<String, String>();
	/**
	 * Hashtable of primarykeys holding pairs of columnname -> datatype
	 */
	protected Hashtable<String, String> t_pk = new Hashtable<String, String>();

	protected abstract boolean createTable();

	protected abstract boolean dropTable();

	// TODO addForeignKey kann auch ganz automatisiert laufen wenn der name der ID
	// aus einer anderen Tabelle bekannt ist:
	public void addForeignKey(String foreignTableName) {
		addForeignKey(foreignTableName + "_id", foreignTableName, foreignTableName
				+ "_id");
	}

	public final boolean create() {
		if (createTable()){
			//hier das einfügen in das xml schema erledigen!!		
			return true;
		}
		return false;
	};

	public final boolean drop() {
		if (dropTable()){
			//hier das löschen aus dem xml schema erledigen!!
			//ich mach hier mal nen Compilerfehler hin, dann finde ich das morgen gleich :)
			//DBSchema <-verwenden!!!
			return true;
		}
		return false;
	}

	public DataHandler addColumn(String column) {
		String columnSignature[] = column.split(":");
		if (columnSignature.length != 2)
			;//TODO throw someException
		t_columns.put(columnSignature[0], columnSignature[1]);
		return this;
	}

	public DataHandler addColumns(String... columns) {
		for (String colString : columns){
			String columnSignature[] = colString.split(":");
			if (columnSignature.length != 2)
				;//TODO throw someException
			t_columns.put(columnSignature[0], columnSignature[1]);
		}
		return this;
	}

	public DataHandler addPrimaryKey(String string) {
		String columnSignature[] = string.split(":");
		if (columnSignature.length != 2)
			return null;//TODO throw someException
		t_pk.put(columnSignature[0], columnSignature[1]);
		//t_columns.put(columnSignature[0], columnSignature[1]);
		return this;
	}

	public void addForeignKey(String selfColumn, String foreignTable, String foreignColumn) {
		//TODO: noch zu implementieren :)
	}

	public DataHandler setTableName(String name) {
		this.t_Name = name;
		return this;
	}

}
