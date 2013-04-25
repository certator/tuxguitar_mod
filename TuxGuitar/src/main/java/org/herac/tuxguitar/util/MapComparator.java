package org.herac.tuxguitar.util;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Compare elements using there values from a mapping
 * @param <K> Type of the element to compare
 * @param <V> Type of the value
 */
public class MapComparator<K,V extends Comparable<V>>  implements Comparator<K>{

	private final Map<K,V> mapping;

	public MapComparator(List<K> list, Map<K,V> mapping) {
		this.mapping = mapping;
	}

	@Override
	public int compare(K k1, K k2) {
		V v1 = mapping.get(k1);
		V v2 = mapping.get(k2);
		if (v1 != null) {
			return v1.compareTo(v2);
		}
		if (v2 != null) {
			return -v2.compareTo(v1);
		}
		return 0;
	}

}
