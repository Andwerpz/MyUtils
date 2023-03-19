package myutils.v10.math;

public class Vec4 {

	public float x, y, z, w;

	public Vec4(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
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

	public Vec4(float a) {
		this.x = a;
		this.y = a;
		this.z = a;
		this.w = a;
	}

	@Override
	public String toString() {
		return this.x + ", " + this.y + ", " + this.z + ", " + this.w;
	}

}
