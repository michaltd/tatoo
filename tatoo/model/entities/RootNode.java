package tatoo.model.entities;

/**
 * RootNode? 
 * hmmm ich weiss nicht mehr wofür ich das gemacht habe. Zumal der eh nichts macht ausser hübsch 
 * auszusehen. Ich glaube das hing mit irgendwelchen Basteleien im View zusammen. Muss ich dann
 * beizeiten mal nachvollziehen.
 * @author mkortz
 *
 */
public class RootNode extends Entity {

	public RootNode(String name) {
		super(name);
	}
	
	/**
	 * Initialisiert den RootNode mit einem Leeren namen. Nicht verwenden! Dieser Konstruktor ist nur für
	 * den gebrauch mit der Serialisierung gedacht!
	 */
	public RootNode(){this("");};

}
