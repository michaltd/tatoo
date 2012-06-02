package tatoo.model.entities;

import java.util.ArrayList;
import java.util.List;

import tatoo.model.conditions.Condition;
import tatoo.model.conditions.ConditionListener;
import tatoo.model.conditions.NumberCondition;
import tatoo.model.conditions.SimpleNumber;


class RealEntity extends AbstractEntity {

  protected Condition<Integer> maxCount = new SimpleNumber(0);
  protected Condition<Integer> minCount = new SimpleNumber(0);

  private ArrayList<AbstractEntity> entities = new ArrayList<AbstractEntity>();

  @Override
  public Condition<Integer> getMaxCount() {
    return maxCount;
  }  
  
  @Override
  public void setMaxCount(int maxCount) {
    setMaxCount(new SimpleNumber(maxCount));
  }
  
  public void setMaxCount(Condition<Integer> maxCount){
  	this.maxCount = maxCount;
  	if (this.getCount().getValue() > this.getMaxCount().getValue())
     	this.setCount(this.getMaxCount());
  }

  @Override
  public Condition<Integer> getMinCount() {
    return minCount;
  }

  @Override
  public void setMinCount(int minCount) {
    this.minCount.setValue(minCount);
    this.count.setValue(minCount);
  }
  
  @Override
  public void setMinCount(Condition<Integer> minCount) {
    this.minCount.setValue(minCount.getValue());
    this.count.setValue(minCount.getValue());
  }
  
  public RealEntity(String name) {
  	this.init();
    this.name = name;
  }

  public RealEntity(String name, int price) {
  	this.init();
//    new RealEntity(name);
    this.name = name;
    this.price.setValue(price);
  }

  @Override
  public void getSpecialRules() {
  // TODO Auto-generated method stub

  }

  @Override
  public int getTotalPrice() {
    int total = price.getValue();
    for (AbstractEntity ae : entities)
      total += ae.getTotalPrice();
    return total;
  }

  @Override
  public void addUpgrade(AbstractUpgrade upgrade) {
  // empty. wird nicht benötigt. Funktionalität im Entity!
  }// TODO: irgendwie blöd noch mal überarbeiten?

  @Override
  public String toString() {
    String returnString = "";
    for (AbstractEntity ae : entities)
      returnString += ae.toString();
    String priceString = "";
    // if (price > 0)
    // priceString = "->" + price;
    return name + priceString; // + returnString;
  }

  @Override
  public Boolean addEntity(AbstractEntity entity) {
    return entities.add(entity);
  }

  @Override
  public void removeEntity(AbstractEntity entity) {
    int idx = getIndexOf(entity);
    if (idx >= 0) entities.remove(idx);
  }

  @Override
  public AbstractEntity getEntityAt(int index) {
    return entities.get(new Integer(index));
  }

  @Override
  public int getChildCount() {
    return entities.size();
  }

  public int getIndexOf(AbstractEntity e) {
    return entities.indexOf(e);
  }

  @Override
  public boolean isLeaf() {
    return entities.isEmpty();
  }

  @Override
  public List<AbstractEntity> getChilds() {
    return entities;
  }

}