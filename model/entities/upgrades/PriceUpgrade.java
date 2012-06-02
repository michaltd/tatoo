package tatoo.model.entities.upgrades;

import tatoo.model.conditions.NumberCondition;


public class PriceUpgrade extends Old_Upgrade {

	public PriceUpgrade(String name, int price)
	{
		this.name = name;
		this.price.setValue(price);
	}
	@Override
	public String getName() {
		return name;
	}

	@Override
	public NumberCondition getPrice() {
		return price;
	}
	
	@Override
	public String toString() {
		return new String(
				receivingEntity.toString() + "," + name + "->" + price);
	}


	

}
