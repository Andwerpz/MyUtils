package myutils.noise.binary;

import myutils.math.MathUtils;
import myutils.math.Vec3;

public class NoiseDomainWarp extends NoiseBinaryOperator {

	private Vec3 xOffset, yOffset, zOffset;

	public NoiseDomainWarp() {
		this.xOffset = MathUtils.random(new Vec3(-1), new Vec3(1));
		this.yOffset = MathUtils.random(new Vec3(-1), new Vec3(1));
		this.zOffset = MathUtils.random(new Vec3(-1), new Vec3(1));
	}

	@Override
	protected float _sampleNoise(Vec3 v) {
		if (this.a == null) {
			System.out.println("A IS NULL");
			return 0;
		}
		if (this.b != null) {
			v.x += b.sampleNoise(v.add(this.xOffset));
			v.y += b.sampleNoise(v.add(this.yOffset));
			v.z += b.sampleNoise(v.add(this.zOffset));
		}
		return a.sampleNoise(v);
	}
	
	public Vec3 getXOffset() {
		return xOffset;
	}

	public void setXOffset(Vec3 xOffset) {
		this.xOffset = xOffset;
	}

	public Vec3 getYOffset() {
		return yOffset;
	}

	public void setYOffset(Vec3 yOffset) {
		this.yOffset = yOffset;
	}

	public Vec3 getZOffset() {
		return zOffset;
	}

	public void setZOffset(Vec3 zOffset) {
		this.zOffset = zOffset;
	}

}
