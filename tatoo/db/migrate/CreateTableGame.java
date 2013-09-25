package tatoo.db.migrate;

import tatoo.VersionNumber;
import tatoo.db.Migration;


public class CreateTableGame extends Migration {

    @Override
    public void up () {
        createTable ("game", "name:VARCHAR(100)", "root_id:INTEGER");
    }

    @Override
    public void down () {
        dropTable ("game");

    }

    @Override
    public VersionNumber getVersion () {
        return new VersionNumber ("00.4812.00");
    }

}
