package tatoo.db.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

import org.h2.jdbc.JdbcSQLException;

import tatoo.db.DBConnection;
import tatoo.db.DataDefinition;
import tatoo.db.DataManipulation;
import tatoo.db.Dataset;
import tatoo.db.Query;

public class SQLConnection extends DBConnection {

	private Class<?> database = null;
	private Connection connection = null;
	
	public SQLConnection(DBases dbase) {
		this.dbase = dbase;
		try {
			database = Class.forName(dbase.getDriverString());
		} catch (ClassNotFoundException e) {
			System.err.println("No Databasedriver found: " + dbase.getDriverString()
					+ "\n is the Databasedriver installed?");
		}
		schema = new DBSchema(dbase.getPathToSchema());
	}

	public boolean connect() {
		if (database != null && connection == null)
			try {
				connection = DriverManager.getConnection(
						dbase.getConnectionString(), dbase.getUSER(), dbase.getPASSWD());
			} catch (SQLException e) {
			  System.err.println("Kein Zugriff auf die Datenbank.");
				e.printStackTrace();
			}
		return connection != null;
	}

	public boolean close() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	@Override
  public Query createQuery(){
		return new SQLQuery(connection, schema);
	}
	
	@Override
	protected DataDefinition createDataDefinition() {
		return new SQLDataDefinition(connection, schema);
	}

	@Override
	protected DataManipulation createDataManipulation() {
		return new SQLDataManipulation(connection, schema);
	}
	
	@Override
	/**
	 * schreibt das Objekt o in die Datenbank.
	 * @param Object o Das Objekt das Serialisiert werden soll
	 */
  public void write(Dataset o) {
	  //1. prüfen ob Objekt bereits eingetragen ist
	  //2. wenn ja: Objekt anpassen
	  //3. wenn nein: Objekt eintragen
	  //4. schon fertig :)
	  
	  //1. prüfen ob Objekt bereits eingetragen ist ...
	  // ... dazu zunächst prüfen ob die ID des objektes > 0 ist. Wenn die id == 0 ist das objekt neu
	  // und noch nicht in der DB eingetragen
	  if (o.getId() > 0)
	    //2. wenn ja: Objekt anpassen
	    createDataManipulation().update(o);
	  else
	    // 3. wenn nein: Objekt eintragen
	    createDataManipulation().insert(o);
	  
	  //4. schon fertig :)
  }
	
	
	
  @Override
  public Dataset read(Class<?> c, long id) {
    if (connect())
    {
      LinkedList<Dataset> results = createQuery().get(c).addCondition("\"dataset\".dataset_id=" + id).execute();
      if (results == null || results.isEmpty())
        return null;
      else
        return results.getFirst();
    }
    return null;
    
  }
  
	
}
