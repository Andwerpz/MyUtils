package myutils.noise.binary;

import myutils.noise.NoiseGenerator;

public abstract class NoiseBinaryOperator extends NoiseGenerator {
	//takes in exactly 2 noise generators, and combines them somehow. 

	protected NoiseGenerator a, b;

	public void setA(NoiseGenerator _a) {
		this.a = _a;
	}

	public void setB(NoiseGenerator _b) {
		this.b = _b;
	}

}
