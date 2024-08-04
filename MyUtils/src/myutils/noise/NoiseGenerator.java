package myutils.noise;

import myutils.math.Vec3;

public abstract class NoiseGenerator {
	//should support up to 3D noise

	protected abstract float _sampleNoise(Vec3 v);

	public float sampleNoise(Vec3 v) {
		return this._sampleNoise(v);
	}

	public float sampleNoise(float x, float y, float z) {
		return this._sampleNoise(new Vec3(x, y, z));
	}

	public float sampleNoise(float x, float y) {
		return this._sampleNoise(new Vec3(x, y, 0));
	}

	public float sampleNoise(float x) {
		return this._sampleNoise(new Vec3(x, 0, 0));
	}

}
