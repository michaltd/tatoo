package tatoo.db.sql;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

import org.h2.jdbc.JdbcSQLException;

import tatoo.VersionNumber;
import tatoo.db.DBConnection;
import tatoo.db.DataDefinition;
import tatoo.db.DataManipulation;
import tatoo.db.Migration;
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
		if (database != null)
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
		return new SQLQuery(connection, new DBSchema(dbase.getPathToSchema()));
	}
	
	@Override
	public DataDefinition createDataDefinition() {
		return new SQLDataDefinition(connection, new DBSchema(dbase.getPathToSchema()));
	}

	@Override
	public DataManipulation createDataManipulation() {
		return new SQLDataManipulation(connection, new DBSchema(dbase.getPathToSchema()));
	}
	
	/**
	 * migrate all pending Migrations
	 * equivalent to migrate(getMigrationList().getLast().getVersion());
	 */
	public void migrate(){		
		migrate(getMigrationList().getLast().getVersion());
	}
	
	public void migrate(long toVersion){
		//Migrationen holen
		LinkedList<Migration> migrations = getMigrationList();	
		
		//Datenbankversion holen
		long dbVersion = 0;
		try {
			//sollte die Tabelle VersionNumber noch nicht existieren, muss mit dbVersion 0 gestartet werden
			LinkedList<Object> sqlResults = createQuery().get(VersionNumber.class).orderBy("version").execute();
			if (sqlResults != null)
				dbVersion = ((VersionNumber)sqlResults.getLast()).longValue();
		} catch (ClassCastException ex){
			ex.printStackTrace();
			return;
		}
		
		if (dbVersion > toVersion){
			while (migrations.getFirst().getVersion() < toVersion){
				migrations.removeFirst();
			}
			while (migrations.getLast().getVersion() > dbVersion){
				migrations.removeLast();
			}
			migrate_down(migrations);
		}
		else if(dbVersion < toVersion){
			while (migrations.getFirst().getVersion() <= dbVersion){
				migrations.removeFirst();
			}
			while (migrations.getLast().getVersion() > toVersion){
				migrations.removeLast();
			}
			migrate_up(migrations);
		}
		
		
	}
	
	/**
	 * migrate up the
	 * @param migrations List of Pending Migrations
	 */
	private void migrate_up(LinkedList<Migration> migrations) {
		for (Migration m: migrations){
			m.migration_up();
		}
	}
	
	private void migrate_down(LinkedList<Migration> migrations) {
		for (int i = migrations.size()-1; i>= 0; i--)
			migrations.get(i).migration_down();
	}	
	
	//TODO Verschieben in eine Sinnvolle Klasse, hier ist das nicht wirklich gut aufgehoben
	/**
	 * Get a List of all Migrations
	 * Searches the Migration-Directory and returns a list of all found Migrations sortet by Version-Number
	 * in ascending order
	 * @return List of Migrations 
	 */
	private LinkedList<Migration> getMigrationList(){
		File migrationDir = new File("./bin/tatoo/db/migrate");
		migrationDir.exists();
		LinkedList<Migration> migrationList = new LinkedList<Migration>();
		//TODO muss noch auf list(FileNameFilter f ) umgestellt werden.
		for (File f: migrationDir.listFiles()){
			String fileName = f.getName();
			fileName = "tatoo.db.migrate." + fileName.substring(0, fileName.length()-6);
			Migration migration = null;
			try {
				migration = (Migration)Class.forName(fileName).newInstance();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if (migration != null){
				int idx = 0;
				for (; idx < migrationList.size() && 
							 migrationList.get(idx).getVersion() < migration.getVersion(); 
							 idx++){ }
				migrationList.add(idx, migration);
			}
		}
		return migrationList;
	}
	
}
