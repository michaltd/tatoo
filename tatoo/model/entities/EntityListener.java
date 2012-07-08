package tatoo.model.entities;

import java.util.EventListener;

public interface EntityListener extends EventListener {
	
  
	public void AttribChanged(String attrib);

}
