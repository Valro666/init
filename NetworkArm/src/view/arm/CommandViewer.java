package view.arm;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.table.*;

import model.arm.CompleteArm;

/**
 * 
 * A class to show commands that have to be executed and actual commands.
 * @author valbert
 *
 */
@SuppressWarnings("serial")
public class CommandViewer extends JPanel implements Observer{
	
	private JTable commands_table;
	private ArrayList<String> to_do;
	private ArrayList<String> done;
	private AbstractTableModel dataModel;
	JScrollPane scrollpane;
	
	public CommandViewer(Dimension dim){
		
		to_do = new ArrayList<String>();
		done = new ArrayList<String>();
		
		dataModel = new AbstractTableModel() {
			  
	          public int getColumnCount() { 
	        	  return 2; 
	          }
	          
	          public int getRowCount() { 
	        	  return to_do.size();
	          }
	          
	          @Override
	          public Object getValueAt(int row, int col) {
	        	  if(col==0)
	        		  return to_do.get(row);
	        	  else
	        		 return done.get(row);
	          }
	          
	          public boolean isCellEditable(int iRowIndex, int iColumnIndex)
	          {
	              return false;
	          }
	          
	          @Override
	          public String getColumnName(int col){
	        	  switch(col){
	        	  case 0:
	        		  return "To do";
	        	  case 1:
	        		  return "Done";
	        	  default:
	        		  return "Unknown column";
	        	  }
	          }
	      };
	      
	      commands_table = new JTable(dataModel);
	      
	      scrollpane = new JScrollPane(commands_table);
	      
	      add(scrollpane);
	      
	      setPreferredSize(dim);
	      scrollpane.setPreferredSize(dim);
	}
	
	public void add(String t_d, String d){
		to_do.add(t_d);
		done.add(d);
		
		dataModel.fireTableRowsInserted(to_do.size()-1, to_do.size()-1);
		scrollpane.getVerticalScrollBar().setValue(scrollpane.getVerticalScrollBar().getMaximum());
	}
	
	public void clear(){
		
		int size = to_do.size()-1;
		
		to_do.clear();
		done.clear();
		
		if(size>0)
			dataModel.fireTableRowsDeleted(0, size);
	}

	@Override
	public void update(Observable o, Object arg) {
		
		//CompleteArm arm = (CompleteArm) o;
		
		
		// String last_planned_comm = arm.getLastComm();
		//
		// if(last_planned_comm.equals("RESET")){
		// add("RESET", "RESET");
		// }else{
		// String last_executed_comm = arm.getActivationString();
		// add(last_planned_comm, last_executed_comm);
		// }

	}
}
