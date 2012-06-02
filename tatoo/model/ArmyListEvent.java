package tatoo.model;

import java.util.EventObject;


/**
 * @author mkortz
 * Superclass of all ArmyList Events 
 */
@SuppressWarnings("serial")
public class ArmyListEvent extends EventObject {

	public ArmyListEvent(Object source) {
		super(source);
	}

}
