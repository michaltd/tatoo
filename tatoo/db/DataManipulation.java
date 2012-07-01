package tatoo.db;

public abstract class DataManipulation extends DataHandler{
	
	public abstract DataManipulation setTableName(String string);

	public abstract DataManipulation setCondition(String string);

	/**
	 * Fügt eine Änderung eines Wertes in die Manipulation ein.
	 * Die Änderung muss in der Form "Feld=<Wert>" übergeben werden. Es wird geprüft ob das Feld 
	 * in der Tabelle existiert. Wenn es nicht existiert wird eine SQLException geworfen.
	 * @param string
	 * @return
	 */
	public abstract DataManipulation alterValue(String string);
	
	public abstract DataManipulation alterValues(String... columnValues);
	
	/**
	 * Fügt das Dataset in die Datenbank ein.
	 * @param dataset
	 * @return
	 */
	public abstract int insert(Dataset dataset);
	
	public abstract int delete();
	
	public abstract int delete(Dataset dataset);
	
	public abstract int update();
	
	public abstract int update(Dataset dataset);

  

}
