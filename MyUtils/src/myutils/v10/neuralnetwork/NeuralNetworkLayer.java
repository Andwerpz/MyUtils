package myutils.v10.neuralnetwork;

public abstract class NeuralNetworkLayer {
	
	public NeuralNetwork neuralNetwork;
	
	public NeuralNetworkLayer(NeuralNetwork neuralNetwork) {
		this.neuralNetwork = neuralNetwork;
	}
	
	public abstract void forwardPropogate(float[] input);	//for the first layer
	public abstract void forwardPropogate(NeuralNetworkLayer l);
	public abstract void backPropogate(NeuralNetworkLayer l);
	
	public abstract void calculateActivation();
	public abstract void calculateDerivatives();
}
