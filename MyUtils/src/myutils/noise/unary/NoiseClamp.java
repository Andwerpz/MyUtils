package myutils.noise.unary;

import myutils.math.MathUtils;
import myutils.math.Vec3;

public class NoiseClamp extends NoiseUnaryOperator {

	private float low = 0, high = 1;

	public void setLow(float _low) {
		this.low = _low;
	}

	public void setHigh(float _high) {
		this.high = _high;
	}

	public float getLow() {
		return this.low;
	}

	public float getHigh() {
		return this.high;
	}

	@Override
	protected float _sampleNoise(Vec3 v) {
		return MathUtils.clamp(low, high, this.a.sampleNoise(v));
	}

}
