package org.ndx.codingame.ghostinthecell.playground;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.ndx.codingame.ghostinthecell.Constants;
import org.ndx.codingame.ghostinthecell.actions.Action;
import org.ndx.codingame.ghostinthecell.entities.Bombs;
import org.ndx.codingame.ghostinthecell.entities.Factory;
import org.ndx.codingame.ghostinthecell.entities.Transport;
import org.ndx.codingame.libgraph.Edge;
import org.ndx.codingame.libgraph.Navigator;
import org.ndx.codingame.libgraph.Vertex;

public class StandardMoveComputer extends AbstractMoveComputer implements MoveComputer {
	public static int getDestinationOrder(final Edge e) {
		return Factory.of(e.destination).getOrder();
	}

	public StandardMoveComputer(final Playfield playfield) {
		super(playfield);
	}

	@Override
	public Stream<Action> computeMovesOf(final Vertex vertex) {
		final Factory my = Factory.of(vertex);
		final Bombs bombs = playfield.bombs;
		final Collection<Action> actions = new ArrayList<>();
		actions.addAll(buildActionsFromEdges(vertex, my, bombs));
		if(my.getCount()>Constants.UPGRADE_TRESHOLD) {
			if(my.production<=Constants.MAX_PRODUCTION) {
				if(my.production==my.getMaxProduction()) {
//					if(my.getFuture(vertex).get(Constants.HORIZON).isMine()) {
					actions.add(my.upgrade(vertex));
//					}
				}
			}
		}
		return actions.stream();
	}

	private Collection<Action> buildActionsFromEdges(final Vertex vertex, final Factory my, final Bombs bombs) {
		final Collection<Action> actions = new ArrayList<>();
		for(final Edge e : sortByGlobalOrder(vertex.getEdges(Navigator.DESTINATION))) {
			final Vertex targetVertex = e.destination;
			Transport transport = Transport.of(e);
			final Factory targetFactory = Factory.of(targetVertex);
			final List<Factory> future = targetFactory.getFuture(targetVertex);
			final Factory realTarget = future.get(transport.distance);
			final int count = realTarget.getCount();
			if(realTarget.isMine()) {
				actions.addAll(moveTroopToAlly(my, e, transport, targetFactory, count));
			} else {
				boolean hasBombed = false;
				// Now ATTACK !
				if(bombs.getCount()>0) {
					if(!transport.hasBomb()) {
						if(bombs.canBomb()) {
							if(count>10 && realTarget.isEnemy()) {
								hasBombed = actions.add(my.dropBomb(e, bombs));
							}
						}
					}
				}
				// do not bomb and move at the same time ! it's dangerous !
				if(!hasBombed) {
					final int remaining = my.getCount();
					if(survive(my, vertex)) {
						final int defenders = realTarget.getDefenders()+1;
						if(remaining>defenders || playfield.getMyCyborgs()>count) {
							final Vertex nearestVertex = targetFactory.getNearest(targetVertex, vertex, my);
							if(nearestVertex.equals(vertex)) {
								actions.add(transport.fireMoveOf(e, Math.min(remaining, defenders)));
							} else {
								transport = Transport.of(nearestVertex.getEdge(Navigator.SOURCE, vertex));
								actions.add(transport.fireMoveOf(e, Math.min(remaining, defenders)));
							}
						} else if(Transport.of(e).hasBomb()) {
							actions.add(transport.fireMoveOf(e, remaining/2));
						}
					}
				}
//				// Always stop after first visible enemy
//				return actions;
			}
		}
		return actions;
	}

	private Collection<Action> moveTroopToAlly(final Factory my, final Edge e,
			final Transport transport, final Factory targetFactory, final int count) {
		final Collection<Action> actions = new ArrayList<>();
		final int remaining = my.getCount();
		if(remaining>count) {
			int reinforcment = 0;
			if(my.getTeamCentrality(e.source)<targetFactory.getTeamCentrality(e.destination)) {
				reinforcment = remaining-count;
			} else {
				reinforcment =  (remaining-count)/2;
			}
			actions.add(transport.fireMoveOf(e,reinforcment));
		}
		return actions;
	}
}
