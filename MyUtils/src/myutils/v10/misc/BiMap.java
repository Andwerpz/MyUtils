package myutils.v10.misc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class BiMap<T1, T2> {
	//two directional map

	//can't have any duplicate keys or values

	private HashMap<T1, T2> keyMap; //maps keys to values
	private HashMap<T2, T1> valueMap;

	public BiMap() {
		this.keyMap = new HashMap<>();
		this.valueMap = new HashMap<>();
	}

	/**
	 * Puts a key value pair into the map
	 * 
	 * @param key
	 * @param value
	 */

	public void put(T1 key, T2 value) {
		if (this.keyMap.keySet().contains(key) || this.valueMap.keySet().contains(value)) {
			return;
		}

		this.keyMap.put(key, value);
		this.valueMap.put(value, key);
	}

	public T2 getValue(T1 key) {
		return this.keyMap.get(key);
	}

	public T1 getKey(T2 value) {
		return this.valueMap.get(value);
	}

	public void removeKey(T1 key) {
		if (!this.keyMap.keySet().contains(key)) {
			return;
		}
		T2 value = this.getValue(key);

		this.keyMap.remove(key);
		this.valueMap.remove(value);
	}

	public void removeValue(T2 value) {
		if (!this.valueMap.keySet().contains(value)) {
			return;
		}
		T1 key = this.getKey(value);

		this.keyMap.remove(key);
		this.valueMap.remove(value);
	}

	public Set<T1> keySet() {
		return this.keyMap.keySet();
	}

	public Set<T2> values() {
		return this.valueMap.keySet();
	}

	public int size() {
		return this.keyMap.size();
	}

}
