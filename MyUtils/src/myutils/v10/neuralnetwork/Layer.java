package myutils.v10.neuralnetwork;

public abstract class Layer {
	
	public NeuralNetwork neuralNetwork;
	
	public Layer(NeuralNetwork neuralNetwork) {
		this.neuralNetwork = neuralNetwork;
	}
	
	public abstract void forwardPropogate(float[] input);	//for the first layer
	public abstract void forwardPropogate(Layer l);
	public abstract void backPropogate(Layer l);
	
	public abstract void calculateActivation();
	public abstract void calculateDerivatives();
}
