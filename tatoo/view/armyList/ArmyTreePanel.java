package tatoo.view.armyList;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import tatoo.model.ArmyListModel;
import tatoo.view.AbstractNodePanel;
import tatoo.view.NodePanelFactory;

@SuppressWarnings("serial")
public class ArmyTreePanel extends JPanel implements MouseListener{

	ArmyListModelHandler modelHandler = new ArmyListModelHandler();
	ArmyListModel model = null;
	JPanel treePanel;
	ArmyTreePanel thisPane;
	
	public ArmyTreePanel(ArmyListModel model, String target ) {

		thisPane=this;
		
		treePanel = new JPanel();
		
		treePanel.setLayout(new BorderLayout());

		this.model = model;
		model.addTreeModelListener(modelHandler);

		buildPanel(this.model.getRoot(), treePanel, new NodePanelFactory(target));
		
		thisPane.add(treePanel);
	}
	
	/**
	 * build the Panel for the node.
	 * build the Panels for all the childs recursively.
	 * @param node the node to build the Panel for.
	 * @param panel Parentpanel where the Components of Node are laid down.
	 */
	public void buildPanel(Object node, JPanel panel, NodePanelFactory nodeFactory) {	
		AbstractNodePanel nodePane = (AbstractNodePanel)nodeFactory.getNodePanel(node);
		nodePane.addMouseListener(this);
		panel.add(nodePane);
		nodePane.createNodePanel(nodePane, model.getChildCount(node) > 0);
		JPanel childPanel = nodePane.getChildNodePanel();
		Object childNode;
		for (int i = 0; i < model.getChildCount(node) && nodePane.showChilds(); i++) {
			childNode = model.getChild(node, i);
			buildPanel(childNode, childPanel, nodeFactory);
		}
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {processMouseEvent(e);}
	@Override
	public void mouseEntered(MouseEvent e) {processMouseEvent(e);}
	@Override
	public void mouseExited(MouseEvent e) {processMouseEvent(e);}
	@Override
	public void mousePressed(MouseEvent e) {processMouseEvent(e);}
	@Override
	public void mouseReleased(MouseEvent e) {processMouseEvent(e);}
	

	private class ArmyListModelHandler implements TreeModelListener {

		@Override
		public void treeNodesChanged(TreeModelEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void treeNodesInserted(TreeModelEvent e) {
			JPanel pane = new JPanel();
			buildPanel(model.getRoot(), pane, new NodePanelFactory("ArmyList"));
			thisPane.removeAll();
			thisPane.add(pane);
			thisPane.revalidate();
		}

		@Override
		public void treeNodesRemoved(TreeModelEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void treeStructureChanged(TreeModelEvent e) {
			// TODO Auto-generated method stub

		}

	}

}
