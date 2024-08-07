package myutils.noise.unary;

import myutils.math.Vec3;

public class NoiseAffine extends NoiseUnaryOperator {

	private Vec3 offset = new Vec3(0);
	private float scale = 1;

	public void setOffset(Vec3 _offset) {
		this.offset.set(_offset);
	}

	public Vec3 getOffset() {
		return new Vec3(this.offset);
	}
	
	public void setScale(float _scale) {
		this.scale = _scale;
	}
	
	public float getScale() {
		return this.scale;
	}

	@Override
	protected float _sampleNoise(Vec3 v) {
		if(a == null) return 0;
		return a.sampleNoise(v.mul(this.scale).add(this.offset));
	}

}
