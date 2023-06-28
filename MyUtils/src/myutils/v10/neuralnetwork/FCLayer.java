package myutils.v10.neuralnetwork;

import myutils.v10.math.MathUtils;

public class FCLayer extends Layer{
	
	public static final int ACTIVATION_TYPE_SIGMOID = 0;
	public static final int ACTIVATION_TYPE_RELU = 1;
	
	//ACTIVATION
	int inputNodeAmt;
	
	public float[] inputNodes;
	public float[] inputNodeDerivatives;
	
	int outputNodeAmt;
	
	public float[] outputNodes;
	public float[] outputNodeDerivatives;
	
	//WEIGHTS
	public float[][] weights;
	public float[][] weightDerivatives;
	
	public int activationType;
	
	public FCLayer(NeuralNetwork neuralNetwork, int inputNodeAmt, int outputNodeAmt, int activationType) {
		super(neuralNetwork);
		
		this.activationType = activationType;
		
		this.inputNodeAmt = inputNodeAmt;
		this.outputNodeAmt = outputNodeAmt;
		
		this.generate(inputNodeAmt, outputNodeAmt);
		
		System.out.println(inputNodeAmt + " " + outputNodeAmt);
	}
	
	public void generate(int inputNodeAmt, int outputNodeAmt) {
		this.inputNodes = new float[inputNodeAmt];
		this.outputNodes = new float[outputNodeAmt];
		this.weights = new float[inputNodeAmt][outputNodeAmt];
		
		this.outputNodeDerivatives = new float[this.outputNodeAmt];
		this.inputNodeDerivatives = new float[this.inputNodeAmt];
		this.weightDerivatives = new float[this.inputNodeAmt][this.outputNodeAmt];
		
		for(int node = 0; node < inputNodeAmt; node++) {
			for(int weight = 0; weight < outputNodeAmt; weight++) {
				float nextWeight = 0;
				if(this.activationType == FCLayer.ACTIVATION_TYPE_SIGMOID) {
					float range = (float) (1f / Math.sqrt(inputNodeAmt));
					nextWeight = (float) (Math.random() * range * 2) - range;
				}
				else if(this.activationType == FCLayer.ACTIVATION_TYPE_RELU) {
					nextWeight = (float) (Math.random() * (Math.sqrt(2) / this.inputNodeAmt));
				}
				this.weights[node][weight] = nextWeight;
			}
		}
	}
	
	public double getCost(double ans) {
		double cost = 0;
		for(int node = 0; node < this.outputNodes.length; node++) {
			if(this.outputNodeAmt == 1) {
				cost += Math.pow(ans - outputNodes[node], 2);
			}
			else {
				cost += Math.pow(((int) ans == node? 1d : 0d) - outputNodes[node], 2);
			}
		}
		
		return cost;
	}
	
	public void forwardPropogate(float[] input) {
		//clear activation
		this.outputNodes = new float[outputNodeAmt];
		this.inputNodes = new float[inputNodeAmt];
		
		int inputSize = input.length;
		
		for(int i = 0; i < inputSize; i++) {
			this.inputNodes[i] = input[i];
		}
		
		this.calculateActivation();
	}

	@Override
	public void forwardPropogate(Layer l) {
		//clear activation
		this.outputNodes = new float[outputNodeAmt];
		this.inputNodes = new float[inputNodeAmt];
		
		if(l instanceof FCLayer) {
			this.inputNodes = ((FCLayer) l).outputNodes;
		}
		
		this.calculateActivation();
	}
	
	public void calculateActivation() {
		//loop through input nodes
		for(int node = 0; node < this.inputNodes.length; node++) {
			float val = this.inputNodes[node];
			float[] curWeights = this.weights[node];
			
			//loop through output nodes
			for(int weight = 0; weight < this.outputNodes.length; weight++) {
				this.outputNodes[weight] += val * curWeights[weight];
			}
		}
		
		for(int node = 0; node < this.outputNodes.length; node++) {
			//activation function
			
			if(this.activationType == FCLayer.ACTIVATION_TYPE_SIGMOID) {
				this.outputNodes[node] = MathUtils.sigmoid(this.outputNodes[node]);
			}
			else if(this.activationType == FCLayer.ACTIVATION_TYPE_RELU) {
				this.outputNodes[node] = MathUtils.relu(this.outputNodes[node]);
			}
			//System.out.print(this.outputNodes[node] + " ");
		}
	}
	
	//if this layer is the output layer
	public void backPropogate(float[] ans) {
		
		//clear derivatives
		this.outputNodeDerivatives = new float[this.outputNodeAmt];
		this.inputNodeDerivatives = new float[this.inputNodeAmt];
		this.weightDerivatives = new float[this.inputNodeAmt][this.outputNodeAmt];
		
		//cost is calculated as 
		//sum((ans[i] - output[i])^2) for all i
		//so the derivative of cost is -2 * (ans[i] - output[i])
		
		for(int node = 0; node < this.outputNodes.length; node++) {
			this.outputNodeDerivatives[node] = -2 * (ans[node] - this.outputNodes[node]);
		}
		
		//calc output node derivatives
//		for(int node = 0; node < this.outputNodes.length; node++) {
//			//calc cost function derivative
//			if(this.outputNodeAmt == 1) {
//				this.outputNodeDerivatives[node] = (this.outputNodes[node] - ans) * 2;
//			}
//			else {
//				this.outputNodeDerivatives[node] = (float) ((this.outputNodes[node] - ((int) ans == node? 1d : 0d)) * 2);
//			}
//		}
		
		this.calculateDerivatives();
	}

	//loads the derivative of the output of the output nodes to the cost
	@Override
	public void backPropogate(Layer l) {
		
		//clear derivatives
		this.outputNodeDerivatives = new float[this.outputNodeAmt];
		this.inputNodeDerivatives = new float[this.inputNodeAmt];
		this.weightDerivatives = new float[this.inputNodeAmt][this.outputNodeAmt];
		
		if(l instanceof FCLayer) {
			this.outputNodeDerivatives = ((FCLayer) l).inputNodeDerivatives;
		}
		
		this.calculateDerivatives();
	}
	
	@Override
	public void calculateDerivatives() {
		
		//apply derivative to output node derivatives
		for(int node = 0; node < this.outputNodeAmt; node++) {
			if(this.activationType == FCLayer.ACTIVATION_TYPE_SIGMOID) {
				//outputNodeDerivatives store the value after applying the sigmoid function
				//since we need the input to calc the derivative, use logit function to get the input
				this.outputNodeDerivatives[node] *= MathUtils.sigmoidDerivative(MathUtils.logit(this.outputNodes[node]));
			}
			else if(this.activationType == FCLayer.ACTIVATION_TYPE_RELU) {
				//if the node output == 0, then the derivative = 0, else, derivative = 1.
				this.outputNodeDerivatives[node] *= MathUtils.reluDerivative(this.outputNodes[node]);
			}
		}
		
		//calc weight and input node derivatives
		for(int node = 0; node < this.inputNodes.length; node++) {
			float[] curWeights = this.weights[node];
			float[] curWeightDerivatives = this.weightDerivatives[node];
			
			//loop through weights
			for(int weight = 0; weight < curWeights.length; weight++) {
				this.inputNodeDerivatives[node] += this.outputNodeDerivatives[weight] * curWeights[weight];
				curWeightDerivatives[weight] = this.outputNodeDerivatives[weight] * this.inputNodes[node];
			}
		}
		
		//adjust weights
		for(int node = 0; node < this.weights.length; node++) {
			for(int weight = 0; weight < this.weights[0].length; weight++) {
				this.weights[node][weight] -= this.weightDerivatives[node][weight] * this.neuralNetwork.learningRate;
			}
		}
	}

}
