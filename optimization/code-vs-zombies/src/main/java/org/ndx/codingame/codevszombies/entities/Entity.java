package org.ndx.codingame.codevszombies.entities;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.ndx.codingame.gaming.tounittest.ConstructableInUnitTest;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public interface Entity extends ConstructableInUnitTest {
	public static <Type extends Entity> SortedMap<Double, Type> byDistanceTo(final Collection<Type> source, final Entity entity) {
		return byDistanceTo(source, entity.getPosition());
	}
	public static <Type extends Entity> SortedMap<Double, Type> byDistanceTo(final Collection<Type> source, final ContinuousPoint position) {
		return source.stream()
				.collect(Collectors.toMap(
						(entity) -> position.distance2To(entity.getPosition()), 
						(entity) -> entity, 
						(v1, v2) -> v1,
						TreeMap::new));
	}

	public ContinuousPoint getPosition();

	public default <Type extends Entity> SortedMap<Double, Type> byDistanceTo(final Collection<Type> source) {
		return byDistanceTo(source, this);
	}
}
