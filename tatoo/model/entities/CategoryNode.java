package tatoo.model.entities;

public class CategoryNode extends Entity{

  public CategoryNode(){ this("");}
  
	public CategoryNode(String name) {
		super(AbstractEntity.EntityType.CATEGORY, name);
	}

}
