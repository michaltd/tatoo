package tatoo.db.migrate;

import tatoo.db.DataDefinition;
import tatoo.db.Migration;

public class InitialAddOfAllTables extends Migration {

	@Override
	public void up() {
		
		createTableWithID("panel",
								"name:VARCHAR(45)");
		
		createTableWithID("game",
								"name:VARCHAR(45)");

		createTableWithID("entity_type",
								"name:VARCHAR(45)");
		
		createTableWithID("condition", 
								"value:VARCHAR(255)");
		
		DataDefinition fraction = createTableWithID("fraction",
								"game_id:INTEGER",
								"name:VARCHAR(45)");
		fraction.addForeignKey("game_id", "game", "id");
		
		DataDefinition armylist = createTableWithID("armylist",
								"fraction_id:INTEGER",
								"name:VARCHAR(45)",
								"is_pattern:TINYINT(1)");
		armylist.addForeignKey("fraction_id","fraction", "id");
		
		
		DataDefinition panelOptions = createTableWithID("panel_options",
								"game_id:INTEGER",
							  "entity_type_id:INTEGER",
							  "fraction_id:INTEGER",
							  "armylist_id:INTEGER",
							  "panel_id:INTEGER",
							  "alignment:VARCHAR(45)");
		panelOptions.addForeignKey("game_id","game", "id");
		panelOptions.addForeignKey("entity_type_id","entity_type", "id");
		panelOptions.addForeignKey("fraction_id","fraction", "id");
		panelOptions.addForeignKey("armylist_id","armylist", "id");
		panelOptions.addForeignKey("panel_id","panel", "id");
		
		DataDefinition entity = createTableWithID("entity", 
								"entity_type_id:INTEGER", 
								"armylist_id:INTEGER",
							  "count:INTEGER",
							  "maxcount:INTEGER",
							  "mincount:INTEGER",
							  "price:INTEGER",
							  "name:VARCHAR(45)",
							  "parent:INTEGER");
//		entity.nullPermitted("entity_type_id", "INTEGER", false);
//		entity.nullPermitted("armylist_id", "INTEGER", false);
		entity.addForeignKey("armylist_id","armylist", "id");
		entity.addForeignKey("entity_type_id","entity_type", "id");
		entity.addForeignKey("count","condition", "id");
		entity.addForeignKey("maxcount","condition", "id");
		entity.addForeignKey("mincount","condition", "id");
		entity.addForeignKey("price","condition", "id");
		entity.addForeignKey("parent","entity", "id");

	}
	
	@Override
	public void down() {

		dropTable("entity");
		dropTable("panel_options");
		dropTable("armylist");
		dropTable("fraction");
		dropTable("condition");
		dropTable("entity_type");
		dropTable("game");
		dropTable("panel");

	}

	@Override
	public long getVersion() {
		return 2;
	}

	

}
