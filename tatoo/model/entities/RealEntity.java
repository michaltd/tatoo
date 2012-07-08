package tatoo.model.entities;

import java.util.ArrayList;
import java.util.List;

import tatoo.model.conditions.Condition;
import tatoo.model.conditions.ConditionListener;
import tatoo.model.conditions.NumberCondition;
import tatoo.model.conditions.SimpleNumber;


public class RealEntity extends AbstractEntity {

  protected NumberCondition<Integer> maxCount = new SimpleNumber(0);
  protected NumberCondition<Integer> minCount = new SimpleNumber(0);

  private ArrayList<AbstractEntity> entities = new ArrayList<AbstractEntity>();

  public RealEntity()
  {
    this(EntityType.NODE,"");
  }
  
  public RealEntity(EntityType type, String name) {
    this(type, name, 0);
  }

  public RealEntity(EntityType type, String name, int price) {
    super(type);
    this.init();
    setName(name);
    setPrice(price);
  }

  
  @Override
  public NumberCondition<Integer> getMaxCount() {
    return maxCount;
  }  
  
  @Override
  public void setMaxCount(int maxCount) {
    setMaxCount(new SimpleNumber(maxCount));
  }
  
  public void setMaxCount(NumberCondition<Integer> maxCount){
  	this.maxCount = maxCount;
  	if ((Integer)this.getCount().getValue() > (Integer)this.getMaxCount().getValue())
     	this.setCount(this.getMaxCount());
  }

  @Override
  public NumberCondition<Integer> getMinCount() {
    return minCount;
  }

  @Override
  public void setMinCount(int minCount) {
    this.minCount.setValue(minCount);
    this.setCount(minCount);
  }

  @Override
  public void setMinCount(NumberCondition<Integer> minCount) {
    this.minCount.setValue(minCount.getValue());
    if ((Integer)this.getCount().getValue() < (Integer)this.minCount.getValue())
      setCount(minCount);
  }
  
  @Override
  public void getSpecialRules() {
  // TODO Auto-generated method stub

  }

  @Override
  public int getTotalPrice() {
    int total = (Integer)getPrice().getValue();
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
    return getName() + priceString; // + returnString;
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