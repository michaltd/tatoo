package tatoo.db.migrate;

import tatoo.db.Migration;

public class CreateTableMigrationSchema extends Migration {

	@Override
	public void down() {
		//drop table nicht ausführen, da migration_schema immer nötig ist!
		//für testzwecke dennoch aktiviert
		dropTable("migration_schema");
	}

	@Override
	public void up() {
		createTable("migration_schema", "version:Number").create();
		
	}

	@Override
	public long getVersion() {
		return 1;
	}

}
