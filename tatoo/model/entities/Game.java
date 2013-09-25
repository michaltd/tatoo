package tatoo.model.entities;

import tatoo.db.DBFactory;
import tatoo.db.Dataset;
import tatoo.model.entities.AbstractEntity.EntityType;

public class Game extends Dataset{

    String name;
    int    rootId;

    // für die Serialisierung!
    public Game(){}
    
    public Game (String name) {
        this.name = name;
        AbstractEntity rootNode = new ArmyListEntity(EntityType.ARMYLIST, name);
        if (DBFactory.getInstance ().write (rootNode))
            rootId = rootNode.getId ();
        DBFactory.getInstance ().write (this);
    }

    public String getName () {
        return name;
    }
    
    public Dataset getGameEntity(){
        return DBFactory.getInstance ().read(ArmyListEntity.class, rootId);
    }

}
