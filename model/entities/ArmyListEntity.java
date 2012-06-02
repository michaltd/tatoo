package tatoo.model.entities;

import tatoo.model.conditions.Condition;

public abstract interface ArmyListEntity {

  public abstract Condition<Integer> getPrice();

  public abstract void setPrice(int price);
  
  public abstract void setPrice(Condition<Integer> price);

  public abstract String getName();

  public abstract void setName(String name);

  public abstract Condition<Integer> getCount();

  public abstract void setCount(int count);
  
  public abstract void setCount(Condition<Integer> count);

  public abstract Condition<Integer> getMinCount();

  public abstract void setMinCount(int minCount);
  
  public abstract void setMinCount(Condition<Integer> minCount);

  public abstract Condition<Integer> getMaxCount();

  public abstract void setMaxCount(int maxCount);
  
  public abstract void setMaxCount(Condition<Integer> maxCount);

  public abstract void getSpecialRules();

  public abstract void addUpgrade(AbstractUpgrade upgrade);

}
