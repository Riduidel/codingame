package org.ndx.codingame.gaming;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ComparatorChain<Type> implements Comparator<Type> {
	private List<Comparator<Type>> comparators;

	public ComparatorChain(final Comparator<Type>...comparators) {
		this(Arrays.asList(comparators));
	}

	public ComparatorChain(final List<Comparator<Type>> asList) {
		comparators = asList;
	}

	@Override
	public int compare(final Type o1, final Type o2) {
		int returned = 0;
		for(final Comparator<Type> c : comparators) {
			returned = c.compare(o1, o2);
			if(returned!=0) {
				return returned;
			}
		}
		return returned;
	}
}
