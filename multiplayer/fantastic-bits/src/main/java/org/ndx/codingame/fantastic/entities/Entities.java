package org.ndx.codingame.fantastic.entities;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.ndx.codingame.lib2d.Segment;
import org.ndx.codingame.lib2d.base.AbstractPoint;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public class Entities {

	private final List<Entity> entities;
	private final List<Wizard> myTeam;

	private final Map<Class<? extends Entity>, Collection<? extends Entity>> collectionssOf = new HashMap<>();
	private final Map<Class<? extends Entity>, Map<ContinuousPoint, ? extends Entity>> positionsOf = new HashMap<>();
	private final Collection<Entity> allEntities;

	public Entities(final List<Entity> entities, final List<Wizard> myTeam, final Segment attacking, final Segment defending) {
		this.entities = entities;
		this.myTeam = myTeam;
		allEntities = new HashSet<>();
		allEntities.addAll(entities);
		allEntities.addAll(myTeam);
	}

	private <Type extends Entity> Collection<Type> buildCollectionOf(final Class<Type> type) {
		return allEntities.stream()
				.filter(e -> type.isInstance(e))
				.map(e -> type.cast(e))
				.collect(Collectors.toList());
	}

	private <Type extends Entity> Map<ContinuousPoint, Type> buildPositionsOf(final Class<Type> type) {
		return allEntities.stream()
				.filter(e -> type.isInstance(e))
				.map(e -> type.cast(e))
				.collect(Collectors.toMap(s -> s.position,
                        Function.identity()));
	}
	
	private <Type extends Entity> Collection<Type> getCollectionOf(final Class<Type> type) {
		if(!collectionssOf.containsKey(type)) {
			collectionssOf.put(type, buildCollectionOf(type));
		}
		return (Collection<Type>) collectionssOf.get(type);
	}
	
	private <Type extends Entity> Map<ContinuousPoint, Type> getPositionsOf(final Class<Type> type) {
		if(!positionsOf.containsKey(type)) {
			positionsOf.put(type, buildPositionsOf(type));
		}
		return (Map<ContinuousPoint, Type>) positionsOf.get(type);
	}

	public Collection<Snaffle> getSnaffles() {
		return getCollectionOf(Snaffle.class);
	}

	public Collection<Bludger> getBludgers() {
		return getCollectionOf(Bludger.class);
	}

	public Collection<Wizard> getAllWizards() {
		return getCollectionOf(Wizard.class);
	}

	public SortedMap<ContinuousPoint, Snaffle> sortSnafflesFor(final Wizard wizard) {
		Segment target = null;
		if(wizard.isAttacking()) {
			target = wizard.getAttackedGoal();
		} else {
			target = wizard.getDefendedGoal();
		}
		return getSnafflesSortedByDistanceTo(wizard, target);
	}

	private SortedMap<ContinuousPoint, Snaffle> getSnafflesSortedByDistanceTo(final Entity source, final Segment goal) {
		final Map<ContinuousPoint, Snaffle> snaffles = getPositionsOf(Snaffle.class);
		final Comparator<AbstractPoint> comparator = new AbstractPoint.PositionByDistance2To(goal);
		final SortedMap<ContinuousPoint, Snaffle> sorted = new TreeMap<>(comparator);
		for(final Map.Entry<ContinuousPoint, Snaffle> entry : snaffles.entrySet()) {
			sorted.put(entry.getKey(), entry.getValue());
		}
		return sorted;
	}
}
