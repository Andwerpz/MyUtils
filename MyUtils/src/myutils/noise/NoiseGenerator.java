package myutils.noise;

public abstract class NoiseGenerator {
	//should support up to 3D noise

	//TODO 
	// - add domain warping 

	protected abstract float _sampleNoise(float x, float y, float z);

	public float sampleNoise(float x, float y, float z) {
		return this._sampleNoise(x, y, z);
	}

	public float sampleNoise(float x, float y) {
		return this._sampleNoise(x, y, 0);
	}

	public float sampleNoise(float x) {
		return this._sampleNoise(x, 0, 0);
	}

}
