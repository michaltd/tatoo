package tatoo.view;

import java.util.HashMap;
import java.util.Map;

import tatoo.model.ArmyListEntityModel;
import tatoo.model.entities.AbstractEntity;
import tatoo.model.entities.ArmyListEntity;
import tatoo.view.armyList.SimpleAnyOfUpgradePanel;
import tatoo.view.armyList.SimpleArmyListPanel;
import tatoo.view.armyList.SimpleCategoryPanel;
import tatoo.view.armyList.SimpleFiller;
import tatoo.view.armyList.SimpleNodePanel;
import tatoo.view.armyList.SimpleOneOfUpgradePanel;

public class NodePanelFactory {
	
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
		nodePanel.put(AbstractEntity.EntityType.ROOT.ordinal(), entity);
		
		entity = new NodePanelEntity();
		entity.setNodePanel(SimpleCategoryPanel.class);
		if (target == "ArmyList"){
			entity.setShowEmptyEntities(false);
		}
		nodePanel.put(AbstractEntity.EntityType.CATEGORY.ordinal(), entity);
		
		entity = new NodePanelEntity();
		entity.setNodePanel(SimpleNodePanel.class);
		if (target == "Sidemenu")
			entity.setShowChilds(false);
		nodePanel.put(AbstractEntity.EntityType.NODE.ordinal(), entity);
		
		entity = new NodePanelEntity();
		entity.setNodePanel(SimpleFiller.class);
		nodePanel.put(AbstractEntity.EntityType.UPGRADE.ordinal(), entity);
		
		entity = new NodePanelEntity();
		entity.setNodePanel(SimpleOneOfUpgradePanel.class);
		nodePanel.put(AbstractEntity.EntityType.ONEOFUPGRADE.ordinal(), entity);
		
		entity = new NodePanelEntity();
		entity.setNodePanel(SimpleAnyOfUpgradePanel.class);
		nodePanel.put(AbstractEntity.EntityType.ANYOFUPGRADE.ordinal(), entity);

	}
	
	public NodePanel getNodePanel(Object node){
		return buildNodePane(new ArmyListEntityModel(node).getNodeType(), node);
	}
	
	private NodePanel buildNodePane(int category, Object node){
		NodePanel panel = null;
		try {
			NodePanelEntity entity = nodePanel.get(category);
			panel = entity.getNodePanel().newInstance();
			panel.setAlignment(entity.getAlignment());
			panel.showEmptyEntitys(entity.isShowEmptyEntities());	
			panel.showChilds(entity.isShowChilds());
			
			panel.setModel(new ArmyListEntityModel(node));
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
