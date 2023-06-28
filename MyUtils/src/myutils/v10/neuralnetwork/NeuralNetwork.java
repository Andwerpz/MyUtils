package myutils.v10.neuralnetwork;

import java.util.ArrayList;

public class NeuralNetwork {
	
	public float learningRate = 0.01f;
	
	public ArrayList<NeuralNetworkLayer> layers;

	public NeuralNetwork() {
		this.layers = new ArrayList<>();
	}

	public void addLayer(NeuralNetworkLayer layer) {
		this.layers.add(layer);
	}

	public float[] forwardPropogate(float[] input) {
		if(this.layers.size() == 0) {
			return null;
		}
		
		this.layers.get(0).forwardPropogate(input);
		for(int i = 1; i < this.layers.size(); i++) {
			this.layers.get(i).forwardPropogate(this.layers.get(i - 1));
		}
		
		FCLayer fl = (FCLayer) this.layers.get(this.layers.size() - 1);
		
		
		return fl.outputNodes;
	}

	
	//does backprop and nudges the weights
	public void backPropogate(float[] input, float[] key) {
		if(this.layers.size() == 0) {
			return;
		}
		
		//run forward prop
		float[] guess = this.forwardPropogate(input);
		
		FCLayer fl = ((FCLayer) this.layers.get(this.layers.size() - 1));
		
		//run backprop
		fl.backPropogate(key);
		
		for(int i = this.layers.size() - 2; i >= 0; i--) {
			this.layers.get(i).backPropogate(this.layers.get(i + 1));
		}
	}

}
