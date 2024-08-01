package myutils.noise.operator;

import myutils.noise.NoiseGenerator;
import myutils.noise.base.UniformGenerator;

public class NoiseAdd extends NoiseGenerator {
	
	private NoiseGenerator a = null, b = null;
	
	public void setA(NoiseGenerator _a) {
		this.a = _a;
	}
	
	public void setB(NoiseGenerator _b) {
		this.b = _b;
	}

	@Override
	protected float _sampleNoise(float x, float y, float z) {
		float ret = 0;
		if(this.a != null) {
			ret += this.a.sampleNoise(x, y, z);
		}
		if(this.b != null) {
			ret += this.b.sampleNoise(x, y, z);
		}
		return ret;
	}

}
