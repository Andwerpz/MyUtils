package myutils.noise.unary;

import myutils.math.Vec3;

public class NoiseOffset extends NoiseUnaryOperator {

	private Vec3 offset = new Vec3(0);

	public void setOffset(Vec3 _offset) {
		this.offset.set(_offset);
	}

	public Vec3 getOffset() {
		return new Vec3(this.offset);
	}

	@Override
	protected float _sampleNoise(Vec3 v) {
		return a.sampleNoise(v.add(this.offset));
	}

}
