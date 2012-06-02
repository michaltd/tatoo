package tatoo.model.entities.attributesetters;

import tatoo.model.conditions.Condition;
import tatoo.model.entities.EntityModel;

public class PriceSetter implements AttribSetter {

	/**
	 * set the price of an Entity
	 */
	@Override
	public void setAttrib(EntityModel em, Condition c) {
		em.setPrice(c);
	}

}
