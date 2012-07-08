package tatoo.view.armyBuilder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;

import tatoo.model.EntityModel;
import tatoo.model.EntityModelListener;
import tatoo.model.entities.events.EntityModelEvent;


@SuppressWarnings("serial")
public class ArmyBuilderEditPane extends JPanel implements EntityModelListener {

  private int width = 300;
  EntityModel model = null;

  private JLabel title = new JLabel();
  private JTextField points = new JTextField();
  private JTextField minCount = new JTextField();
  private JTextField maxCount = new JTextField();

  public ArmyBuilderEditPane(EntityModel entityModel) {
    super();
    this.model = entityModel;
    model.addEntityModelListener(this);
    buildPanel();
  }

  public void buildPanel() {

    title.setPreferredSize(new Dimension(width, 30));
    title.setBorder(new LineBorder(Color.black));
    title.setHorizontalAlignment(SwingConstants.CENTER);

    JLabel pointsLabel = new JLabel("Points");
    points.setPreferredSize(new Dimension(50, 20));
    points.getDocument().addDocumentListener(
        new TextFieldActionHandler(points) {

          @Override
          public void setValue(String val) {
            try {
              model.setPrice(new Integer(val));
            } catch (NumberFormatException e) {
              System.err.println("Wert entspricht keiner Zahl!");
            }
          };
        });

    JPanel pointsPanel = new groupPane("Points");
    pointsPanel.add(pointsLabel);
    pointsPanel.add(points);

    JLabel minCountLabel = new JLabel("Min Count");
    minCount.setPreferredSize(new Dimension(50, 20));
    minCount.getDocument().addDocumentListener(
        new TextFieldActionHandler(minCount) {

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
    maxCount.setPreferredSize(new Dimension(50, 20));
    maxCount.getDocument().addDocumentListener(
        new TextFieldActionHandler(maxCount) {

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

  private void showValues() {
    if (model == null) this.setEnabled(false);
    else {
      title.setText(model.getName());
      points.setText(new Integer(model.getPrice()).toString());
      minCount.setText(new Integer(model.getMinCount()).toString());
      maxCount.setText(new Integer(model.getMaxCount()).toString());
    }
  }

  private void clearPanel() {
    this.removeAll();
  }

  public void setWidth(int width) {
    this.width = width;
    clearPanel();
    buildPanel();
  }

  @Override
  public void SourceChanged() {
    showValues();
  }
  
  @Override
  public void AttribChanged(EntityModelEvent e) {
    //we ignore the event-Object here and refresh for simplicity all attributes
    showValues();
  }

  private class groupPane extends JPanel {

    public groupPane(String title) {
      this(title, 60);
    }

    public groupPane(String title, int height) {
      TitledBorder b = new TitledBorder(new LineBorder(Color.lightGray), title);
      b.setTitleColor(Color.lightGray);
      b.setTitleFont(new Font("Arial", Font.ITALIC, 12));
      setBorder(b);
      setHeight(height);
    }

    private void setHeight(int height) {
      setPreferredSize(new Dimension(width, height));
      setMaximumSize(new Dimension(width, height));
      setMinimumSize(new Dimension(width, height));
    }
  }

  private abstract class TextFieldActionHandler implements DocumentListener {

    JTextField field;

    public TextFieldActionHandler(JTextField field) {
      this.field = field;
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
      setValue(field.getText());
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
      setValue(field.getText());
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
      String val = field.getText();
      if (val.length() > 0) setValue(val);
    }

    public abstract void setValue(String value);

  }


}
