package tatoo.view;

import java.util.HashMap;
import java.util.Map;

import tatoo.model.entities.AnyOfUpgradeNode;
import tatoo.model.entities.CategoryNode;
import tatoo.model.entities.EntityModel;
import tatoo.model.entities.NodeNode;
import tatoo.model.entities.OneOfUpgradeNode;
import tatoo.model.entities.RootNode;
import tatoo.model.entities.UpgradeNode;
import tatoo.view.armyList.SimpleAnyOfUpgradePanel;
import tatoo.view.armyList.SimpleArmyListPanel;
import tatoo.view.armyList.SimpleCategoryPanel;
import tatoo.view.armyList.SimpleFiller;
import tatoo.view.armyList.SimpleNodePanel;
import tatoo.view.armyList.SimpleOneOfUpgradePanel;

public class NodePanelFactory {

	private final static int ROOT = 0;
	private final static int CATEGORY = 1;
	private final static int NODE = 2;
	private final static int UPGRADE = 3;
	private final static int ONE_OF_UPGRADE = 4;
	private final static int ANY_OF_UPGRADE = 5;
	
	private Map<Integer, NodePanelEntity> nodePanel;
	
	public NodePanelFactory(String target){
		
		nodePanel = new HashMap<Integer, NodePanelEntity>();
		
		//TODO: diese ganze geschichte muss hinterher von der DB bzw. dem XML kommen
		NodePanelEntity entity = new NodePanelEntity();
		entity.setNodePanel(SimpleArmyListPanel.class);
		if (target == "ArmyList"){
			entity.setAlignment(NodePanel.HORIZONTAL_ALIGNMENT);
			entity.setShowEmptyEntities(false);
		}
		nodePanel.put(ROOT, entity);
		
		entity = new NodePanelEntity();
		entity.setNodePanel(SimpleCategoryPanel.class);
		if (target == "ArmyList"){
			entity.setShowEmptyEntities(false);
		}
		nodePanel.put(CATEGORY, entity);
		
		entity = new NodePanelEntity();
		entity.setNodePanel(SimpleNodePanel.class);
		if (target == "Sidemenu")
			entity.setShowChilds(false);
		nodePanel.put(NODE, entity);
		
		entity = new NodePanelEntity();
		entity.setNodePanel(SimpleFiller.class);
		nodePanel.put(UPGRADE, entity);
		
		entity = new NodePanelEntity();
		entity.setNodePanel(SimpleOneOfUpgradePanel.class);
		nodePanel.put(ONE_OF_UPGRADE, entity);
		
		entity = new NodePanelEntity();
		entity.setNodePanel(SimpleAnyOfUpgradePanel.class);
		nodePanel.put(ANY_OF_UPGRADE, entity);

	}
	
	public NodePanel getNodePanel(Object node){
		
		try {
			return getNodePanel((UpgradeNode)node);
		}catch (ClassCastException e){}
		
		try {
			return getNodePanel((OneOfUpgradeNode)node);
		}catch (ClassCastException e){}
		
		try {
			return getNodePanel((AnyOfUpgradeNode)node);
		}catch (ClassCastException e){}
		
		try {
			return getNodePanel((NodeNode)node);
		}catch (ClassCastException e){}
		
		try {
			return getNodePanel((CategoryNode)node);
		}catch (ClassCastException e){}
		
		try {
			return getNodePanel((RootNode)node);
		}catch (ClassCastException e){}
		
		throw new ClassCastException();
	}
	
	private NodePanel getNodePanel(RootNode node){
		return buildNodePane(ROOT, node);
	}
	
	private NodePanel getNodePanel(CategoryNode node){
		return buildNodePane(CATEGORY, node);
	}
	
	private NodePanel getNodePanel(NodeNode node){
		return buildNodePane(NODE, node);
	}
	
	private NodePanel getNodePanel(UpgradeNode node){
		return buildNodePane(UPGRADE, node);
	}
	
	private NodePanel getNodePanel(OneOfUpgradeNode node){
		return buildNodePane(ONE_OF_UPGRADE, node);
	}
	
	private NodePanel getNodePanel(AnyOfUpgradeNode node){
		return buildNodePane(ANY_OF_UPGRADE, node);
	}
	
	private NodePanel buildNodePane(int category, Object node){
		NodePanel panel = null;
		try {
			NodePanelEntity entity = nodePanel.get(category);
			panel = entity.getNodePanel().newInstance();
			panel.setAlignment(entity.getAlignment());
			panel.showEmptyEntitys(entity.isShowEmptyEntities());	
			panel.showChilds(entity.isShowChilds());
			
			panel.setModel(new EntityModel(node));
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return panel;
	}
	
	private class NodePanelEntity{
		
		public Class<? extends NodePanel> nodePanel;
		public boolean showEmptyEntities = true;
		public boolean showChilds = true;
		public int alignment = NodePanel.VERTICAL_ALIGNMENT;
				
		public boolean isShowChilds() {
			return showChilds;
		}
		public void setShowChilds(boolean showChilds) {
			this.showChilds = showChilds;
		}
		
		public Class<? extends NodePanel> getNodePanel() {
			return nodePanel;
		}
		public void setNodePanel(Class<? extends NodePanel> nodePanel) {
			this.nodePanel = nodePanel;
		}
		public boolean isShowEmptyEntities() {
			return showEmptyEntities;
		}
		public void setShowEmptyEntities(boolean showEmptyEntities) {
			this.showEmptyEntities = showEmptyEntities;
		}
		public int getAlignment() {
			return alignment;
		}
		public void setAlignment(int alignment) {
			this.alignment = alignment;
		}	
	}
	
}
