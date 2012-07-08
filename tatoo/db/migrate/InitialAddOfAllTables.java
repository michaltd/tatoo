package tatoo.db.migrate;

import tatoo.VersionNumber;
import tatoo.db.Migration;

public class InitialAddOfAllTables extends Migration {

	@Override
	public void up() {
		
	  createTableWithID("entity", "parent:INTEGER");
		
	  createTableWithID("abstract_entity", "name:VARCHAR(45)", "price:INTEGER", "count:INTEGER", "entity_type:VARCHAR");
	  
	  createTableWithID("abstract_upgrade","receiving_entity_id:INTEGER");
	  
	  createTableWithID("real_entity", "max_count_id:INTEGER", "min_count_id:INTEGER");
	  
	  createTableWithID("simple_number", "number:INTEGER");
	  
	  createTable("realentity_entities", "real_entity_id:INTEGER","entities_id:INTEGER");
    
    createTable("calculated_number", "source:Integer", "value:Integer", "arith:VARCHAR");
    
    createTable("abstract_number_condition");
    
    createTable("abstract_number_condition_listeners", "abstract_number_condition_id:Integer", "listenerList_id:Integer" );

	}
	
	@Override
	public void down() {

	  dropTable("entity");
    dropTable("abstract_entity");
	  dropTable("abstract_upgrade");
	  dropTable("real_entity");
	  dropTable("simple_number");
	  dropTable("realentity_entities");
    dropTable("calculated_number");
    dropTable("abstract_number_condition");
    dropTable("abstract_number_condition_listeners");
	}

	@Override
	public VersionNumber getVersion() {
		return new VersionNumber(3);
	}

	

}
