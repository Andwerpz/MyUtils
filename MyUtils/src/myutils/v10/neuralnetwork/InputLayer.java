package myutils.v10.neuralnetwork;

public class InputLayer extends NeuralNetworkLayer {

	//this layer consists of an array of output nodes, which are the input activations. 

	public int nodeAmt;
	public float[] nodes;

	public InputLayer(NeuralNetwork neuralNetwork, int nodeAmt) {
		super(neuralNetwork);

		this.nodeAmt = nodeAmt;
		this.nodes = new float[nodeAmt];
	}

	public void setActivation(float[] activations) {
		if (activations.length != this.nodeAmt) {
			System.err.println("InputLayer : INPUT ARRAY IS WRONG SIZE");
			return;
		}

		for (int i = 0; i < this.nodeAmt; i++) {
			this.nodes[i] = activations[i];
		}
	}

}
