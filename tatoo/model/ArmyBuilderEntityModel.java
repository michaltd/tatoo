package tatoo.model;

import tatoo.Tatoo;
import tatoo.commands.CmdSetEntityName;
import tatoo.commands.CmdSetEntityType;
import tatoo.model.conditions.Condition;
import tatoo.model.conditions.Condition.ConditionTypes;
import tatoo.model.conditions.ConditionParser;
import tatoo.model.entities.AbstractEntity;
import tatoo.model.entities.AbstractEntity.EntityType;

public class ArmyBuilderEntityModel extends ArmyListEntityModel {

    private ConditionParser conditionParser;

    /**
     * Setzt das gekapselte Entiy des Models. Löst ein SourceChanged Event aus.
     * 
     * @param o
     * Das ArmyListEntity welches von dem Model gekapselt werden soll.
     */
    public void setSource (Object o) {
        try {
            if (o == null)
                return;
            // ist entity schon belegt? dann muss das model als Listener wieder
            // entfernt werden!
            if (entity != null)
                entity.removeEntityListener (this);
            entity = (AbstractEntity) o;
            if (entity != null)
                entity.addEntityListener (this);
            // Beim ersten zuweisen eines Entities muss der ConditionParser erzeugt werden: 
            if (conditionParser == null) {
                AbstractEntity rootNode = entity;
                while (rootNode.getParent () != null)
                    rootNode = rootNode.getParent ();
                conditionParser = new ConditionParser (rootNode);
            }
            fireSourceChanged ();
        }
        catch (ClassCastException e) {
            System.err.println ("Objekt: \"" + o.toString () + "\" is not an ArmyListEntity!");
        }
    }

    @Override
    public String getCount () {
        return getAttrib (ConditionTypes.COUNT);
    }

    public void setCount (String val) {
        if (val.matches ("\\d+"))
            // es handelt sich um einen Integer also direkt an
            // ArmyListEntityModel übergeben, da der ConditionParser eine neue
            // SimpleNumber erzeugen würde.
            super.setCount (Integer.parseInt (val));
        else {
            @SuppressWarnings ("rawtypes")
            Condition condition = conditionParser.createCondition (val);
            super.setCount (condition);
        }
    }

    @Override
    public String getPrice () {
        return getAttrib (ConditionTypes.PRICE);
    }

    public void setPrice (String val) {
        if (val.matches ("\\d+"))
            // es handelt sich um einen Integer also direkt an
            // ArmyListEntityModel übergeben,
            // da der ConditionParser eine neue SimpleNumber erzeugen würde.
            super.setPrice (val);
        else {
            Condition condition = conditionParser.createCondition (val);
            setPrice (condition);
        }
    }

    public void setMinCount (String val) {
        if (val.matches ("\\d+"))
            // es handelt sich um einen Integer also direkt an
            // ArmyListEntityModel übergeben,
            // da der ConditionParser eine neue SimpleNumber erzeugen würde.
            super.setMinCount (val);
        else {
            Condition condition = conditionParser.createCondition (val);
            setMinCount (condition);
        }
    }

    public void setMaxCount (String val) {
        if (val.matches ("\\d+"))
            // es handelt sich um einen Integer also direkt an
            // ArmyListEntityModel übergeben,
            // da der ConditionParser eine neue SimpleNumber erzeugen würde.
            super.setMaxCount (val);
        else {
            Condition condition = conditionParser.createCondition (val);
            setMaxCount (condition);
        }
    }

    @Override
    public String getMinCount () {
        return getAttrib (ConditionTypes.MIN_COUNT);
    }

    @Override
    public String getMaxCount () {
        return getAttrib (ConditionTypes.MAX_COUNT);
    }

    public void setType (String val) {
        if (val == null)
            return;
        EntityType eType = null;
        if (val.equals (EntityType.ARMYLIST.name ()))
            eType = EntityType.ARMYLIST;
        else if (val.equals (EntityType.CATEGORY.name ()))
            eType = EntityType.CATEGORY;
        else if (val.equals (EntityType.NODE.name ()))
            eType = EntityType.NODE;
        else if (val.equals (EntityType.UPGRADE.name ()))
            eType = EntityType.UPGRADE;
        else if (val.equals (EntityType.ANYOFUPGRADE.name ())) {
            eType = EntityType.ANYOFUPGRADE;
            Tatoo.cmdMgr.execute (new CmdSetEntityName (entity, "beliebiges aus"));
        }
        else if (val.equals (EntityType.ONEOFUPGRADE.name ())) {
            eType = EntityType.ONEOFUPGRADE;
            Tatoo.cmdMgr.execute (new CmdSetEntityName (entity, "eines aus"));
        }
        Tatoo.cmdMgr.execute (new CmdSetEntityType (entity, eType));
    }

    private String getAttrib (ConditionTypes type) {
        if (entity != null) {
            Condition condition = entity.getAttribute (type);
            String conditionString = conditionParser.getConditionString (entity, condition);
            return conditionString;
        }
        return null;
    }

    public EntityType[] getPossibleNodeTypes () {
        if (entity != null) {
            EntityType[] resultTypes;
            if (entity.getParent () != null) {
                EntityType parentType = entity.getParent ().getType ();
                resultTypes = parentType.getChildTypes ();
            }
            else resultTypes = new EntityType[] {EntityType.ARMYLIST};

            return resultTypes;
        }
        return null;
    }
}
