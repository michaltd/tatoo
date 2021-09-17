package tatoo.view;

import lombok.Getter;
import lombok.Setter;
import tatoo.model.ArmyListEntityModel;
import tatoo.model.entities.AbstractEntity;
import tatoo.view.armyList.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NodePanelFactory
{
    private final Map<Integer, NodePanelEntity> nodePanel;

    public NodePanelFactory(String target)
    {
        nodePanel = new HashMap<>();

        // TODO: diese ganze geschichte muss hinterher von der DB bzw. dem XML
        // kommen
        NodePanelEntity entity = new NodePanelEntity();
        entity.setNodePanel(SimpleArmyListPanel.class);
        if (Objects.equals(target, "ArmyList")) {
            entity.setAlignment(NodePanel.HORIZONTAL_ALIGNMENT);
            entity.setShowEmptyEntities(false);
        }
        nodePanel.put(AbstractEntity.EntityType.ARMYLIST.ordinal(), entity);

        entity = new NodePanelEntity();
        entity.setNodePanel(SimpleCategoryPanel.class);
        if (Objects.equals(target, "ArmyList")) {
            entity.setShowEmptyEntities(false);
        }
        nodePanel.put(AbstractEntity.EntityType.CATEGORY.ordinal(), entity);

        entity = new NodePanelEntity();
        entity.setNodePanel(SimpleNodePanel.class);
        if (Objects.equals(target, "Sidemenu"))
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

    public NodePanel getNodePanel(Object node)
    {
        // return buildNodePane(new ArmyListEntityModel(node).getNodeType(), node);
        return buildNodePane(new ArmyListEntityModel(node).getSourceType().ordinal(), node);
    }

    private NodePanel buildNodePane(int category, Object node)
    {
        NodePanel panel = null;
        try {
            NodePanelEntity entity = nodePanel.get(category);
            panel = entity.getNodePanel().newInstance();
            panel.setAlignment(entity.getAlignment());
            panel.showEmptyEntitys(entity.isShowEmptyEntities());
            panel.showChilds(entity.isShowChilds());
            panel.setModel(new ArmyListEntityModel(node));
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return panel;
    }

    @Getter
    @Setter
    private static class NodePanelEntity
    {
        public Class<? extends NodePanel> nodePanel;
        public boolean showEmptyEntities = true;
        public boolean showChilds = true;
        public int alignment = NodePanel.VERTICAL_ALIGNMENT;
    }
}
