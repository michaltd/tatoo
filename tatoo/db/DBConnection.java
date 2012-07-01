package tatoo.db;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;

import tatoo.VersionNumber;
import tatoo.db.sql.DBSchema;

public abstract class DBConnection {
	
  //TODO: Dieses Enum muss noch umgebaut werden, 
  //so dass die Treiber auch per String aus einer config datei geladen werden können
	public enum DBases {
		
		H2Server("org.h2.Driver","jdbc:h2:tcp://localhost/~/workspace/tatoo/db/tatoo", "tatoo/db/sql/db_schema.xml"),
//		H2Server("org.h2.Driver","jdbc:h2:./db/tatoo"),
		H2("org.h2.Driver", "jdbc:h2:./db/tatoo", "db/sql/db_schema.xml"),
		XML("tatoo.model.xmlConnector", "", "");

		private final String driverString;
		private final String connectionString;
		private final String pathToSchemaFile;
		private final String USER = "tatoo";
		private final String PASSWD = "";
		
		private DBases(String driverString, String connectionString, String pathToSchemaFile) {
			this.driverString = driverString;
			this.connectionString = connectionString;
			this.pathToSchemaFile = pathToSchemaFile;
		}
		
		public String getDriverString() {return driverString;}
		public String getConnectionString() {return connectionString;}
		public String getUSER() {return USER;}
		public String getPASSWD() {return PASSWD;}
		public String getPathToSchema(){return this.pathToSchemaFile;}
	}

  protected DBSchema schema;
  protected DBases dbase;

	public abstract boolean connect();

	public abstract boolean close();

	protected abstract Query createQuery();
	
	protected abstract DataManipulation createDataManipulation();
	
	protected abstract DataDefinition createDataDefinition();

  public abstract void write(Dataset o);

  public abstract Dataset read(Class<?> c, long id);

  /**
   * migrate all pending Migrations
   * equivalent to migrate(getMigrationList().getLast().getVersion());
   */
  public void migrate() {		
  	migrate(getMigrationList().getLast().getVersion());
  }

  public void migrate(long toVersion) {
  		//Migrationen holen
  		LinkedList<Migration> migrations = getMigrationList();	
  		
  		//Datenbankversion holen
  		long dbVersion = 0;
  		try {
  			//sollte die Tabelle VersionNumber noch nicht existieren, muss mit dbVersion 0 gestartet werden
  			LinkedList<Dataset> sqlResults = createQuery().get(VersionNumber.class).orderBy("version").execute();
  			if (sqlResults != null)
  				dbVersion = ((VersionNumber)sqlResults.getLast()).intValue();
  //			  dbVersion = 0;
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

  /**
   * Get a List of all Migrations
   * Searches the Migration-Directory and returns a list of all found Migrations sortet by Version-Number
   * in ascending order
   * @return List of Migrations 
   */
  private LinkedList<Migration> getMigrationList() {
  	File migrationDir = new File("./tatoo/db/migrate");
  	migrationDir.exists();
  	LinkedList<Migration> migrationList = new LinkedList<Migration>();
  	//TODO muss noch auf list(FileNameFilter f ) umgestellt werden.
  	for (File f: migrationDir.listFiles()){
  		String fileName = f.getName();
  		if (!fileName.endsWith(".java"))
  		  continue;
  		fileName = "tatoo.db.migrate." + fileName.substring(0, fileName.length()-5);
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
  		  migrationList.add(migration);
  		}
  	}
  	Collections.sort(migrationList);
  	return migrationList;
  }

}