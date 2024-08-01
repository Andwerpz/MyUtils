package myutils.noise.base;

import myutils.math.MathUtils;
import myutils.math.Vec3;
import myutils.noise.NoiseGenerator;

public class GradientGenerator extends NoiseGenerator {
	//define two points, each with their respective value. 
	//next, define two planes, each plane passing through exactly one point, and the vector pointing from one point
	//to another is normal to both planes. 
	
	//the gradient is between these two planes. 
	
	private Vec3 p1, p2;
	private float v1, v2;
	
	public GradientGenerator() {
		this.p1 = new Vec3(0);
		this.p2 = new Vec3(1);
		this.v1 = 0;
		this.v2 = 1;
	}
	
	public void setP1(Vec3 _p1) {
		this.p1.set(_p1);
	}
	
	public void setP2(Vec3 _p2) {
		this.p2.set(_p2);
	}
	
	public void setV1(float _v1) {
		this.v1 = _v1;
	}
	
	public void setV2(float _v2) {
		this.v2 = _v2;
	}
	
	public Vec3 getP1() {
		return new Vec3(this.p1);
	}
	
	public Vec3 getP2() {
		return new Vec3(this.p2);
	}
	
	public float getV1() {
		return this.v1;
	}
	
	public float getV2() {
		return this.v2;
	}

	@Override
	protected float _sampleNoise(float x, float y, float z) {
		Vec3 a = new Vec3(x, y, z);
		Vec3 p12 = p2.sub(p1);
		Vec3 p1a = a.sub(p1);
		float t = p12.dot(p1a) / p12.lengthSq();
		return MathUtils.lerp(this.v1, 0, this.v2, 1, t);
	}

}
