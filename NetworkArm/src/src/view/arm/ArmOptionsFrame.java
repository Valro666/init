package src.view.arm;


import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;

import src.model.options.ArmOptions;

@SuppressWarnings("serial")
public class ArmOptionsFrame extends JFrame{
	private JFormattedTextField field_max_time;
	private JFormattedTextField field_move_time;
	
	public ArmOptionsFrame(){
		super();
		
		setTitle("Arm options");
		
		setMinimumSize(new Dimension(300, 100));
		
		field_max_time = new JFormattedTextField(new Float(ArmOptions.max_time));
		field_move_time = new JFormattedTextField(new Float(ArmOptions.move_time));
		
		
		field_max_time.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArmOptions.max_time = ((Float)field_max_time.getValue()).doubleValue();
			}
		});
		
		field_move_time.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArmOptions.move_time = ((Float)field_move_time.getValue()).doubleValue();
			}
		});
		
		
		setLayout(new GridLayout(0, 2));
		
		add(new JLabel("Max time : "));
		add(field_max_time);
		
		add(new JLabel("Move time : "));
		add(field_move_time);
		
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		pack();
	}
}
