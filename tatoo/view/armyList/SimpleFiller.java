package tatoo.view.armyList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import tatoo.model.EntityModel;
import tatoo.model.entities.events.EntityModelEvent;
import tatoo.view.AbstractNodePanel;

@SuppressWarnings("serial")
public class SimpleFiller extends AbstractNodePanel {

	final JTextField valueField = new JTextField();
	JLabel priceLabel = new JLabel();
	
	@Override
	public void create(JPanel parentPanel, JPanel contentPanel ,boolean hasChilds) {
		
		model.addEntityModelListener(this);
		
		JPanel borderForPanel = new JPanel();
		borderForPanel.setLayout(new BoxLayout(borderForPanel, BoxLayout.Y_AXIS));
		borderForPanel.setAlignmentX(0);
		parentPanel.setLayout(new BorderLayout());
		
		contentPanel.setBorder(new EmptyBorder(0, 10, 0, 0));		
		
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BorderLayout());
		
		JLabel nameLabel = new JLabel();
		nameLabel.setText(model.getName());
		
		JPanel valuePanel = new JPanel();
		valuePanel.setLayout(new BorderLayout());
		
		int width = 23;
		
		final EntityModel simpleFillerModel = model;
		
		JButton decrement = new JButton("-"){ 
				@Override
				public synchronized void addMouseListener(MouseListener l) {
					super.addMouseListener(new MouseListener(){
						@Override
						public void mouseClicked(MouseEvent e) {
							simpleFillerModel.setCount(simpleFillerModel.getCount()-1);
						}
						@Override	public void mouseEntered(MouseEvent e) {}
						@Override	public void mouseExited(MouseEvent e) {}
						@Override	public void mousePressed(MouseEvent e) {}
						@Override	public void mouseReleased(MouseEvent e) {}						
					});
				}
			};
			
		JPanel decrementPanel = new JPanel();
		decrementPanel.setLayout(new BorderLayout());
		decrementPanel.add(Box.createHorizontalStrut(width), BorderLayout.NORTH);
		decrementPanel.add(decrement, BorderLayout.CENTER);
		valuePanel.add(decrementPanel, BorderLayout.WEST);
		
		JPanel valueFieldPanel = new JPanel();
		valueFieldPanel.setLayout(new BorderLayout());
		valueFieldPanel.add(Box.createHorizontalStrut(50), BorderLayout.NORTH);
		
		valueField.setHorizontalAlignment(JTextField.CENTER);
		
		valueFieldPanel.add(valueField, BorderLayout.CENTER);
		valuePanel.add(valueFieldPanel, BorderLayout.CENTER );
		
		JButton increment = new JButton("+"){ 
			@Override
			public synchronized void addMouseListener(MouseListener l) {
				super.addMouseListener(new MouseListener(){
					@Override
					public void mouseClicked(MouseEvent e) {
						simpleFillerModel.setCount(simpleFillerModel.getCount() + 1);
					}
					@Override	public void mouseEntered(MouseEvent e) {}
					@Override	public void mouseExited(MouseEvent e) {}
					@Override	public void mousePressed(MouseEvent e) {}
					@Override	public void mouseReleased(MouseEvent e) {}						
				});
			}
		};
		JPanel incrementPanel = new JPanel();
		incrementPanel.setLayout(new BorderLayout());
		incrementPanel.add(Box.createHorizontalStrut(width), BorderLayout.NORTH);
		incrementPanel.add(increment, BorderLayout.CENTER);
		valuePanel.add(incrementPanel, BorderLayout.EAST);
		
		JPanel valueContainerPane = new JPanel();
		valueContainerPane.add(valuePanel);
		priceLabel.setText(((Integer)model.getPrice()).toString());
		priceLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		JPanel pricePanel = new JPanel();
		pricePanel.setLayout(new BorderLayout());
		pricePanel.add(Box.createHorizontalStrut(21), BorderLayout.NORTH);
		pricePanel.add(priceLabel, BorderLayout.CENTER);
		valueContainerPane.add(pricePanel);
		
		titlePanel.add(nameLabel, BorderLayout.WEST);
		titlePanel.add(valueContainerPane, BorderLayout.EAST);
		
		borderForPanel.add(titlePanel);
		borderForPanel.add(contentPanel);
		
		parentPanel.add(borderForPanel);
		
		setCount();
		
	}
	
	private void setCount(){
		valueField.setText( model.getCount() + "/" + model.getMaxCount());
		priceLabel.setText(((Integer)model.getPrice()).toString());
	}

	@Override
	public void AttribChanged(EntityModelEvent e) {
		setCount();
	}

	@Override
	public void SourceChanged() {	}

}
