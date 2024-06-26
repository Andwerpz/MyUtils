package myutils.math;

public class Vec2 {

	public float x, y;

	public Vec2() {
		set(0, 0);
	}

	public Vec2(Vec2 a, Vec2 b) {
		set(b.x - a.x, b.y - a.y);
	}

	public Vec2(float x, float y) {
		set(x, y);
	}

	public Vec2(float a) {
		set(a, a);
	}

	public Vec2(double x, double y) {
		set((float) x, (float) y);
	}

	public Vec2(double a) {
		set(a, a);
	}

	public Vec2(IVec2 a) {
		set(a.x, a.y);
	}

	public Vec2(Vec2 v) {
		set(v);
	}

	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void set(double x, double y) {
		set((float) x, (float) y);
	}

	public Vec2 set(Vec2 v) {
		x = v.x;
		y = v.y;
		return this;
	}

	public Vec2 setLength(float len) {
		this.normalize();
		this.muli(len);
		return this;
	}

	public Vec2 setLength(double len) {
		return this.setLength((float) len);
	}

	/**
	 * Negates this vector and returns this.
	 */
	public Vec2 negi() {
		return neg(this);
	}

	/**
	 * Sets out to the negation of this vector and returns out.
	 */
	public Vec2 neg(Vec2 out) {
		out.x = -x;
		out.y = -y;
		return out;
	}

	/**
	 * Returns a new vector that is the negation to this vector.
	 */
	public Vec2 neg() {
		return neg(new Vec2());
	}

	/**
	 * Multiplies this vector by s and returns this.
	 */
	public Vec2 muli(float s) {
		return mul(s, this);
	}

	/**
	 * Sets out to this vector multiplied by s and returns out.
	 */
	public Vec2 mul(float s, Vec2 out) {
		out.x = s * x;
		out.y = s * y;
		return out;
	}

	/**
	 * Returns a new vector that is a multiplication of this vector and s.
	 */
	public Vec2 mul(float s) {
		return mul(s, new Vec2());
	}

	public Vec2 muli(double s) {
		return this.muli((float) s);
	}

	public Vec2 mul(double s, Vec2 out) {
		return this.mul((float) s, out);
	}

	public Vec2 mul(double s) {
		return this.mul((float) s);
	}

	/**
	 * Divides this vector by s and returns this.
	 */
	public Vec2 divi(float s) {
		return div(s, this);
	}

	/**
	 * Sets out to the division of this vector and s and returns out.
	 */
	public Vec2 div(float s, Vec2 out) {
		out.x = x / s;
		out.y = y / s;
		return out;
	}

	/**
	 * Returns a new vector that is a division between this vector and s.
	 */
	public Vec2 div(float s) {
		return div(s, new Vec2());
	}

	/**
	 * Multiplies this vector by v and returns this.
	 */
	public Vec2 muli(Vec2 v) {
		return mul(v, this);
	}

	/**
	 * Sets out to the product of this vector and v and returns out.
	 */
	public Vec2 mul(Vec2 v, Vec2 out) {
		out.x = x * v.x;
		out.y = y * v.y;
		return out;
	}

	/**
	 * Returns a new vector that is the product of this vector and v.
	 */
	public Vec2 mul(Vec2 v) {
		return mul(v, new Vec2());
	}

	/**
	 * Divides this vector by v and returns this.
	 */
	public Vec2 divi(Vec2 v) {
		return div(v, this);
	}

	/**
	 * Sets out to the division of this vector and v and returns out.
	 */
	public Vec2 div(Vec2 v, Vec2 out) {
		out.x = x / v.x;
		out.y = y / v.y;
		return out;
	}

	/**
	 * Returns a new vector that is the division of this vector by v.
	 */
	public Vec2 div(Vec2 v) {
		return div(v, new Vec2());
	}

	/**
	 * Adds v to this vector and returns this.
	 */
	public Vec2 addi(Vec2 v) {
		return add(v, this);
	}

	/**
	 * Sets out to the addition of this vector and v and returns out.
	 */
	public Vec2 add(Vec2 v, Vec2 out) {
		out.x = x + v.x;
		out.y = y + v.y;
		return out;
	}

	/**
	 * Returns a new vector that is the addition of this vector and v.
	 */
	public Vec2 add(Vec2 v) {
		return add(v, new Vec2());
	}

	public Vec2 add(float x, float y) {
		return add(new Vec2(x, y), new Vec2());
	}

	public Vec2 addi(float x, float y) {
		return add(new Vec2(x, y), this);
	}

	public Vec2 add(float s) {
		return add(new Vec2(s), new Vec2());
	}

	public Vec2 addi(float s) {
		return add(new Vec2(s), this);
	}

	/**
	 * Adds v * s to this vector and returns this.
	 */
	public Vec2 addsi(Vec2 v, float s) {
		return adds(v, s, this);
	}

	/**
	 * Sets out to the addition of this vector and v * s and returns out.
	 */
	public Vec2 adds(Vec2 v, float s, Vec2 out) {
		out.x = x + v.x * s;
		out.y = y + v.y * s;
		return out;
	}

	/**
	 * Returns a new vector that is the addition of this vector and v * s.
	 */
	public Vec2 adds(Vec2 v, float s) {
		return adds(v, s, new Vec2());
	}

	/**
	 * Subtracts v from this vector and returns this.
	 */
	public Vec2 subi(Vec2 v) {
		return sub(v, this);
	}

	/**
	 * Sets out to the subtraction of v from this vector and returns out.
	 */
	public Vec2 sub(Vec2 v, Vec2 out) {
		out.x = x - v.x;
		out.y = y - v.y;
		return out;
	}

	/**
	 * Returns a new vector that is the subtraction of v from this vector.
	 */
	public Vec2 sub(Vec2 v) {
		return sub(v, new Vec2());
	}

	public Vec2 sub(float x, float y) {
		return sub(new Vec2(x, y), new Vec2());
	}

	public Vec2 subi(float x, float y) {
		return sub(new Vec2(x, y), this);
	}

	/**
	 * Returns a new vector that is equal to (this.x - s, this.y - s);
	 * @param f
	 * @return
	 */
	public Vec2 sub(float s) {
		return sub(new Vec2(s), new Vec2());
	}

	public Vec2 subi(float s) {
		return sub(new Vec2(s), this);
	}

	/**
	 * Returns the squared length of this vector.
	 */
	public float lengthSq() {
		return x * x + y * y;
	}

	/**
	 * Returns the length of this vector.
	 */
	public float length() {
		return (float) StrictMath.sqrt(x * x + y * y);
	}

	/**
	 * Rotates this vector by the given radians.
	 * @return 
	 */
	public Vec2 rotate(float radians) {
		float c = (float) StrictMath.cos(radians);
		float s = (float) StrictMath.sin(radians);

		float xp = x * c - y * s;
		float yp = x * s + y * c;

		x = xp;
		y = yp;

		return this;
	}

	/**
	 * Rotates this vector by the given radians.
	 * @return 
	 */
	public Vec2 rotate(double radians) {
		return this.rotate((float) radians);
	}

	/**
	 * Normalizes this vector, making it a unit vector. A unit vector has a length
	 * of 1.0.
	 */
	public Vec2 normalize() {
		float lenSq = lengthSq();

		if (lenSq > 0.000000001) {
			float invLen = 1.0f / (float) StrictMath.sqrt(lenSq);
			x *= invLen;
			y *= invLen;
		}

		return this;
	}

	/**
	 * Sets this vector to the minimum between a and b.
	 */
	public Vec2 mini(Vec2 a, Vec2 b) {
		return min(a, b, this);
	}

	/**
	 * Sets this vector to the maximum between a and b.
	 */
	public Vec2 maxi(Vec2 a, Vec2 b) {
		return max(a, b, this);
	}

	/**
	 * Returns the dot product between this vector and v.
	 */
	public float dot(Vec2 v) {
		return dot(this, v);
	}

	/**
	 * Returns the squared distance between this vector and v.
	 */
	public float distanceSq(Vec2 v) {
		return distanceSq(this, v);
	}

	/**
	 * Returns the distance between this vector and v.
	 */
	public float distance(Vec2 v) {
		return distance(this, v);
	}

	/**
	 * Sets this vector to the cross between v and a and returns this.
	 */
	public Vec2 cross(Vec2 v, float a) {
		return cross(v, a, this);
	}

	/**
	 * Sets this vector to the cross between a and v and returns this.
	 */
	public Vec2 cross(float a, Vec2 v) {
		return cross(a, v, this);
	}

	/**
	 * Returns the scalar cross between this vector and v. This is essentially the
	 * length of the cross product if this vector were 3d. This can also indicate
	 * which way v is facing relative to this vector.
	 */
	public float cross(Vec2 v) {
		return cross(this, v);
	}

	public static Vec2 min(Vec2 a, Vec2 b, Vec2 out) {
		out.x = StrictMath.min(a.x, b.x);
		out.y = StrictMath.min(a.y, b.y);
		return out;
	}

	public static Vec2 max(Vec2 a, Vec2 b, Vec2 out) {
		out.x = StrictMath.max(a.x, b.x);
		out.y = StrictMath.max(a.y, b.y);
		return out;
	}

	public static float dot(Vec2 a, Vec2 b) {
		return a.x * b.x + a.y * b.y;
	}

	public static float distanceSq(Vec2 a, Vec2 b) {
		float dx = a.x - b.x;
		float dy = a.y - b.y;

		return dx * dx + dy * dy;
	}

	public static float distance(Vec2 a, Vec2 b) {
		float dx = a.x - b.x;
		float dy = a.y - b.y;

		return (float) StrictMath.sqrt(dx * dx + dy * dy);
	}

	public static Vec2 cross(Vec2 v, float a, Vec2 out) {
		out.x = v.y * a;
		out.y = v.x * -a;
		return out;
	}

	public static Vec2 cross(float a, Vec2 v, Vec2 out) {
		out.x = v.y * -a;
		out.y = v.x * a;
		return out;
	}

	public static float cross(Vec2 a, Vec2 b) {
		return a.x * b.y - a.y * b.x;
	}

	/**
	 * Returns an array of allocated Vec2 of the requested length.
	 */
	public static Vec2[] arrayOf(int length) {
		Vec2[] array = new Vec2[length];

		while (--length >= 0) {
			array[length] = new Vec2();
		}

		return array;
	}

	@Override
	public String toString() {
		return "[" + this.x + ", " + this.y + "]";
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Vec2)) {
			return false;
		}
		Vec2 v = (Vec2) other;
		return this.x == v.x && this.y == v.y;
	}
}
