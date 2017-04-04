package src.view.arm;

import info.monitorenter.gui.chart.*;
import info.monitorenter.gui.chart.IAxis.AxisTitle;
import info.monitorenter.gui.chart.traces.Trace2DSimple;

import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import src.view.View;
import src.view.network.JSpinSlider;
import src.model.LinkedEnvironment;
import src.model.network.Neuron;
import src.model.options.GraphOptions;

@SuppressWarnings("serial")
public class GraphViewer extends JPanel implements Observer{
	
	private Chart2D chart;
	private Trace2DSimple tracer_errors;
	private Trace2DSimple tracer_errors_approx;
	private JSpinSlider slider_nb_neurons;
	private JLabel nb_neurons_shower;
	
	private View holder;
	
	public GraphViewer(View v, Dimension dim){
		
		super();
		
		holder=v;
		
		setPreferredSize(dim);
		
		nb_neurons_shower = new JLabel("Nb neurons selected : "+GraphOptions.nb_neurons_approx);
		
		tracer_errors = new Trace2DSimple("Errors");
		tracer_errors.setColor(Color.ORANGE);
		
		tracer_errors_approx = new Trace2DSimple("Errors with approximation");
		tracer_errors_approx.setColor(Color.MAGENTA);
		
		chart = new Chart2D();
		chart.addTrace(tracer_errors);
		chart.addTrace(tracer_errors_approx);
		
		chart.getAxisX().setAxisTitle(new AxisTitle("Epochs"));
		chart.getAxisY().setAxisTitle(new AxisTitle("Error"));
		
		chart.setPreferredSize(dim);
		
		slider_nb_neurons = new JSpinSlider(dim.width, 20 , 0.001, 1.0, 0.5, 0.001);
		
		slider_nb_neurons.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				ArrayList<ArrayList<Neuron>> neurons = holder.getWorld().getNet().getNeurons();
				int nb_neurons = neurons.size() * neurons.get(0).size();
				double percentage = slider_nb_neurons.getValue();
				GraphOptions.nb_neurons_approx = ((int)(percentage * nb_neurons) > 0) ? (int)(percentage * nb_neurons) : 1;
				nb_neurons_shower.setText("Nb neurons selected : "+GraphOptions.nb_neurons_approx);
			}
		});
		
		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());
		container.add(chart, BorderLayout.NORTH);
		container.add(slider_nb_neurons, BorderLayout.CENTER);
		container.add(nb_neurons_shower, BorderLayout.SOUTH);
		
		add(container);
	}
	
	public void clear(){
		tracer_errors.removeAllPoints();
		tracer_errors_approx.removeAllPoints();
	}

	@Override
	public void update(Observable o, Object arg) {
		
		LinkedEnvironment env = (LinkedEnvironment) o;
		
		int elapsed_examples = env.getExamples();
		
		double error = env.getError();
		tracer_errors.addPoint(elapsed_examples, error);
		
		double error_approx = env.getErrorApprox();
		tracer_errors_approx.addPoint(elapsed_examples, error_approx);
		
		ArrayList<ArrayList<Neuron>> neurons = holder.getWorld().getNet().getNeurons();
		int nb_neurons = neurons.size() * neurons.get(0).size();
		double percentage = slider_nb_neurons.getValue();
		GraphOptions.nb_neurons_approx = ((int)(percentage * nb_neurons) > 0) ? (int)(percentage * nb_neurons) : 1;
		
		nb_neurons_shower.setText("Nb neurons selected : "+GraphOptions.nb_neurons_approx);
	}
	
}
