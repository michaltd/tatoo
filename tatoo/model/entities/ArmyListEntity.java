package tatoo.model.entities;

import tatoo.model.conditions.NumberCondition;

public abstract interface ArmyListEntity {

  public abstract NumberCondition<Integer> getPrice();

  public abstract void setPrice(int price);
  
  public abstract void setPrice(NumberCondition<Integer> price);

  public abstract String getName();

  public abstract void setName(String name);

  public abstract NumberCondition<Integer> getCount();

  public abstract void setCount(int count);
  
  public abstract void setCount(NumberCondition<Integer> count);

  public abstract NumberCondition<Integer> getMinCount();

  public abstract void setMinCount(int minCount);

  public abstract void setMinCount(NumberCondition<Integer> minCount);

  public abstract NumberCondition<Integer> getMaxCount();

  public abstract void setMaxCount(int maxCount);
  
  public abstract void setMaxCount(NumberCondition<Integer> maxCount);

  public abstract void getSpecialRules();

  public abstract void addUpgrade(AbstractUpgrade upgrade);

}
