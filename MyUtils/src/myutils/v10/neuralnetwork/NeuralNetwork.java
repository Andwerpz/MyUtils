package myutils.v10.neuralnetwork;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class NeuralNetwork {
	
	public float learningRate = 0.01f;
	
	public ArrayList<Layer> layers;

	public NeuralNetwork() {
		this.layers = new ArrayList<>();
	}

//	public void generateNetwork() {
//		this.layers = new ArrayList<>();
//		
//		int numImages = 1;
//		int imageSize = TESTCASE_SIZE;
//		
////		this.layers.add(new ConvolutionLayer(3, 5, 1, 28));	//output 3x 24 by 24
////		this.layers.add(new FlatteningLayer(3, 24));	
//		
//		this.layers.add(new FCLayer(TESTCASE_SIZE * TESTCASE_SIZE, 300, FCLayer.ACTIVATION_TYPE_RELU));
//		this.layers.add(new FCLayer(300, 100, FCLayer.ACTIVATION_TYPE_RELU));
//		this.layers.add(new FCLayer(100, 10, FCLayer.ACTIVATION_TYPE_SIGMOID));
//		
////		this.layers.add(new FCLayer(8, 80, FCLayer.ACTIVATION_TYPE_RELU));
////		this.layers.add(new FCLayer(80, 80, FCLayer.ACTIVATION_TYPE_RELU));
////		this.layers.add(new FCLayer(80, 10, FCLayer.ACTIVATION_TYPE_SIGMOID));
//		
////		this.layers.add(new FCLayer(3600, 1000, FCLayer.ACTIVATION_TYPE_RELU));
////		this.layers.add(new FCLayer(1000, 300, FCLayer.ACTIVATION_TYPE_RELU));
////		this.layers.add(new FCLayer(300, 2, FCLayer.ACTIVATION_TYPE_SIGMOID));
//	}

	public float[] forwardPropogate(float[] input) {
//		if(vectorMode) {
//			this.layers.get(0).forwardPropogate(tc.vector);
//		}
//		else if(imageMode) {
//			this.layers.get(0).forwardPropogate(tc.activation);
//		}
		
		this.layers.get(0).forwardPropogate(input);
		for(int i = 1; i < this.layers.size(); i++) {
			this.layers.get(i).forwardPropogate(this.layers.get(i - 1));
		}
		
		FCLayer fl = (FCLayer) this.layers.get(this.layers.size() - 1);
		
//		if(vectorMode) {
//			ans = fl.outputNodes[0];
//		}
//		
//		else if(imageMode) {
//			double maxActivation = fl.outputNodes[0];
//			
//			for(int i = 0; i < fl.outputNodes.length; i++) {
//				double nextActivation = fl.outputNodes[i];
//				if(maxActivation < nextActivation) {
//					ans = i;
//					maxActivation = nextActivation;
//				}
//			}
//		}
		
		return fl.outputNodes;
	}

	
	//does backprop and nudges the weights
	public void backPropogate(float[] input, float[] key) {
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
