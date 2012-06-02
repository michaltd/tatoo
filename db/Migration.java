package tatoo.db;


public abstract class Migration {
	
	DBConnection dbConn = DBFactory.getInstance().getConnection();
	
	protected DataDefinition createTableWithID(String name, String... columns){
		
		DataDefinition dd = createTable(name, columns);
		dd.addPrimaryKey(name + "_id:INTEGER");
		dd.create();
		return dd;

	}
	
	protected DataDefinition createTable(String name, String... columns){
		
		DataDefinition dd = dbConn.createDataDefinition();
		dd.setTableName(name);
		dd.addColumns(columns);
		return dd;

	}
	
	
	
	protected void dropTable(String name){
		
		DataDefinition dd = dbConn.createDataDefinition();
		dd.setTableName(name);
		dd.drop();
		

	}
	
	public void migration_down(){
		down();
		DataManipulation dm = dbConn.createDataManipulation();
		dm.setTableName("migration_schema");
		dm.setCondition("version = " + getVersion());
		dm.delete();
		
	}
	
	public void migration_up() {
		up();
		DataManipulation dm = dbConn.createDataManipulation();
		dm.setTableName("migration_schema");
		dm.addValue("version = " + getVersion());
		dm.insert();
	}
	
	public abstract void up();
	public abstract void down();
	public abstract long getVersion();

}
