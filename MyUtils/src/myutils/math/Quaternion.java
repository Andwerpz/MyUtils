package myutils.math;

import java.util.StringTokenizer;

public class Quaternion {

	//in order to utilize this to represent 3D rotations, s is theta, and i, j, k, is the unit vector
	//that is the axis of rotation. 

	public float s, i, j, k;

	public Quaternion() {
		this.s = 0;
		this.i = 0;
		this.j = 0;
		this.k = 0;
	}

	public Quaternion(float s, float i, float j, float k) {
		this.s = s;
		this.i = i;
		this.j = j;
		this.k = k;
	}

	public Quaternion(Quaternion a) {
		this.s = a.s;
		this.i = a.i;
		this.j = a.j;
		this.k = a.k;
	}

	public static Quaternion identity() {
		return new Quaternion(1, 0, 0, 0);
	}

	public void set(Quaternion a) {
		this.s = a.s;
		this.i = a.i;
		this.j = a.j;
		this.k = a.k;
	}

	public Quaternion add(Quaternion a) {
		return new Quaternion(this.s + a.s, this.i + a.i, this.j + a.j, this.k + a.k);
	}

	public Quaternion addi(Quaternion a) {
		this.set(this.add(a));
		return this;
	}

	public Quaternion mul(float f) {
		return new Quaternion(this.s * f, this.i * f, this.j * f, this.k * f);
	}

	public Quaternion muli(float f) {
		this.set(this.mul(f));
		return this;
	}

	public Quaternion sub(Quaternion a) {
		return this.add(a.mul(-1));
	}

	public Quaternion subi(Quaternion a) {
		this.set(this.sub(a));
		return this;
	}

	public Quaternion mul(Quaternion a) {
		Vec3 v = new Vec3(this.i, this.j, this.k);
		Vec3 va = new Vec3(a.i, a.j, a.k);
		Vec3 vf = v.cross(va).add(va.mul(this.s)).add(v.mul(a.s));

		Quaternion ret = new Quaternion();
		ret.s = this.s * a.s - v.x * va.x - v.y * va.y - v.z * va.z;
		ret.i = vf.x;
		ret.j = vf.y;
		ret.k = vf.z;

		return ret;
	}

	public Quaternion muli(Quaternion a) {
		this.set(this.mul(a));
		return this;
	}

	public Quaternion conjugate() {
		return new Quaternion(this.s, -this.i, -this.j, -this.k);
	}

	public float lengthSq() {
		return this.s * this.s + this.i * this.i + this.j * this.j + this.k * this.k;
	}

	public float length() {
		return (float) Math.sqrt(this.lengthSq());
	}

	public Quaternion div(float f) {
		return this.mul(1.0f / f);
	}

	public Quaternion divi(float f) {
		this.set(this.div(f));
		return this;
	}

	public Quaternion inv() {
		return this.conjugate().div(this.lengthSq());
	}

	public Quaternion div(Quaternion a) {
		return this.mul(a.inv());
	}

	public Quaternion divi(Quaternion a) {
		this.set(this.div(a));
		return this;
	}
	
	public Quaternion expi() {
		float r = (float) Math.sqrt(this.i * this.i + this.j * this.j + this.k * this.k);
		float et = (float) Math.exp(this.s);
		float s = r >= 0.00001f? et * (float) Math.sin(r) / r : 0.0f;
		
		this.s = et * (float) Math.cos(r);
		this.i *= s;
		this.j *= s;
		this.k *= s;
		return this;
	}
	
	public Quaternion exp() {
		return new Quaternion(this).expi();
	}

	public Quaternion lni() {
		float r = (float) Math.sqrt(this.i * this.i + this.j * this.j + this.k * this.k);
		float t = r > 0.00001f? (float) Math.atan2(r, this.s) / r : 0.0f;
		
		this.s = 0.5f * (float) Math.log(this.s * this.s + this.i * this.i + this.j * this.j + this.k * this.k);
		this.i *= t;
		this.j *= t;
		this.k *= t;
		return this;
	}
	
	public Quaternion ln() {
		return new Quaternion(this).lni();
	}
	
	/**
	 * Only makes sense for unit rotation quaternions. 
	 * @param n
	 * @return
	 */
	public Quaternion powi(float n){
		this.lni();
		this.muli(n);
		this.expi();
	    return this;
	}
	
	public Quaternion pow(float n) {
		return new Quaternion(this).powi(n);
	}

	public void normalize() {
		this.divi(this.length());
	}

	@Override
	public String toString() {
		String ret = "";
		ret += this.s + " ";
		ret += this.i + " ";
		ret += this.j + " ";
		ret += this.k;
		return ret;
	}

	public static Quaternion parseQuaternion(String string) {
		StringTokenizer st = new StringTokenizer(string);
		float s = Float.parseFloat(st.nextToken());
		float i = Float.parseFloat(st.nextToken());
		float j = Float.parseFloat(st.nextToken());
		float k = Float.parseFloat(st.nextToken());
		return new Quaternion(s, i, j, k);
	}

}
