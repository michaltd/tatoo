package tatoo.model.entities;

import tatoo.model.conditions.Condition;


public interface ArmyListEntityModel {

  String getName();
  Condition<Integer> getPrice();
  int getTotalPrice();
  Condition<Integer> getCount();
  Condition<Integer> getMinCount();
  Condition<Integer> getMaxCount();
  int getUpgradeCount();
  int getUpgrade(int index);
  void setSource(Object o);
  public Object getSource();
}
