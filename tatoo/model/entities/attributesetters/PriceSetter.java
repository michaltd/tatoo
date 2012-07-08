package tatoo.model.entities.attributesetters;

import tatoo.model.EntityModel;
import tatoo.model.conditions.Condition;
import tatoo.model.conditions.NumberCondition;

public class PriceSetter implements AttribSetter {

	/**
	 * Setzt den Preis den Entitys. Wenn es sich bei dem Parameter nicht um eine {@linkplain tatoo.model.conditions.NumberCondition<Integer>} handelt wird eine
	 * ClassCastException erzeugt und geworfen.
	 */
	@Override
	public void setAttrib(EntityModel em, Condition c) {
	  em.setPrice((NumberCondition<Integer>)c);
	}

}
