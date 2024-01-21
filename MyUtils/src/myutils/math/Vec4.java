package myutils.math;

public class Vec4 {

	public float x, y, z, w;

	public Vec4(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Vec4(double x, double y, double z, double w) {
		this.x = (float) x;
		this.y = (float) y;
		this.z = (float) z;
		this.w = (float) w;
	}

	public Vec4(Vec4 v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		this.w = v.w;
	}

	public Vec4(Vec3 v, float w) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		this.w = w;
	}

	public Vec4(Vec2 a, Vec2 b) {
		this.x = a.x;
		this.y = a.y;
		this.z = b.x;
		this.w = b.y;
	}

	public Vec4(float a) {
		this.x = a;
		this.y = a;
		this.z = a;
		this.w = a;
	}

	public Vec4(double a) {
		this.x = (float) a;
		this.y = (float) a;
		this.z = (float) a;
		this.w = (float) a;
	}

	public void set(Vec4 v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		this.w = v.w;
	}

	public Vec4 mul(float f) {
		Vec4 ret = new Vec4(this);
		ret.x *= f;
		ret.y *= f;
		ret.z *= f;
		ret.w *= f;
		return ret;
	}

	public Vec4 muli(float f) {
		this.set(this.mul(f));
		return this;
	}

	@Override
	public String toString() {
		return "[" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + "]";
	}

}
