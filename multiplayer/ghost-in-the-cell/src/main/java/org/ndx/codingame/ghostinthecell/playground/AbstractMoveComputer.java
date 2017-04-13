package org.ndx.codingame.ghostinthecell.playground;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ndx.codingame.gaming.actions.Action;
import org.ndx.codingame.ghostinthecell.Constants;
import org.ndx.codingame.ghostinthecell.entities.Bombs;
import org.ndx.codingame.ghostinthecell.entities.Factory;
import org.ndx.codingame.ghostinthecell.entities.Transport;
import org.ndx.codingame.libgraph.Edge;
import org.ndx.codingame.libgraph.Vertex;

public abstract class AbstractMoveComputer implements MoveComputer {
	protected final Playfield playfield;

	public AbstractMoveComputer(final Playfield playfield) {
		super();
		this.playfield = playfield;
	}

	@Override
	public Collection<Action> compute() {
		final List<Vertex> orderedVertices = playfield.graph.vertices().stream()
				.sorted(Factory.BY_DECREASING_DISTANCE_DIFFERENCE)
				.map((v) -> {
					Factory.of(v).computeState();
					return v;
				})
				.collect(Collectors.toList());
		// Now evaluate them in order
		int index = 0;
		for (final Vertex v : orderedVertices) {
			Factory.of(v).setOrder(index++);
		}
		return orderedVertices.stream().filter((v) -> Factory.of(v).isMine()).flatMap(this::computeMovesOf)
				.collect(Collectors.toList());
	}

	public abstract Stream<Action> computeMovesOf(final Vertex vertex);

	public boolean survive(final Factory my, final Vertex vertex) {
		final List<Factory> future = my.getFuture(vertex);
		return future.get(Constants.HORIZON).isMine();
	}

	public List<Edge> sortByGlobalOrder(final Collection<Edge> edges) {
		return edges.stream().filter((e) -> Factory.of(e.destination).getProduction() > 0)
				.sorted(Comparator.comparing(StandardMoveComputer::getDestinationOrder)).collect(Collectors.toList());
	}

	protected Collection<Action> upgrade(final Vertex vertex, final Factory my) {
		final Collection<Action> actions = new ArrayList<>();
		if (my.getCount() > Constants.UPGRADE_TRESHOLD) {
			if (my.getProduction() <= Constants.MAX_PRODUCTION) {
				if (my.getProduction() == my.getMaxProduction()) {
					// if(my.getFuture(vertex).get(Constants.HORIZON).isMine())
					// {
					actions.add(my.upgrade(vertex));
					// }
				}
			}
		}
		return actions;
	}

	protected boolean dropBomb(final Factory my, final Bombs bombs, final Collection<Action> actions, final Edge e,
			final Transport transport, final Factory realTarget) {
		if (bombs.canBomb()) {
			if (!transport.hasBomb()) {
				if (realTarget.getCount() > 10 && realTarget.isEnemy() && realTarget.getProduction()>0) {
					actions.add(my.dropBomb(e, bombs));
					return true;
				}
			}
		}
		return false;
	}
}
