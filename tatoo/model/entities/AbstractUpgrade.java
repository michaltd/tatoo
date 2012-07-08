package tatoo.model.entities;

import java.util.List;

import tatoo.model.conditions.Condition;
import tatoo.model.conditions.NumberCondition;


public abstract class AbstractUpgrade extends AbstractEntity {

  public AbstractUpgrade(EntityType type) { super(type); }

  protected AbstractEntity receivingEntity;

  public String getName() {
    return receivingEntity.getName();
  }

  public void setName(String name) {
    receivingEntity.setName(name);
  }

  public NumberCondition<Integer> getPrice() {
    return receivingEntity.getPrice();
  }

  public int getTotalPrice() {
    return receivingEntity.getTotalPrice();
  }

  public void setPrice(int price) {
    receivingEntity.setPrice(price);
  }
  
  public void setPrice(NumberCondition<Integer> price) {
    receivingEntity.setPrice(price);
  }

  @Override
  public NumberCondition<Integer> getMinCount() {
    return receivingEntity.getMinCount();
  }

  @Override
  public void setMinCount(int minCount) {
    receivingEntity.setMinCount(minCount);
  }

  @Override
  public void setMinCount(NumberCondition<Integer> minCount) {
    receivingEntity.setMinCount(minCount);
  }
  
  @Override
  public NumberCondition<Integer> getMaxCount() {
    return receivingEntity.getMaxCount();
  }

  @Override
  public void setMaxCount(int maxCount) {
    receivingEntity.setMaxCount(maxCount);
  }

  @Override
  public void setMaxCount(NumberCondition<Integer> maxCount) {
    receivingEntity.setMaxCount(maxCount);
  }
  
  @Override
  public AbstractEntity getEntityAt(int index) {
    return receivingEntity.getEntityAt(index);
  }

  @Override
  public final void getSpecialRules() {
    receivingEntity.getSpecialRules();
  }

  @Override
  public Boolean addEntity(AbstractEntity entity) {
    return receivingEntity.addEntity(entity);
  }

  public void removeEntity(AbstractEntity entity) {
    receivingEntity.removeEntity(entity);
  }

  @Override
  public void addUpgrade(AbstractUpgrade upgrade) {
    upgrade.receivingEntity = this.receivingEntity;
    this.receivingEntity = upgrade;
  }

  public int getChildCount() {
    return receivingEntity.getChildCount();
  }

  @Override
  public int getIndexOf(AbstractEntity e) {
    return receivingEntity.getIndexOf(e);
  }

  public boolean isLeaf() {
    return receivingEntity.isLeaf();
  }

  @Override
  public List<AbstractEntity> getChilds() {
    return receivingEntity.getChilds();
  }

}