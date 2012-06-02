package tatoo.model.entities;

import java.util.EventListener;


public interface EntityModelListener extends EventListener {
  
  public void SourceChanged();

  public void AttribChanged(EntityModelEvent e);

}
