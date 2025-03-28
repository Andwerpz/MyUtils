package myutils.math;

import java.util.StringTokenizer;

public class Vec3 {
	public float x, y, z;

	public Vec3() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public Vec3(float val) {
		this.x = val;
		this.y = val;
		this.z = val;
	}

	public Vec3(double val) {
		this.x = (float) val;
		this.y = (float) val;
		this.z = (float) val;
	}

	public Vec3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3(double x, double y, double z) {
		this.x = (float) x;
		this.y = (float) y;
		this.z = (float) z;
	}

	public Vec3(float[] a) {
		this.x = a[0];
		this.y = a[1];
		this.z = a[2];
	}

	public Vec3(Vec3 a, Vec3 b) {
		this.x = b.x - a.x;
		this.y = b.y - a.y;
		this.z = b.z - a.z;
	}

	public Vec3(Vec3 v) {
		if (v == null) {
			return;
		}
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	public void set(Vec3 v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3 addi(Vec3 v) {
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
		return this;
	}

	public Vec3 add(Vec3 v) {
		Vec3 result = new Vec3(this);
		result.addi(v);
		return result;
	}

	public Vec3 subi(Vec3 v) {
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
		return this;
	}

	public Vec3 sub(Vec3 v) {
		Vec3 result = new Vec3(this);
		result.subi(v);
		return result;
	}

	public float lengthSq() {
		return this.x * this.x + this.y * this.y + this.z * this.z;
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	public Vec3 muli(float val) {
		this.x *= val;
		this.y *= val;
		this.z *= val;
		return this;
	}

	public Vec3 muli(double val) {
		this.x *= val;
		this.y *= val;
		this.z *= val;
		return this;
	}

	public Vec3 mul(float val) {
		Vec3 result = new Vec3(this);
		result.muli(val);
		return result;
	}

	public Vec3 muli(float x, float y, float z) {
		this.x *= x;
		this.y *= y;
		this.z *= z;
		return this;
	}

	public Vec3 mul(float x, float y, float z) {
		Vec3 result = new Vec3(this);
		result.muli(x, y, z);
		return result;
	}

	public Vec3 divi(float val) {
		this.x /= val;
		this.y /= val;
		this.z /= val;
		return this;
	}

	public Vec3 div(float val) {
		Vec3 result = new Vec3(this);
		result.divi(val);
		return result;
	}

	public Vec3 normalize() {
		float mag = this.length();
		if (mag == 0) {
			return this;
		}
		this.x /= mag;
		this.y /= mag;
		this.z /= mag;
		return this;
	}

	public float dot(Vec3 a) {
		return this.x * a.x + this.y * a.y + this.z * a.z;
	}

	/**
	 * returns cross(this, a)
	 * @param a
	 * @return
	 */

	public Vec3 cross(Vec3 a) {
		Vec3 result = new Vec3(0);
		result.x = this.y * a.z - this.z * a.y;
		result.y = this.z * a.x - this.x * a.z;
		result.z = this.x * a.y - this.y * a.x;
		return result;
	}

	// returns a new vector equal to this vector projected onto a.
	public Vec3 projectOnto(Vec3 a) {
		Vec3 result = new Vec3(a);
		return result.muli(this.dot(a) / a.dot(a)); // a.dot(a) is equal to a.length() * a.length().
	}

	public Vec3 setLength(float mag) {
		this.normalize();
		this.x *= mag;
		this.y *= mag;
		this.z *= mag;
		return this;
	}

	public Vec3 rotateX(float xRot) {
		float x = this.x;
		float y = this.y;
		float z = this.z;
		this.x = x;
		this.y = (float) ((y * Math.cos(xRot)) + (z * Math.sin(xRot)));
		this.z = (float) ((y * -Math.sin(xRot)) + (z * Math.cos(xRot)));
		return this;
	}

	public Vec3 rotateY(float yRot) {
		float x = this.x;
		float y = this.y;
		float z = this.z;
		this.x = (float) ((x * Math.cos(yRot)) + (z * -Math.sin(yRot)));
		this.y = y;
		this.z = (float) ((x * Math.sin(yRot)) + (z * Math.cos(yRot)));
		return this;
	}

	public Vec3 rotateZ(float zRot) {
		float x = this.x;
		float y = this.y;
		float z = this.z;
		this.x = (float) ((x * Math.cos(zRot)) + (y * Math.sin(zRot)));
		this.y = (float) ((x * -Math.sin(zRot)) + (y * Math.cos(zRot)));
		this.z = z;
		return this;
	}

	@Override
	public String toString() {
		return "[" + this.x + ", " + this.y + ", " + this.z + "]";
	}

	public static Vec3 parseVec3(String s) {
		StringTokenizer st = new StringTokenizer(s);
		float x = Float.parseFloat(st.nextToken());
		float y = Float.parseFloat(st.nextToken());
		float z = Float.parseFloat(st.nextToken());
		return new Vec3(x, y, z);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Vec3)) {
			return false;
		}
		Vec3 v = (Vec3) o;
		return v.x == this.x && v.y == this.y && v.z == this.z;
	}

	@Override
	public int hashCode() {
		return Float.hashCode(x) ^ Float.hashCode(y) ^ Float.hashCode(z);
	}
}
