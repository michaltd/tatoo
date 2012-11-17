package tatoo.db.migrate;

import tatoo.VersionNumber;
import tatoo.db.Migration;

public class InitialAddOfAllTables extends Migration {

    @Override
    public void up () {

        createTable ("abstract_entity", "entity_type:INTEGER", "name:VARCHAR(45)", "parent:INTEGER");
        createTable ("simple_number", "number:INTEGER");
        createTable ("calculated_number", "source:Integer", "value:Integer", "arith:VARCHAR");
        createTable ("true_false_condition", "source:Integer", "value:Integer", "arith:VARCHAR");
        createTable ("abstract_number_condition", "owner_node:INTEGER");
        createTable ("entity_type", "name:VARCHAR(20)", "ordinal:INTEGER", "ROOT:INTEGER", "CATEGORY:INTEGER",
                        "NODE:INTEGER", "ANYOFUPGRADE:INTEGER", "ONEOFUPGRADE:INTEGER", "UPGRADE:INTEGER");

    }

    @Override
    public void down () {

        dropTable ("abstract_entity");
        dropTable ("simple_number");
        dropTable ("calculated_number");
        dropTable ("true_false_condition");
        dropTable ("abstract_number_condition");
        dropTable ("entity_type");

    }

    @Override
    public VersionNumber getVersion () {
        return new VersionNumber (3);
    }

}
