package myutils.math;

import java.util.Objects;

public class IVec2 {

	public int x, y;

	public IVec2() {
		this.x = 0;
		this.y = 0;
	}

	public IVec2(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public IVec2(int val) {
		this.x = val;
		this.y = val;
	}

	public IVec2(IVec2 other) {
		this.x = other.x;
		this.y = other.y;
	}

	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void set(IVec2 other) {
		this.x = other.x;
		this.y = other.y;
	}

	public IVec2 addi(IVec2 other) {
		this.x += other.x;
		this.y += other.y;
		return this;
	}

	public IVec2 add(IVec2 other) {
		IVec2 ret = new IVec2(this);
		return ret.addi(other);
	}

	public IVec2 addi(int x, int y) {
		this.x += x;
		this.y += y;
		return this;
	}

	public IVec2 add(int x, int y) {
		IVec2 ret = new IVec2(this);
		return ret.addi(x, y);
	}

	public IVec2 subi(IVec2 other) {
		this.x -= other.x;
		this.y -= other.y;
		return this;
	}

	public IVec2 sub(IVec2 other) {
		IVec2 ret = new IVec2(this);
		return ret.subi(other);
	}

	public IVec2 muli(int s) {
		this.x *= s;
		this.y *= s;
		return this;
	}

	public IVec2 mul(int s) {
		IVec2 ret = new IVec2(this);
		return ret.muli(s);
	}

	public IVec2 divi(int s) {
		this.x /= s;
		this.y /= s;
		return this;
	}

	public IVec2 div(int s) {
		IVec2 ret = new IVec2(this);
		return ret.divi(s);
	}

	public IVec2 modi(int s) {
		this.x %= s;
		this.y %= s;
		return this;
	}

	public IVec2 mod(int s) {
		IVec2 ret = new IVec2(this);
		return ret.modi(s);
	}

	@Override
	public String toString() {
		return "[" + this.x + ", " + this.y + "]";
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof IVec2)) {
			return false;
		}
		IVec2 vec = (IVec2) other;
		return this.x == vec.x && this.y == vec.y;
	}

	@Override
	public final int hashCode() {
		return Objects.hash(this.x, this.y);
	}

}
