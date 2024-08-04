package myutils.noise.binary;

import myutils.math.Vec3;
import myutils.noise.NoiseGenerator;
import myutils.noise.base.UniformGenerator;

public class NoiseAdd extends NoiseBinaryOperator {
	@Override
	protected float _sampleNoise(Vec3 v) {
		float ret = 0;
		if (this.a != null) {
			ret += this.a.sampleNoise(v);
		}
		if (this.b != null) {
			ret += this.b.sampleNoise(v);
		}
		return ret;
	}
}
