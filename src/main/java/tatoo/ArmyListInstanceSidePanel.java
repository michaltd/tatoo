package tatoo;

import tatoo.db.DBFactory;
import tatoo.model.entities.AbstractEntity;
import tatoo.model.entities.ArmyListEntity;

public class ArmyListInstanceSidePanel
{
    private AbstractEntity armeeliste;

    public ArmyListInstanceSidePanel()
    {
        armeeliste = (AbstractEntity) DBFactory.getInstance().read(ArmyListEntity.class, 3);
    }

    private AbstractEntity anyOfUpgrade()
    {
        return new ArmyListEntity(AbstractEntity.EntityType.ANYOFUPGRADE, "beliebiges aus");
    }

    private AbstractEntity oneOfUpgrade()
    {
        return new ArmyListEntity(AbstractEntity.EntityType.ONEOFUPGRADE, "eines aus");
    }
}
