package tatoo.db;

import java.util.LinkedList;

public abstract class Query extends DataHandler {
	
	public abstract Query get(Class<?> cl);
	
	public abstract Query addCondition(String condition);
	
	public abstract Query orderBy(String order);
	
	public abstract Query groupBy(String group);
	
	public abstract LinkedList<Object> execute();

}
