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

/**
 * Implementierung der DBConnection für die Verbindung mit der SQL-Datenbank H2.
 * @see tatoo.db.DBConnection
 * @author mkortz
 *
 */
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

	@Override
	public boolean connect()
	{
		if (database != null && connection == null)
		{
			try {
				connection = DriverManager.getConnection(
						dbase.getConnectionString(), dbase.getUSER(), dbase.getPASSWD());
			} catch (SQLException e) {
			  System.err.println("Kein Zugriff auf die Datenbank.");
				e.printStackTrace();
			}
		}

		return connection != null;
	}

	@Override
	public boolean close()
	{
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


}
