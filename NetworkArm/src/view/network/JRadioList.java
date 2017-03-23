package view.network;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * 
 * This class is used to create panel with JRadioButton and to apply on them the same listener.
 * @author valbert
 *
 */

@SuppressWarnings("serial")
public class JRadioList extends JPanel{
	private ArrayList<JRadioButton> buttons;
	private ButtonGroup group;
	
	/**
	 * A constructor which creates all buttons from an array list
	 * @param buttons_name The array list of names
	 */
	public JRadioList(ArrayList<String> buttons_name){
		group = new ButtonGroup();
		buttons = new ArrayList<JRadioButton>();
		
		//We create all buttons
		for(int i=0; i<buttons_name.size(); i++){
			buttons.add(new JRadioButton(buttons_name.get(i)));
		}
		
		//We add all buttons
		addToThis();
	}
	
	/**
	 * A constructor which creates all buttons from an array
	 * @param buttons_name The array of names
	 */
	public JRadioList(String[] buttons_name){
		group = new ButtonGroup();
		buttons = new ArrayList<JRadioButton>();
		
		for(int i=0; i<buttons_name.length; i++){
			buttons.add(new JRadioButton(buttons_name[i]));
		}
		
		//We add all buttons
		addToThis();
	}
	
	/**
	 * Add all buttons to the panel and to the same ButtonGroup
	 */
	private void addToThis(){
		removeAll();
		setLayout(new GridLayout(buttons.size(), 1));
		
		for(int i=0; i<buttons.size(); i++){
			add(buttons.get(i));
			group.add(buttons.get(i));
		}	
	}
	
	public void setSelected(int i){
		deselect();
		buttons.get(i).doClick();
	}
	
	public void deselect(){
		ButtonModel b = group.getSelection();
		
		if(b != null)
			b.setSelected(false);
	}
	
	public void addButton(String name, ActionListener listener){
		buttons.add(new JRadioButton(name));
		addToThis();
	}
	
	/**
	 * Method which applies on all buttons the same listener
	 * @param a The ActionListener to apply
	 */
	public void addActionListenerOnAll(ActionListener a){
		
		//In fact, the listener will always set data from the text of the radio button
		for(JRadioButton b : buttons)
			b.addActionListener(a);
		
	}
}
