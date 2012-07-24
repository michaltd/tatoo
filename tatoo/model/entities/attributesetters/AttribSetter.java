package tatoo.model.entities.attributesetters;

import tatoo.model.EntityModel;
import tatoo.model.conditions.Condition;

/**
 * Helper Class to set an Attribute of an ArmyListEntity.
 * Classes which implement this Interface are for setting one specific Attribute of an
 * ArmyListEntity. This trivial Classes are Used as a Sort of Callback-Functions. 
 * 
 * @author mkortz
 *
 */
public interface AttribSetter {
	
	public void setAttrib(EntityModel em, Condition c);
	

}
