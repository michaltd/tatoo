package tatoo.model.entities.attributesetters;

import tatoo.model.conditions.Condition;
import tatoo.model.entities.EntityModel;

/**
 * Helper Class to set an Attribute of an Entity.
 * Classes which implement this Interface are for setting one specific Attribute of an
 * Entity. This trivial Classes are Used as a Sort of Callback-Functions. 
 * 
 * @author mkortz
 *
 */
public interface AttribSetter {
	
	public void setAttrib(EntityModel em, Condition c);
	

}
