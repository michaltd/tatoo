package tatoo.view;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import tatoo.model.entities.EntityModel;
import tatoo.model.entities.EntityModelListener;

/**
 * general implementation for all node panels.
 * This abstract class make some Methods and Attributes available, which makes
 * the implementation of NodePanels easyer. 
 * It consider of EmptyEntities, the model of an Entity and so on.
 * @author mkortz
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractNodePanel extends JPanel implements NodePanel, EntityModelListener {

	/** the model of this NodePanel */
	protected EntityModel model;
	/** show empty nodes? */
	protected boolean showEmptyEntities = true;
	/** alignment of panel */
	private int alignment;
	/** show childs */
	protected boolean showChilds = true;
	
	/**
	 * Panel for the Content of this Node Panel
	 */
	protected JPanel contentPanel = new JPanel();
	
	/**
	 * Constructor to initialize this NodePanel without a node.
	 * you have to care about setting the node later. 
	 * Use {@link AbstractNodePanel(Object node)} to initialize with a node.
	 */
	protected AbstractNodePanel() {
	} 
	
	/**
	 * Constructor to initialize the model of the NodePanel.
	 * @param node the node of this Panel
	 */
	public AbstractNodePanel(Object node){
		setNode(node);
	}
	
	/**
	 * configure wether empty Entitys should be shown or not.
	 * @param show show empty Entitys?
	 */
	public void showEmptyEntitys(boolean show){
		showEmptyEntities = show;
	}
	
	/**
	 * this method is for intern use only. Do not override it or call it. Use
	 * method {@link create(JPanel parentPanel, boolean hasChilds)} instead.
	 */
	@Override
	public void createNodePanel(JPanel parentPanel, boolean hasChilds) {
		if (showEmptyEntities || hasChilds){
			parentPanel.setLayout(new BoxLayout(parentPanel, alignment));
			create(parentPanel, contentPanel, hasChilds);
		}
	}
	
	/**
	 * set the node of this Panel
	 * @param node the Node 
	 */
	private void setNode(Object node){
		model = new EntityModel(node);
	}
	
	public EntityModel getModel() {
		return model;
	}

	public void setModel(EntityModel model) {
		this.model = model;
	}
	
	public boolean showChilds() {
		return showChilds;
	}

	public void showChilds(boolean showChilds) {
		this.showChilds = showChilds;
	}
	
	@Override
	public JPanel getChildNodePanel() {
		return contentPanel;
	}
	
	public void setAlignment(int align){
		contentPanel.setLayout(new BoxLayout(contentPanel, align));
		alignment = align;
	}
	
	/**
	 * Here you can create and design your Panel
	 * @param parentPanel the Panel on which the Components are laid-out
	 * @param hasChilds information about wether the node has Childs or not
	 */
	public abstract void create(JPanel parentPanel, JPanel contentPanel ,boolean hasChilds);

}
