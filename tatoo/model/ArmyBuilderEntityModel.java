package tatoo.model;

import tatoo.model.conditions.Condition;
import tatoo.model.conditions.Condition.ConditionTypes;
import tatoo.model.conditions.ConditionParseException;
import tatoo.model.conditions.ConditionParser;
import tatoo.model.entities.AbstractEntity;

public class ArmyBuilderEntityModel extends ArmyListEntityModel {

    private ConditionParser conditionParser;

    /**
     * Setzt das gekapselte Entiy des Models. Löst ein SourceChanged Event aus.
     * 
     * @param o
     *            Das ArmyListEntity welches von dem Model gekapselt werden
     *            soll.
     */
    public void setSource( Object o ) {
        try {
            entity = (AbstractEntity) o;
            entity.addEntityListener( this );
            AbstractEntity rootNode = entity;
            while ( rootNode.getParent() != null )
                rootNode = rootNode.getParent();

            conditionParser = new ConditionParser( rootNode );
            fireSourceChanged();
        }
        catch ( ClassCastException e ) {
            System.err.println( "Objekt: \"" + o.toString() + "\" is not an ArmyListEntity!" );
        }
    }

    @Override
    public String getCount() {
        return getAttrib( ConditionTypes.COUNT );
    }

    public void setCount( String val ) {
        if ( val.matches( "\\d+" ) )
            // es handelt sich um einen Integer also direkt an
            // ArmyListEntityModel übergeben,
            // da der ConditionParser eine neue SimpleNumber erzeugen würde.
            super.setCount( Integer.parseInt( val ) );
        else {
            @SuppressWarnings( "rawtypes" )
            Condition condition = conditionParser.createCondition( val );
            super.setCount( condition );
        }
    }

    @Override
    public String getPrice() {
        return getAttrib( ConditionTypes.PRICE );
    }

    public void setPrice( String val ) {
        if ( val.matches( "\\d+" ) )
            // es handelt sich um einen Integer also direkt an
            // ArmyListEntityModel übergeben,
            // da der ConditionParser eine neue SimpleNumber erzeugen würde.
            super.setPrice( val );
        else {
            Condition condition = conditionParser.createCondition( val );
            setPrice( condition );
        }
    }

    public void setMinCount( String val ) {
        if ( val.matches( "\\d+" ) )
            // es handelt sich um einen Integer also direkt an
            // ArmyListEntityModel übergeben,
            // da der ConditionParser eine neue SimpleNumber erzeugen würde.
            super.setMinCount( val );
        else {
            Condition condition = conditionParser.createCondition( val );
            setMinCount( condition );
        }
    }

    public void setMaxCount( String val ) {
        if ( val.matches( "\\d+" ) )
            // es handelt sich um einen Integer also direkt an
            // ArmyListEntityModel übergeben,
            // da der ConditionParser eine neue SimpleNumber erzeugen würde.
            super.setMaxCount( val );
        else {
            Condition condition = conditionParser.createCondition( val );
            setMaxCount( condition );
        }
    }

    
    
    @Override
    public String getMinCount() {
        return getAttrib( ConditionTypes.MIN_COUNT );
    }

    @Override
    public String getMaxCount() {
        return getAttrib( ConditionTypes.MAX_COUNT );
    }

    private String getAttrib( ConditionTypes type ) {
        Condition condition = entity.getAttribute( type );
        String conditionString = conditionParser.getConditionString( entity, condition );
        return conditionString;
    }

}
