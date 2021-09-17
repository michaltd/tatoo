package tatoo.view.armyList;

import tatoo.db.DBFactory;
import tatoo.db.Dataset;
import tatoo.model.ArmyListModel;
import tatoo.model.entities.AbstractEntity;
import tatoo.model.entities.ArmyListEntity;
import tatoo.model.entities.Game;
import tatoo.view.AbstractNodePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Comparator;

@SuppressWarnings("serial")
public class ArmyListPanel extends JPanel
{
    ArmyListModel sideMenuModel;
    ArmyListModel armyListModel;

    public ArmyListModel getModel()
    {
        return sideMenuModel;
    }

    public ArmyListPanel()
    {
        this.setLayout(new BorderLayout());

        sideMenuModel = new ArmyListModel();

        Dataset[] games = DBFactory.getInstance().read(Game.class);
        ArrayList<String> gameNames = new ArrayList<String>(games.length);
        for (Dataset g : games)
            gameNames.add(((Game) g).getName());
        gameNames.sort(new StringIgnoreCaseComparator());
        JComboBox gamesList = new JComboBox(gameNames.toArray());
        gamesList.setEditable(true);

        JPanel sideMenu = new JPanel();
        sideMenu.setLayout(new BorderLayout());

        ArmyTreePanel armyPanel = new ArmyTreePanel(sideMenuModel, "Sidemenu");
        armyPanel.addMouseListener(new SideMenuListener());

        armyListModel = new ArmyListModel(new ArmyListEntity(AbstractEntity.EntityType.ARMYLIST, "Armeeliste"));
        ArmyTreePanel armyList = new ArmyTreePanel(armyListModel, "ArmyList");

        sideMenu.add(gamesList, BorderLayout.NORTH);
        sideMenu.add(armyPanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(armyList);
        scrollPane.setBorder(null);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sideMenu, scrollPane);
        splitPane.setDividerLocation(215);

        this.add(splitPane);

        gamesList.addActionListener(e -> {
            JComboBox cb = (JComboBox) e.getSource();
            String val = (String) cb.getSelectedItem();
            Dataset[] games1 = DBFactory.getInstance().read(Game.class);
            for (Dataset g : games1) {
                if (((Game) g).getName().equalsIgnoreCase(val)) {
                    AbstractEntity rootEntity = (AbstractEntity) ((Game) g).getGameEntity();
                    sideMenuModel.setRoot(rootEntity);
                }
            }
        });
    }

    private class SideMenuListener implements MouseListener
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            // das NodePanel das das Event ausgelöst hat aus dem event holen.
            AbstractNodePanel sourcePanel = (AbstractNodePanel) e.getSource();
            // Das NodePanel hat ein Model und daraus das ArmyListEntity holen
            Object entity = sourcePanel.getModel().getSource();
            // aus dem sideMenuModel den Pfad für dieses ArmyListEntity
            // bestimmen...
            Object[] treePath = sideMenuModel.getTreePathTo(entity);
            // ... und an das armyListModel anhängen.
            armyListModel.insertCopyOf(treePath);
        }

        @Override
        public void mouseEntered(MouseEvent e)
        {
        }

        @Override
        public void mouseExited(MouseEvent e)
        {
        }

        @Override
        public void mousePressed(MouseEvent e)
        {
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
        }
    }

    private static class StringIgnoreCaseComparator implements Comparator<String>
    {
        @Override
        public int compare(String o1, String o2)
        {
            return o1.compareToIgnoreCase(o2);
        }
    }
}
