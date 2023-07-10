package myutils.v10.neuralnetwork;

public abstract class HiddenLayer extends NeuralNetworkLayer {

	public HiddenLayer(NeuralNetwork neuralNetwork) {
		super(neuralNetwork);
	}

	public abstract void forwardPropogate(NeuralNetworkLayer l);

	public abstract void backPropogate(NeuralNetworkLayer l);

	public abstract void calculateActivation();

	public abstract void calculateDerivatives();

}
