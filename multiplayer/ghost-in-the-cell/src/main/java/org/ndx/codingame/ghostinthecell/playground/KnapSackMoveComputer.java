package org.ndx.codingame.ghostinthecell.playground;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ndx.codingame.ghostinthecell.actions.Action;
import org.ndx.codingame.ghostinthecell.actions.MoveTo;
import org.ndx.codingame.ghostinthecell.entities.Bombs;
import org.ndx.codingame.ghostinthecell.entities.Factory;
import org.ndx.codingame.ghostinthecell.entities.Transport;
import org.ndx.codingame.libgraph.Edge;
import org.ndx.codingame.libgraph.Navigator;
import org.ndx.codingame.libgraph.Vertex;

/**
 * In knapsack problem, we consider
 * - capacity to be the number of cyborgs in given factory
 * - weight number of cyborg in factories to conquer (at the time we want to conquer them)
 * - value the production difference for the given factory : -1 if i loose it, 0 if nothing change, 
 * and 1 if I win it (all that being supposed)
 * 
 * The goal is to optimize the new production (elements like bombing, upgrade will be considered afterwards)
 * 
 * @author ndelsaux
 *
 */
public class KnapSackMoveComputer extends AbstractMoveComputer implements MoveComputer {
	public static class SolutionBuilder implements Comparable<SolutionBuilder>{
		private final Vertex vertex;
		private final Factory my;
		// Number of cyborgs required for these moves
		private final int required;
		private final Map<Edge, MoveTo> actions;
		private final int productionEarned;
		private final int maxTurn;
		public SolutionBuilder(final Vertex vertex, final Factory my, final int required,
				final int productionEarned,
				final int maxTurn,
				final Map<Edge, MoveTo> moves) {
			this.vertex = vertex;
			this.my = my;
			this.required = required;
			this.productionEarned = productionEarned;
			this.maxTurn = maxTurn;
			actions = moves;
		}
		
		public SolutionBuilder(final Vertex vertex, final Factory my, final int required) {
			this(vertex, my, required, 0, 0, new LinkedHashMap<>());
		}

		public Deque<Map<Edge, MoveTo>> findAll(final List<Edge> globalEdgesOrder) {
			final Deque<Map<Edge, MoveTo>> returned = new ArrayDeque<>();
			if(required<=my.getCount()) {
				if(globalEdgesOrder.isEmpty()) {
					returned.add(actions);
				} else {
					final Edge evaluated = globalEdgesOrder.get(0);
					List<Edge> remainingEdges;
					if(globalEdgesOrder.size()>1) {
						remainingEdges = globalEdgesOrder.subList(1, globalEdgesOrder.size());
					} else {
						remainingEdges = Collections.emptyList();
					}
					final Transport transport = Transport.of(evaluated);
					final Factory future = KnapSackMoveComputer.getFactoryAfter(Navigator.DESTINATION, evaluated);
					if(KnapSackMoveComputer.canMoveOnFactory(my.getCount()-required, evaluated)) {
						final Map<Edge, MoveTo> next = new LinkedHashMap<>(actions);
						next.put(evaluated, transport.createMoveTo(evaluated, future.getDefenders()));
						final SolutionBuilder doTakeSolution = new SolutionBuilder(vertex, my, 
								required+future.getDefenders(),
								productionEarned+future.getMaxProduction(),
								Math.max(maxTurn, transport.distance),
								next); 
						returned.addAll(doTakeSolution.findAll(remainingEdges));
					}
					if(globalEdgesOrder.size()>1) {
						final SolutionBuilder doNotTakeSolution = new SolutionBuilder(vertex, my, required,
								productionEarned+future.getMaxProduction()*future.owner,
								maxTurn,
								actions); 
						returned.addAll(doNotTakeSolution.findAll(remainingEdges));
					}
				}
			}
			return returned;
		}

		@Override
		public int compareTo(final SolutionBuilder o) {
			int returned = 0;
			returned = productionEarned-o.productionEarned;
			if(returned==0) {
				returned = o.maxTurn - maxTurn;
			}
			return 0;
		}
	}
	

	public KnapSackMoveComputer(final Playfield playfield) {
		super(playfield);
	}

	@Override
	public Stream<Action> computeMovesOf(final Vertex vertex) {
		final Factory my = Factory.of(vertex);
		final Bombs bombs = playfield.bombs;
		final Collection<Action> actions = new ArrayList<>();
		actions.addAll(buildActionsFromEdges(vertex, my, bombs));
		actions.addAll(upgrade(vertex, my));
		return actions.stream();
	}

	private Collection<? extends Action> buildActionsFromEdges(final Vertex vertex, final Factory my, final Bombs bombs) {
		final List<Edge> globalEdgesOrder = sortByGlobalOrder(vertex.getEdges(Navigator.DESTINATION));
		final Collection<Action> returned = new ArrayList<>();
		returned.addAll(fillKnapsackOf(vertex, my, bombs, globalEdgesOrder));
		if(bombs.canBomb()) {
			for(final Edge e : globalEdgesOrder) {
				final Vertex targetVertex = e.destination;
				final Transport transport = Transport.of(e);
				final Factory targetFactory = Factory.of(targetVertex);
				final List<Factory> future = targetFactory.getFuture(targetVertex);
				final Factory realTarget = future.get(transport.distance);
				if(realTarget.isEnemy()) {
					dropBomb(my, bombs, returned, e, transport, realTarget);
				}
			}
		}
		return returned;
	}

	private Collection<? extends Action> fillKnapsackOf(final Vertex vertex, final Factory my, final Bombs bombs,
			final List<Edge> globalEdgesOrder) {
		final Map<Edge, MoveTo> possible = new SolutionBuilder(vertex, my, 0).findAll(globalEdgesOrder)
				.stream()
				.findFirst()
				.orElse(Collections.emptyMap());
		return possible.entrySet().stream()
				.map((entry) -> {
					final Edge edge = entry.getKey();
					final MoveTo move = entry.getValue();
					Transport.of(edge).accountMoveOn(edge, move.count);
					return move;
				}
				)
				.collect(Collectors.toList());
	}

	public static boolean canMoveOnFactory(final int remaining, final Edge evaluated) {
		final Factory future = getFactoryAfter(Navigator.DESTINATION, evaluated);
		return !future.isMine() && remaining>getCountAt(Navigator.DESTINATION, evaluated);
	}

	public static int getCountAt(final Navigator destination, final Edge evaluated) {
		final Factory futureFactory = getFactoryAfter(destination, evaluated);
		return futureFactory.getCount();
	}

	public static Factory getFactoryAfter(final Navigator destination, final Edge evaluated) {
		final Transport transport = Transport.of(evaluated);
		final Vertex support = destination.navigateOn(evaluated);
		final Factory factory = Factory.of(support);
		final Factory futureFactory = factory.getFuture(support).get(transport.getDistance());
		return futureFactory;
	}

}
