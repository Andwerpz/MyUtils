package myutils.noise.unary;

import myutils.math.Vec3;
import myutils.noise.NoiseGenerator;

public abstract class NoiseUnaryOperator extends NoiseGenerator {
	//takes in one noise generator and applies some sort of change to it

	protected NoiseGenerator a;

	public void setA(NoiseGenerator _a) {
		this.a = _a;
	}
}
