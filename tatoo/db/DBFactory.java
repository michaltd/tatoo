package tatoo.db;

import tatoo.db.DBConnection.DBases;
import tatoo.db.sql.SQLConnection;

public class DBFactory {
	
	private DBConnection conn;
	private static DBFactory factory= null;
	
	/**
	 * create and initialize a new DBFactory
	 */
	private void init() {
		//Connection aufbauen
		//TODO hier muss per Reflection der "Treiber" geladen werden (SQLConnection, XMLConnection oder MySQLConnection usw.)
		conn = new SQLConnection(DBases.H2Server);
		//conn = new SQLConnection(DBases.H2);
		conn.connect();
	}
	
	public DBConnection getConnection(){
		return conn;
	}
	
	public static DBFactory getInstance(){
		if (factory == null){
			factory = new DBFactory();
			factory.init();
		}
		return factory;
	}
	
//	/**
//	 * migrate all pending Migrations
//	 * equivalent to migrate(getMigrationList().getLast().getVersion());
//	 */
//	public void migrate(){		
//		migrate(getMigrationList().getLast().getVersion());
//	}
//	
//	public void migrate(long toVersion){
//		//Migrationen holen
//		LinkedList<Migration> migrations = getMigrationList();	
//		
//		//Datenbankversion holen
//		long dbVersion = 0;
//		ResultSet rs;
//		try{
//		  	rs = conn.executeQuery("SELECT max(version) from \"migration_schema\";");
//		  	rs.first();
//		  	dbVersion = rs.getLong("max(version)");
//		}catch (SQLException e){
//			//das muss funktionieren! wenn nicht dann muss migriert werden! in diesem Fall keine Fehlermeldung ausgeben?
////			e.printStackTrace();
//		}
//		catch (Exception e){
//			e.printStackTrace();
//		}
//		
//		if (dbVersion > toVersion){
//			while (migrations.getFirst().getVersion() < toVersion){
//				migrations.removeFirst();
//			}
//			while (migrations.getLast().getVersion() > dbVersion){
//				migrations.removeLast();
//			}
//			migrate_down(migrations);
//		}
//		else if(dbVersion < toVersion){
//			while (migrations.getFirst().getVersion() <= dbVersion){
//				migrations.removeFirst();
//			}
//			while (migrations.getLast().getVersion() > toVersion){
//				migrations.removeLast();
//			}
//			migrate_up(migrations);
//		}
//		
//		
//	}
//	
//	/**
//	 * migrate up the
//	 * @param migrations List of Pending Migrations
//	 */
//	private void migrate_up(LinkedList<Migration> migrations) {
//		for (Migration m: migrations){
//			m.migration_up();
//		}
//	}
//	
//	private void migrate_down(LinkedList<Migration> migrations) {
//		for (int i = migrations.size()-1; i>= 0; i--)
//			migrations.get(i).migration_down();
//	}	
//	
//	/**
//	 * Get a List of all Migrations
//	 * Searches the Migration-Directory and returns a list of all found Migrations sortet by Version-Number
//	 * in ascending order
//	 * @return List of Migrations 
//	 */
//	private LinkedList<Migration> getMigrationList(){
//		File migrationDir = new File("./bin/tatoo/db/migrate");
//		migrationDir.exists();
//		LinkedList<Migration> migrationList = new LinkedList<Migration>();
//		//TODO muss noch auf list(FileNameFilter f ) umgestellt werden.
//		for (File f: migrationDir.listFiles()){
//			String fileName = f.getName();
//			fileName = "tatoo.db.migrate." + fileName.substring(0, fileName.length()-6);
//			Migration migration = null;
//			try {
//				migration = (Migration)Class.forName(fileName).newInstance();
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			} catch (InstantiationException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}
//			if (migration != null){
//				int idx = 0;
//				for (; idx < migrationList.size() && 
//							 migrationList.get(idx).getVersion() < migration.getVersion(); 
//							 idx++){ }
//				migrationList.add(idx, migration);
//			}
//		}
//		return migrationList;
//	}
	
	

}
