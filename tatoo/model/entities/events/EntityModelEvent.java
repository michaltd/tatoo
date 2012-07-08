package tatoo.model.entities.events;

import java.util.EventObject;


@SuppressWarnings("serial")
public class EntityModelEvent extends EventObject {
  
  String attrib = "";
  
  public EntityModelEvent(Object source, String attrib) {
    super(source);
    this.attrib = attrib;
  }
  
  public String getAttribChanged(){
  	return attrib;
  }

}
