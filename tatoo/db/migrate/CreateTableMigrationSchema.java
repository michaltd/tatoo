package tatoo.db.migrate;

import tatoo.db.Migration;

public class CreateTableMigrationSchema extends Migration {

	@Override
	public void down() {
		dropTable("migration_schema");
		dropTable("dataset");
	}

	@Override
	public void up() {
	  
	  // bevor überhaupt etwas angelegt werden kann muss die Tabelle Dataset vorhanden sein
	  createTableWithID("dataset", "type:Varchar");
	  
		createTableWithID("migration_schema", "version:Integer");
		
	}

	@Override
	public int getVersion() {
		return 1;
	}

}
