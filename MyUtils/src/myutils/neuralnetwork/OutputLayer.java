package myutils.neuralnetwork;

public class OutputLayer extends FCLayer {

	//just an extension of the FCLayer, modified to calculate cost. 

	public OutputLayer(NeuralNetwork neuralNetwork, int inputNodeAmt, int outputNodeAmt, int activationType) {
		super(neuralNetwork, inputNodeAmt, outputNodeAmt, activationType);
	}

	public float calculateCost(float[] ans) {
		float cost = 0;
		for (int node = 0; node < this.outputNodes.length; node++) {
			cost += (float) Math.pow(ans[node] - this.outputNodes[node], 2);
		}
		return cost;
	}

	//returns the cost of the neural network. 
	public float backPropogate(float[] ans) {

		//clear derivatives
		this.outputNodeDerivatives = new float[this.outputNodeAmt];
		this.inputNodeDerivatives = new float[this.inputNodeAmt];
		this.weightDerivatives = new float[this.inputNodeAmt][this.outputNodeAmt];

		//cost is calculated as the sum of squared differences between the generated output and the expected answer. 
		//sum((ans[i] - output[i])^2) for all i
		//so the derivative of cost is -2 * (ans[i] - output[i])

		float cost = 0;
		for (int node = 0; node < this.outputNodes.length; node++) {
			cost += (float) Math.pow(ans[node] - this.outputNodes[node], 2);
			this.outputNodeDerivatives[node] = -2 * (ans[node] - this.outputNodes[node]);
		}

		this.calculateDerivatives();
		return cost;
	}

}
