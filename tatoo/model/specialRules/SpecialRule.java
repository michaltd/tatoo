package tatoo.model.specialRules;

import tatoo.model.entities.AbstractEntity;


public interface SpecialRule {
  
  void addReceiver(AbstractEntity entity);
  
  void apply();

}
