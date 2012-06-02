package tatoo.view.armyBuilder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import tatoo.model.entities.EntityModel;
import tatoo.model.entities.attributesetters.PriceSetter;

/**
 * Panel to edit Entities.
 * @author mkortz
 */
@SuppressWarnings("serial")
public class EntityEditPane extends ArmyListEditPanel implements ActionListener {

  private EntityEditPane thisPane = this;
  private JLabel title;
  private JTextField points;
	private JTextField minCount;
  private JTextField maxCount;
  Popup popup;
  ConditionBuilder condBuilder;

  public EntityEditPane(EntityModel entityModel) {
    super(entityModel);
    showValues();
  }

  /**
   * arrange Components on Criteria builder.
   */
  @Override
  public void buildPanel() {

    title = new JLabel();
    title.setPreferredSize(new Dimension(width, 35));
    title.setBorder(new LineBorder(Color.black));
    title.setHorizontalAlignment(SwingConstants.CENTER);

    JLabel pointsLabel = new JLabel("Points");
    points = new JTextField();
    points.setPreferredSize(new Dimension(50, 25));
    points.getDocument().addDocumentListener(
        new TextFieldChangeHandler(points) {
          @Override
          public void setValue(String val) {
            try {
              model.setPrice(new Integer(val));
            } catch (NumberFormatException e) {
              System.err.println("Wert entspricht keiner Zahl!");
            }
          };
        });

    final JButton pointsButton = new JButton();
    pointsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
      	condBuilder = new ConditionBuilder(new PriceSetter(), model);
      	condBuilder.addActionListener(thisPane);
        PopupFactory factory = PopupFactory.getSharedInstance();
        if (popup != null)
        	popup.hide();
        popup = factory.getPopup(
            thisPane, condBuilder, pointsButton.getLocationOnScreen().x -150, pointsButton.getLocationOnScreen().y + 20);
        popup.show();
      }
    });
    pointsButton.setPreferredSize(new Dimension(20, 20));

    JPanel pointsPanel = new groupPane("Points");
    pointsPanel.add(pointsLabel);
    pointsPanel.add(points);
    pointsPanel.add(pointsButton);

    JLabel minCountLabel = new JLabel("Min Count");
    minCount = new JTextField();
    minCount.setPreferredSize(new Dimension(50, 25));
    minCount.getDocument().addDocumentListener(
        new TextFieldChangeHandler(minCount) {
          @Override
          public void setValue(String val) {
            try {
              model.setMinCount(new Integer(val));
            } catch (NumberFormatException e) {
              System.err.println("Wert entspricht keiner Zahl!");
            }
          };
        });
    JLabel maxCountLabel = new JLabel("Max Count");
    maxCount = new JTextField();
    maxCount.setPreferredSize(new Dimension(50, 25));
    maxCount.getDocument().addDocumentListener(
        new TextFieldChangeHandler(maxCount) {

          @Override
          public void setValue(String val) {
            try {
              model.setMaxCount(new Integer(val));
            } catch (NumberFormatException e) {
              System.err.println("Wert entspricht keiner Zahl!");
            }
          };
        });
    JPanel countPanel = new groupPane("Count");
    countPanel.add(minCountLabel);
    countPanel.add(minCount);
    countPanel.add(maxCountLabel);
    countPanel.add(maxCount);

    JPanel editPane = new JPanel();
    LayoutManager editPaneLayout = new BoxLayout(editPane, BoxLayout.Y_AXIS);
    editPane.setLayout(editPaneLayout);
    editPane.add(pointsPanel);
    editPane.add(countPanel);
    editPane.add(Box.createVerticalGlue());

    setLayout(new BorderLayout());
    setBackground(Color.white);
    add(title, BorderLayout.NORTH);
    add(editPane, BorderLayout.CENTER);

  }

  /**
   * get Values from model and update EntityEditPanel.
   */
  @Override
  void showValues() {
    if (model == null) this.setEnabled(false);
    else {
      title.setText(model.getName());
      points.setText(new Integer(model.getPrice().getValue()).toString());
      minCount.setText(new Integer(model.getMinCount().getValue()).toString());
      maxCount.setText(new Integer(model.getMaxCount().getValue()).toString());
    }
  }

  /**
   * Hides the performing popup and refreshes the Values in EditPanel.
   * EntitiyEditPanel listens on Popup Windows. If an Action is performed
   * from them this Method is called.
   */
	@Override
	public void actionPerformed(ActionEvent e) {
		popup.hide();
		showValues();
	}

}
