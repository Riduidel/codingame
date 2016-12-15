package org.ndx.codingame.fantastic.entities;

import java.util.ArrayList;
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

import org.ndx.codingame.fantastic.actions.Action;
import org.ndx.codingame.fantastic.actions.ActionListBuilder;
import org.ndx.codingame.fantastic.actions.OrderActions;
import org.ndx.codingame.fantastic.status.Status;
import org.ndx.codingame.fantastic.status.TeamStatus;
import org.ndx.codingame.lib2d.base.AbstractPoint;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.lib2d.shapes.Segment;

public class Entities {
	private class NearestToSnaffles implements Comparator<Wizard> {

		@Override
		public int compare(final Wizard o1, final Wizard o2) {
			return (int) Math.signum(o1.position.minDistance2To(getPositionsOfSnaffles().keySet())-
					o2.position.minDistance2To(getPositionsOfSnaffles().keySet()));
		}
		
	}
	public final Status status;

	private final List<Entity> entities;
	private final List<Wizard> myTeam;

	private final Map<Class<? extends Entity>, Collection<? extends Entity>> collectionssOf = new HashMap<>();
	private final Map<Class<? extends Entity>, Map<ContinuousPoint, ? extends Entity>> positionsOf = new HashMap<>();
	private final Collection<Entity> allEntities;

	public Entities(final Status status, final List<Entity> entities, final List<Wizard> myTeam, final Segment attacking, final Segment defending) {
		this.status = status;
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
	
	private Map<ContinuousPoint, Snaffle> getPositionsOfSnaffles() {
		return getPositionsOf(Snaffle.class);
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
			target = status.get(TeamStatus.class).getAttacked();
		} else {
			target = status.get(TeamStatus.class).getDefended();
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
	
	public Collection<Entity> all() {
		return allEntities;
	}

	public List<String> computeActionsToString() {
		final Map<Wizard, Action> actions = computeActions();
		actions.values().forEach(a -> a.updateStatus(status));
		return myTeam.stream()
				.map(w -> actions.get(w))
				.map(a -> a.toCommand())
				.collect(Collectors.toList());
	}

	public Map<Wizard, Action> computeActions() {
		final Map<Wizard, List<Action>> all = buildAllActions();
		// Now we have all actions per wizard. If there is a conflict between both wizards, both first actions are eliminated and replaced by next ones
		final Map<Wizard, Action> used = resolveConflictsIn(all);
		return used;
	}

	/**
	 * Resolve conflicts between all wizards by always degrading the 
	 * @param all
	 * @return
	 */
	private Map<Wizard, Action> resolveConflictsIn(final Map<Wizard, List<Action>> all) {
		final Wizard w1 = myTeam.get(0);
		final Wizard w2 = myTeam.get(1);
		final List<Map<Wizard, Action>> possible = new ArrayList<>();
		for(final Action a : all.get(w1)) {
			for(final Action b : all.get(w2)) {
				final Map<Wizard, Action> pair = new HashMap<>();
				pair.put(w1, a);
				pair.put(w2, b);
				possible.add(pair);
			}
		}
		// Now we have paired the whole, get the valid pair with the highest score
		final List<Map<Wizard, Action>> valid = possible.stream()
				.filter(p -> !p.get(w1).conflictsWith(status, p.get(w2)))
				.collect(Collectors.toList());
		SortedMap<Double, Map<Wizard, Action>> sorted = null;
		if(valid.isEmpty()) {
			sorted = sortByAscendingScore(possible);
		} else {
			sorted = sortByAscendingScore(valid);
		}
		// Do not forget map is sorted in ascending order
		return sorted.get(sorted.lastKey());
	}

	private SortedMap<Double, Map<Wizard, Action>> sortByAscendingScore(final List<Map<Wizard, Action>> valid) {
		final SortedMap<Double, Map<Wizard, Action>> sorted = new TreeMap<>();
		for(final Map<Wizard, Action> move : valid) {
			double score = 0;
			for(final Action a : move.values()) {
				score += a.getScore();
			}
			sorted.put(score, move);
		}
		return sorted;
	}

	private Map<Wizard, List<Action>> buildAllActions() {
		return myTeam.stream()
				.collect(Collectors.toMap(Function.identity(), w -> computeActionsFor(w)));
	}

	private List<Action> computeActionsFor(final Wizard w) {
		return entities.stream()
				.flatMap(e -> e.accept(new ActionListBuilder(this, status, w)).stream())
//				.filter(a -> a.getScore()>0)
				.sorted(new OrderActions())
				.collect(Collectors.toList());
	}
}
