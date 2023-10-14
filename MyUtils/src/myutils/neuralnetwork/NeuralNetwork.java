package myutils.neuralnetwork;

import java.util.ArrayList;

public class NeuralNetwork {

	//typically used in the output layer of classification neural networks
	public static final int ACTIVATION_TYPE_SIGMOID = 0;

	//used in hidden layers
	public static final int ACTIVATION_TYPE_RELU = 1;

	//typically used in the output layer of regression neural networks. 
	public static final int ACTIVATION_TYPE_LINEAR = 2;
	
	//used in hidden layers
	public static final int ACTIVATION_TYPE_TANH = 3;

	public float learningRate = 0.01f;

	public ArrayList<NeuralNetworkLayer> layers;

	public NeuralNetwork() {
		this.init();
	}

	private void init() {
		this.layers = new ArrayList<>();
	}

	public void addLayer(NeuralNetworkLayer layer) {
		this.layers.add(layer);
	}

	public boolean isNetworkValid() {
		if (this.layers.size() <= 2) {
			System.err.println("NeuralNetwork : Must have at least two layers");
			return false;
		}
		if (!(this.layers.get(0) instanceof InputLayer)) {
			System.err.println("NeuralNetwork : First layer must be InputLayer");
			return false;
		}
		if (!(this.layers.get(this.layers.size() - 1) instanceof OutputLayer)) {
			System.err.println("NeuralNetwork : Last layer must be OutputLayer");
			return false;
		}
		return true;
	}

	//returns the activations for the output layer
	public float[] forwardPropogate(float[] input) {
		if (!this.isNetworkValid()) {
			return null;
		}

		InputLayer inputLayer = (InputLayer) this.layers.get(0);
		inputLayer.setActivation(input);
		for (int i = 1; i < this.layers.size(); i++) {
			((HiddenLayer) this.layers.get(i)).forwardPropogate(this.layers.get(i - 1));
		}

		OutputLayer outputLayer = (OutputLayer) this.layers.get(this.layers.size() - 1);

		return outputLayer.outputNodes;
	}

	//does backprop and nudges the weights
	//returns the cost of the neural network on the given input
	public float backPropogate(float[] input, float[] key) {
		if (!this.isNetworkValid()) {
			return 0;
		}

		//run forward prop
		float[] guess = this.forwardPropogate(input);

		//run backprop
		OutputLayer outputLayer = (OutputLayer) this.layers.get(this.layers.size() - 1);
		float cost = outputLayer.backPropogate(key);

		for (int i = this.layers.size() - 2; i >= 1; i--) {
			((HiddenLayer) this.layers.get(i)).backPropogate(this.layers.get(i + 1));
		}
		return cost;
	}

}
