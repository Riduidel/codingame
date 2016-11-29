package org.ndx.codingame.fantastic.entities;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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

	private List<Entity> entities;
	private List<Wizard> myTeam;

	private Map<Class<? extends Entity>, Collection<? extends Entity>> collectionssOf = new HashMap<>();
	private Map<Class<? extends Entity>, Map<ContinuousPoint, ? extends Entity>> positionsOf = new HashMap<>();
	private Collection<Entity> allEntities;

	public Entities(List<Entity> entities, List<Wizard> myTeam, Segment attacking, Segment defending) {
		this.entities = entities;
		this.myTeam = myTeam;
		this.allEntities = new HashSet<>();
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
	
	private <Type extends Entity> Collection<Type> getCollectionOf(Class<Type> type) {
		if(!collectionssOf.containsKey(type)) {
			collectionssOf.put(type, buildCollectionOf(type));
		}
		return (Collection<Type>) collectionssOf.get(type);
	}
	
	private <Type extends Entity> Map<ContinuousPoint, Type> getPositionsOf(Class<Type> type) {
		if(!positionsOf.containsKey(type)) {
			positionsOf.put(type, buildPositionsOf(type));
		}
		return (Map<ContinuousPoint, Type>) positionsOf.get(type);
	}

	public Collection<Snaffle> getSnaffles() {
		return getCollectionOf(Snaffle.class);
	}

	public Snaffle findBestSnaffleFor(ContinuousPoint position) {
		return getPositionsOf(Snaffle.class).get(position.findNearestDistance2(getPositionsOf(Snaffle.class).keySet()));
	}

	public Collection<Bludger> getBludgers() {
		return getCollectionOf(Bludger.class);
	}

	public Collection<Wizard> getAllWizards() {
		return getCollectionOf(Wizard.class);
	}

	public Snaffle findBestSnaffleFor(Wizard wizard) {
		SortedMap<ContinuousPoint, Snaffle> snaffles = sortSnafflesFor(wizard);
		SortedMap<ContinuousPoint, Snaffle> goodOnes = snaffles.headMap(wizard.position);
		ContinuousPoint key = null;
		if(goodOnes.isEmpty()) {
			// We will have to get the first snaffle in the other side map, but please take care to remove the one already targetted
			if(snaffles.size()==1) {
				key = snaffles.firstKey();
			} else {
				// get the first non targetted one
				Iterator<ContinuousPoint> iterator = snaffles.keySet().iterator();
				Snaffle s = null;
				do {
					key = iterator.next();
					s = snaffles.get(key);
				} while(s.isATarget);
			}
		} else {
			key = goodOnes.keySet().stream()
					.sorted(new AbstractPoint.PositionByDistanceTo(wizard.position))
					.findFirst()
					.get();
			key = goodOnes.lastKey();
		}
		return snaffles.get(key);
	}

	public SortedMap<ContinuousPoint, Snaffle> sortSnafflesFor(Wizard wizard) {
		SortedMap<ContinuousPoint, Snaffle> snaffles = null;
		if(wizard.isAttacking()) {
			snaffles = getSnafflesSortedByDistanceTo(wizard.getAttackedGoal().pointAtNTimes(0.5));
		} else {
			snaffles = getSnafflesSortedByDistanceTo(wizard.getDefendedGoal().pointAtNTimes(0.5));
		}
		return snaffles;
	}

	private SortedMap<ContinuousPoint, Snaffle> getSnafflesSortedByDistanceTo(ContinuousPoint goalCenter) {
		final Map<ContinuousPoint, Snaffle> snaffles = getPositionsOf(Snaffle.class);
		SortedMap<ContinuousPoint, Snaffle> sorted = new TreeMap<>(new AbstractPoint.PositionByDistanceTo(goalCenter));
		for(Map.Entry<ContinuousPoint, Snaffle> entry : snaffles.entrySet()) {
			sorted.put(entry.getKey(), entry.getValue());
		}
		return sorted;
	}
}
