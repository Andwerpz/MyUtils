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
			return 0;
		}
		if (this.b != null) {
			v.x += b.sampleNoise(v.add(this.xOffset));
			v.y += b.sampleNoise(v.add(this.yOffset));
			v.z += b.sampleNoise(v.add(this.zOffset));
		}
		return a.sampleNoise(v);
	}

}
