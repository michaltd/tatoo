package tatoo.view;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import tatoo.model.AbstractEntityModel;
import tatoo.model.ArmyListEntityModel;
import tatoo.model.EntityModelListener;

/**
 * Interface for Panels for a node of {@link Armylist}. 
 * In most cases you don't want to use this Interface directly, 
 * use {@link AbstractNodePanel} instead. 
 * @author mkortz
 *
 */
public interface NodePanel extends EntityModelListener {
	
	/**
   * Specifies that components should be laid out left to right.
   */
  public static final int HORIZONTAL_ALIGNMENT = BoxLayout.X_AXIS;

  /**
   * Specifies that components should be laid out top to bottom.
   */
  public static final int VERTICAL_ALIGNMENT = BoxLayout.Y_AXIS;

	
	/**
	 * create the main Panel with all its Components. 
	 * @param parentPanel the parent Panel where to lay down the created Panel. 
	 * Attention: the parent Panel may be changed in the NodePanel.
	 * @param hasChilds
	 */
	public void createNodePanel(JPanel parentPanel, boolean hasChilds);
	
	/**
	 * return the Panel on which the Components of this Panel are laid-out
	 * @return The Panel.
	 */
	public JPanel getChildNodePanel();
	
	/** return if childs of this Panel should be shown. */
	public boolean showChilds();
	
	/** set if childs of this Panel should be shown. */
	public void showChilds(boolean showChilds);
	
	public void setAlignment(int align);

	/**
	 * configure wether empty Entitys should be shown or not.
	 * @param show show empty Entitys?
	 */
	public void showEmptyEntitys(boolean show);
	
	public AbstractEntityModel getModel();

	public void setModel(ArmyListEntityModel model);

}
