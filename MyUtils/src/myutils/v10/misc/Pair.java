package myutils.v10.misc;

public class Pair<T1, T2> implements Comparable<Pair<T1, T2>> {

	public T1 first;
	public T2 second;

	public Pair(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public int compareTo(Pair<T1, T2> o) {
		int ret = 0;
		if(this.first instanceof Comparable) {
			ret = ((Comparable<T1>) this.first).compareTo(o.first);
		}
		if(this.second instanceof Comparable && ret != 0) {
			ret = ((Comparable<T2>) this.second).compareTo(o.second);
		}
		return ret;
	}

}
