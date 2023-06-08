package myutils.v10.misc;

public class Pair<T1 extends Comparable<T1>, T2 extends Comparable<T2>> implements Comparable<Pair<T1, T2>> {

	public T1 first;
	public T2 second;

	public Pair(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public int compareTo(Pair<T1, T2> o) {
		int ret = o.first.compareTo(this.first);
		ret = ret == 0? o.second.compareTo(this.second) : 0;
		return ret;
	}

}
