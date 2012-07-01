package tatoo.db;

import java.util.LinkedList;

public abstract class Query extends DataHandler {
	
	public abstract Query get(Class<?> cl);
	
	public abstract Query addCondition(String condition);
	
	public abstract Query orderBy(String order);
	
	public abstract Query groupBy(String group);
	
	/**
	 * Führt die Abfrage aus. Liefert die gefundenen Objekte zurück. Wenn ein Objekt nicht gefunden wurde,
	 * oder ein anderer Fehler auftritt wird <code>null</code> zurück gegeben.
	 * @return
	 */
	public abstract LinkedList<Dataset> execute();

}
