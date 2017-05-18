package org.ndx.codingame.gaming;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

public class MapUtils {
	private static class ValueComparator<Key, Value extends Comparable<? super Value>> implements Comparator<Map.Entry<Key, Value>> {

		@Override
		public int compare(final Entry<Key, Value> o1, final Entry<Key, Value> o2) {
			return o1.getValue().compareTo(o2.getValue());
		}

	}
	public static <Key, Value> Value safeGet(final Map<Key, Value> map, final Key key, final Value defaultValue) {
		if(map.containsKey(key)) {
			return map.get(key);
		} else {
			return defaultValue;
		}
	}
	public static <Key, Value extends Comparable<? super Value>> Optional<Key> keyWithSmallestValue(final Map<Key, Value> map) {
		if(map.isEmpty()) {
			return Optional.empty();
		} else {
			Map.Entry<Key, Value> smallestEntry = null;
			for(final Map.Entry<Key, Value> entry : map.entrySet()) {
				if(smallestEntry==null) {
					smallestEntry = entry;
				} else {
					if(smallestEntry.getValue().compareTo(entry.getValue())>0) {
						smallestEntry = entry;
					}
				}
			}
			return Optional.of(smallestEntry.getKey());
		}
	}

	public static <Key, Value extends Comparable<? super Value>> Deque<Key> keysByValue(final Map<Key, Value> map) {
		final List<Map.Entry<Key, Value>> content = new ArrayList<>(map.entrySet());
		content.sort(new ValueComparator<>());
		final Deque<Key> returned = new ArrayDeque<>();
		for(final Map.Entry<Key, Value> entry : content) {
			returned.add(entry.getKey());
		}
		return returned;
	}
}
