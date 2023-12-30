package myutils.math;

public class IVec3 {

	public int x, y, z;

	public IVec3() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public IVec3(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public IVec3(int val) {
		this.x = val;
		this.y = val;
		this.z = val;
	}

	public IVec3(IVec3 other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}

	public void set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void set(IVec3 other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}

	public IVec3 addi(IVec3 other) {
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;
		return this;
	}

	public IVec3 add(IVec3 other) {
		IVec3 ret = new IVec3(this);
		return ret.addi(other);
	}

	public IVec3 subi(IVec3 other) {
		this.x -= other.x;
		this.y -= other.y;
		this.z -= other.z;
		return this;
	}

	public IVec3 sub(IVec3 other) {
		IVec3 ret = new IVec3(this);
		return ret.subi(other);
	}

	public IVec3 muli(int s) {
		this.x *= s;
		this.y *= s;
		this.z *= s;
		return this;
	}

	public IVec3 mul(int s) {
		IVec3 ret = new IVec3(this);
		return ret.muli(s);
	}

	public IVec3 divi(int s) {
		this.x /= s;
		this.y /= s;
		this.z /= s;
		return this;
	}

	public IVec3 div(int s) {
		IVec3 ret = new IVec3(this);
		return ret.divi(s);
	}

	public IVec3 modi(int s) {
		this.x %= s;
		this.y %= s;
		this.z %= s;
		return this;
	}

	public IVec3 mod(int s) {
		IVec3 ret = new IVec3(this);
		return ret.modi(s);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof IVec3)) {
			return false;
		}
		IVec3 otherVec = (IVec3) other;
		return this.x == otherVec.x && this.y == otherVec.y && this.z == otherVec.z;
	}

	@Override
	public int hashCode() {
		long mod = (long) (1e9 + 7);
		return (int) MathUtils.cantor(this.x, MathUtils.cantor(this.y, this.z, mod), mod);
	}

	@Override
	public String toString() {
		return "[" + this.x + ", " + this.y + ", " + this.z + "]";
	}

}
