package myutils.v10.neuralnetwork;

public abstract class NeuralNetworkLayer {

	//perhaps the layers themselves should handle propogating between layers, 
	//kind of like a linked list, maybe layers should contain references to layers before and after themselves. 

	public NeuralNetwork neuralNetwork;

	public NeuralNetworkLayer(NeuralNetwork neuralNetwork) {
		this.neuralNetwork = neuralNetwork;
	}
}
