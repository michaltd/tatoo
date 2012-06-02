package tatoo.db;

public abstract class DataManipulation extends DataHandler{
	
	public abstract DataManipulation setTableName(String string);

	public abstract DataManipulation setCondition(String string);

	public abstract DataManipulation addValue(String string);
	
	public abstract DataManipulation addValues(String... columnValues);

	public abstract int insert();
	
	public abstract int insert(Dataset dataset);
	
	public abstract int delete();
	
	public abstract int delete(Dataset dataset);
	
	public abstract int update();
	
	public abstract int update(Dataset dataset);

  

}
