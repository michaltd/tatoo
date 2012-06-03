package tatoo.db;

import java.io.Serializable;

import tatoo.db.sql.DBSchema;
import tatoo.model.entities.Entity;

public abstract class DBConnection {
	
  //TODO: Dieses Enum muss noch umgebaut werden, 
  //so dass die Treiber auch per String aus einer config datei geladen werden können
	public enum DBases {
		
		H2Server("org.h2.Driver","jdbc:h2:tcp://localhost/~/workspace/tatoo/db/tatoo", "db/sql/db_schema.xml"),
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

	public abstract Query createQuery();
	
	public abstract DataManipulation createDataManipulation();
	
	public abstract DataDefinition createDataDefinition();
	
	public abstract void migrate();
	
	public abstract void migrate(long toVersion);

  public void save(Serializable entity) {
 // Den Datensatz zu diesem Objekt suchen
    Query query = createQuery();
    query.get(entity.getClass());
//    query.addCondition("Entity_id = " + entity.);
//    
//    // wird der Datensatz nicht gefunden muss das Objekt in die Datenbank
//    // eingetragen werden
//    DataManipulation datset = conn.createDataManipulation();
//    if (query.execute().size() == 0) {
//      int new_id = datset.insert(this);
//      if (new_id > 0 ){
//        this.id = new_id;
//        return true;
//      }
//      return false;
//    }
//    // ansonsten muss es verändert werden.
//    else {
//      return datset.update(this) > 0;
//    }
    
  }

}