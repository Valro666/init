package view.network;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.LinkedEnvironment;
import model.network.Data;
import model.network.Neuron;


@SuppressWarnings("serial")
public class NetworkDraw extends JPanel{
	
	private int nb_draw;
	
    private LinkedEnvironment world ;
    private int echelle;
    
    public NetworkDraw(LinkedEnvironment e, int size) {
    	
    	super();
		world = e;
		this.echelle = size;
		this.setPreferredSize(new Dimension(size, size));
		nb_draw = 0;
		
    }
    
    public LinkedEnvironment getWorld(){
    	return world;
    }
    
    public void saveImage(String path, String type){
    	
    	BufferedImage image = new BufferedImage(600,600, BufferedImage.TYPE_INT_RGB);
		Graphics g2 = image.createGraphics();
    	paint(g2);
		
		try{
    		ImageIO.write(image, type, new File(path+(nb_draw++)+"."+type));
    	} catch (Exception e) { 
    		e.printStackTrace(); 
    	}
    		
		nb_draw++;
    }
    
    public int getNbDraw() {
		return nb_draw;
	}

	public void setNbDraw(int nb_draw) {
		this.nb_draw = nb_draw;
	}

	@Override
    public void paintComponent(Graphics g) {
    	
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        Neuron neur;

        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
	
        g2.setPaint(Color.lightGray);
        
        Data data_to_print = world.getData(0);
        
        for(int i=0;i<data_to_print.size();i+=2){
        	Ellipse2D.Double d = new Ellipse2D.Double(data_to_print.get(i).getWeights().get(0)*echelle,
        	data_to_print.get(i).getWeights().get(1)*echelle,.5,.5);	  
	    	g2.draw(d);
        }      

        if(world.getNet() != null && world.getNet().size() != 0){
	        for(int i=0;i<world.getNet().getNeurons().size();i++){
			    for(int k=0;k<world.getNet().getNeurons().get(i).size();k++){
			    	
					g2.setPaint(Color.red);
					neur = world.getNet().getNeurons().get(i).get(k);	
										
					g2.fillOval ((int)(neur.getWeights().get(0)*echelle), (int)(neur.getWeights().get(1)*echelle),5,5) ;
			
					g2.setPaint(Color.BLUE);
			
					for(int j=0;j<neur.getNeighbors().size();j++){
					    Line2D.Double l = 
						new Line2D.Double(new Point2D.Double(neur.getWeights().get(0)*echelle, neur.getWeights().get(1)*echelle),
								  new Point2D.Double(neur.getNeighbors().get(j).getWeights().get(0)*echelle,
										     neur.getNeighbors().get(j).getWeights().get(1)*echelle)
								  );
					    g2.draw(l);
					}
			    }
	        }
        }

        revalidate();
    }
}
