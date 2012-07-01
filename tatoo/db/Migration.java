package tatoo.db;

import tatoo.VersionNumber;


public abstract class Migration implements Comparable<Migration>{
	
  DBConnection dbConn = DBFactory.getInstance().getConnection();
	
  protected void createTableWithID(String name, String... columns){
		
		DataDefinition dd = priv_createTable(name, columns);
		dd.addPrimaryKey(name + "_id:INTEGER");
		dd.create();

	}
	
	protected void createTable(String name, String... columns){
		priv_createTable(name, columns).create();

	}
	
	private DataDefinition priv_createTable(String name, String... columns )
	{
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
//		DataManipulation dm = dbConn.createDataManipulation();
		VersionNumber version = new VersionNumber(getVersion());
//		dm.setTableName("migration_schema");
//		dm.alterValue("version = " + getVersion());
//		dm.insert(version);
		DBFactory.getInstance().write(version);
	}
	
	@Override
  public int compareTo(Migration m) {
    return (int)(getVersion() - m.getVersion());
  }
	
	public abstract void up();
	public abstract void down();
	public abstract int getVersion();

}
