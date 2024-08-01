package myutils.noise.base;

import myutils.noise.NoiseGenerator;

public class UniformGenerator extends NoiseGenerator {
	//kinda useless, just outputs a constant value. 
	//mostly here for the sake of completeness
	
	float value;
	
	public UniformGenerator() {
		this.value = 0;
	}
	
	public void setValue(float _value) {
		this.value = _value;
	}

	@Override
	protected float _sampleNoise(float x, float y, float z) {
		return this.value;
	}

}
