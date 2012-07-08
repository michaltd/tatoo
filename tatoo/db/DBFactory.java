package tatoo.db;

import tatoo.db.DBConnection.DBases;
import tatoo.db.sql.SQLConnection;
import tatoo.model.entities.Entity;
import tatoo.model.entities.RootNode;

/**
 * Hält ein global nutzbare Datenverbindung aufrecht. 
 * Es können Objekte direkt in die Datenverbindung geschrieben oder daraus gelesen werden.
 *   
 * @author mkortz
 *
 */
public class DBFactory {
	
	private DBConnection conn;
	private static DBFactory factory= null;
	
	/**
	 * Erzeugen und initialisieren der Datenverbindung.
	 */
	private void init() {
		//Connection aufbauen
		//TODO hier muss per Reflection der "Treiber" geladen werden (SQLConnection, XMLConnection oder MySQLConnection usw.)
	  // zur zeit noch einfach so eingestellt
		conn = new SQLConnection(DBases.H2Server);
		//conn = new SQLConnection(DBases.H2);
		conn.connect();
	}
	
	/**
	 * Liefert die aktuell verwendete Datenverbindung zurück.
	 * @return
	 */
	public DBConnection getConnection(){
		return conn;
	}
	
	/**
	 * Liefert die eine Instanz der DBFactory zurück.
	 * @return die globale DBFactory
	 */
	public static DBFactory getInstance(){
		if (factory == null){
			factory = new DBFactory();
			factory.init();
		}
		return factory;
	}
	
	/**
   * Schreibt das übergebene Objekt o in die darunter liegende Datenstruktur. Ein Objekt, welches sich bereits in der Datenbank befindet,
   * wird lediglich angepasst, alle anderen neu geschrieben. Datasets erhalten nach den Schreibvorgang ihre ID.
   * @param Object o Das Objekt das geschrieben werden soll.
   * @return gibt true zurück wenn das schreiben geklappt hat. False sonst.
   */
  public boolean write(Dataset o) {
    return getConnection().write(o);
  }

  /**
   * Liest ein Objekt der übergebenen Klasse mit der angegebenen id aus der Datenstruktur.
   * @param c Die Klasse welche gelesen werden soll
   * @param id Die Id des zu lesenden Objektes
   * @return Das gelesene Objekt. Kann das Objekt nicht gelesen werden wird <code>null</code> zurückgegeben.
   */
  public Dataset read(Class<?> c, int id) {
    return getConnection().read(c, id);
    
  }
}
