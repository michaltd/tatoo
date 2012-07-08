package tatoo.model.entities;

import java.util.List;


/**
 * @author mkortz
 * Ein Proxy auf ein RealEntity oder ein AbstractUpgrade.
 */
public class Entity extends AbstractUpgrade {

	protected AbstractEntity parent = null;

	
	public Entity(){super();};
	
	public Entity(EntityType type, String name)
	{
	  super(type);
		this.init();
		this.receivingEntity = new RealEntity(type, name);
	}
	
	public AbstractEntity getParent() {
		return parent;
	}

	public void setParent(AbstractEntity parent) {
		this.parent = parent;
	}
	
	@Override
	public Boolean addEntity(AbstractEntity entity) {
		((Entity)entity).setParent(this);
		return super.addEntity(entity);
	}
	
	@Override
	public String toString() {
		return "[" + receivingEntity.toString() + "]";
	}

	public List<AbstractEntity> getChilds() {
		return receivingEntity.getChilds();
	}

}