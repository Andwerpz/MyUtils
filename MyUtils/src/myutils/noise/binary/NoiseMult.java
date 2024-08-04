package myutils.noise.binary;

import myutils.math.Vec3;

public class NoiseMult extends NoiseBinaryOperator {

	@Override
	protected float _sampleNoise(Vec3 v) {
		return this.a.sampleNoise(v) * this.b.sampleNoise(v);
	}

}
