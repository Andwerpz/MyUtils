package myutils.misc;

import java.util.Objects;

public class Triple<T1, T2, T3> implements Comparable<Triple<T1, T2, T3>> {

	public T1 first;
	public T2 second;
	public T3 third;

	public Triple(T1 first, T2 second, T3 third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	@Override
	public int compareTo(Triple<T1, T2, T3> o) {
		int ret = 0;
		if (this.first instanceof Comparable) {
			ret = ((Comparable<T1>) this.first).compareTo(o.first);
		}
		if (this.second instanceof Comparable && ret != 0) {
			ret = ((Comparable<T2>) this.second).compareTo(o.second);
		}
		if (this.third instanceof Comparable && ret != 0) {
			ret = ((Comparable<T3>) this.third).compareTo(o.third);
		}
		return ret;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.first, this.second, this.third);
	}

	@Override
	public boolean equals(Object arg) {
		if (arg == null) {
			return false;
		}
		if (!(arg instanceof Triple<?, ?, ?>)) {
			return false;
		}
		if (arg == this) {
			return true;
		}
		Triple<?, ?, ?> p = (Triple<?, ?, ?>) arg;
		return this.first.equals(p.first) && this.second.equals(p.second) && this.third.equals(p.third);
	}

	@Override
	public String toString() {
		return "(" + this.first.toString() + ", " + this.second.toString() + ", " + this.third.toString() + ")";
	}

}
