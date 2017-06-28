package org.ndx.codingame.wondevwoman.playground;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ndx.codingame.gaming.tounittest.ToUnitTestFiller;
import org.ndx.codingame.gaming.tounittest.ToUnitTestHelpers;
import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.wondevwoman.Constants;
import org.ndx.codingame.wondevwoman.actions.B;
import org.ndx.codingame.wondevwoman.actions.BuildDirection;
import org.ndx.codingame.wondevwoman.actions.DirectionMapping;
import org.ndx.codingame.wondevwoman.actions.WonderAction;
import org.ndx.codingame.wondevwoman.entities.Gamer;

public class Gaming implements ToUnitTestFiller {
	public static Gaming from(final List<String> rows) {
		return new Gaming().withPlayfield(Playfield.from(rows));
	}

	public static Gaming from(final String...rows) {
		return new Gaming().withPlayfield(Playfield.from(rows));
	}

	private static Map<DiscretePoint, Gamer> toGamerMap(final Collection<Gamer> gamers) {
		// Notice we use a linkedhashmap to guarantee that key order is consistent with gamers order
		final Map<DiscretePoint, Gamer> returned = new LinkedHashMap<>();
		for(final Gamer g : gamers) {
			returned.put(g.position, g);
		}
		return returned;
	}

	protected List<WonderAction> actions;

	protected Map<DiscretePoint, Gamer> enemy = new LinkedHashMap<>();

	protected Map<DiscretePoint, Gamer> myByPosition = new LinkedHashMap<>();

	protected Playfield playfield;

	@Override
	public StringBuilder build(final String effectiveCommand) {
		final StringBuilder returned = new StringBuilder();
		returned.append(ToUnitTestHelpers.declaredFilledContainer(ToUnitTestHelpers.CONTENT_PREFIX, myByPosition.values(), List.class,
				Gamer.class, "my"));
		returned.append(ToUnitTestHelpers.declaredFilledContainer(ToUnitTestHelpers.CONTENT_PREFIX, enemy.values(), List.class,
				Gamer.class, "enemy"));
		returned.append(ToUnitTestHelpers.declaredFilledContainer(ToUnitTestHelpers.CONTENT_PREFIX, actions, List.class,
				WonderAction.class, "actions"));
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("Gaming g = Gaming.from(\n");
		returned.append(playfieldToString());
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append(");\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("g.withMy(my)\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("\t.withEnemy(enemy)\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("\t.withActions(actions);\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX)
		.append("assertThat(g.computeMoves()).isNotEqualTo(\"")
		.append(effectiveCommand.replace(
				"\n",
				"\\n\"+\n"+ToUnitTestHelpers.CONTENT_PREFIX+"\""))
		.append("\");\n");
		return returned;
	}

	public List<WonderAction> computeAvailableActions() {
		return computeAvailableActions(myByPosition.values());
	}

	private List<WonderAction> computeAvailableActions(final Collection<Gamer> my) {
		final List<WonderAction> returned = new ArrayList<>();
		for(final Gamer g : my) {
			returned.addAll(computeAvailableActions(g));
		}
		return returned;
	}

	/**
	 * Browse all directions around gamer, and all directions around them, looking for
	 * elements with a floor around gamer height
	 * @param g
	 * @return
	 */
	private Collection<? extends WonderAction> computeAvailableActions(final Gamer gamer) {
		final List<WonderAction> returned = new ArrayList<>();
		final int height = playfield.getHeightOf(gamer);
		for(final Direction d : DirectionMapping.DIRECTIONS_TO_NAMES.keySet()) {
			final DiscretePoint moved = d.move(gamer.position);
			if(playfield.contains(moved)) {
				// playfield contains direction, but is it reachable (not too high, not too low)
				final int movedHeight = playfield.get(moved).getHeight();
				// ok, we're on a reachable floor
				if(movedHeight<=height+1 && movedHeight<=Constants.MAX_FLOOR && movedHeight>=0) {
					// is there an ally ?
					if(myByPosition.containsKey(moved)) {
						// Is there an enemy
					} else if(enemy.containsKey(moved)) {
						returned.addAll(computeAvailableActions(moved, B.g(gamer).p(d)));
					} else {
						returned.addAll(computeAvailableActions(moved, B.g(gamer).m(d)));
					}
				}
			}
		}
		return returned;
	}

	private Collection<? extends WonderAction> computeAvailableActions(final DiscretePoint position, final BuildDirection m) {
		final List<WonderAction> returned = new ArrayList<>();
		for(final Direction d : DirectionMapping.DIRECTIONS_TO_NAMES.keySet()) {
			final DiscretePoint moved = d.move(position);
			if(playfield.contains(moved)) {
				final int movedHeight = playfield.get(moved).getHeight();
				if(movedHeight<=Constants.MAX_FLOOR) {
					returned.add(m.b(d));
				}
			}
		}
		return returned;
	}

	public String computeMoves() {
		final List<GamingStep> futures = computeFutures(actions);
		// Now create future trajectories from those initial steps
		List<GamingTrajectory> trajectories = toTrajectories(futures);
		int count = 0;
		do {
			final List<GamingTrajectory> next = new ArrayList<>();
			for(final GamingTrajectory t : trajectories) {
				next.addAll(t.extend());
			}
			trajectories = next;
			count++;
		} while(count<Constants.HORIZON);
		Collections.sort(trajectories);

		final int lastIndex = trajectories.size()-1;
		final GamingTrajectory best = trajectories.get(lastIndex);
		return best.toCommandString();
	}

	private List<GamingTrajectory> toTrajectories(final List<GamingStep> futures) {
		final List<GamingTrajectory> returned = new ArrayList<>();
		for(final GamingStep step : futures) {
			returned.add(new GamingTrajectory(step));
		}
		return returned;
	}

	private List<GamingStep> computeFutures(final List<WonderAction> actions) {
		final List<GamingStep> returned = new ArrayList<>();
		for(final WonderAction action : actions) {
			returned.add(deriveForAction(action));
		}
		return returned;
	}

	protected GamingStep deriveForAction(final WonderAction action) {
		final GamingStep future = new GamingStep()
				.withMy(myByPosition.values())
				.withEnemy(enemy.values())
				.withPlayfield(new Playfield(playfield));
		future.applyAction(action);
		return future;
	}

	private String playfieldToString() {
		return playfield.toString();
	}

	public Gaming withActions(final List<WonderAction> actions) {
		this.actions = actions;
		return this;
	}

	public Gaming withEnemy(final Collection<Gamer> enemy) {
		this.enemy = toGamerMap(enemy);
		return this;
	}

	public Gaming withMy(final Collection<Gamer> my) {
		this.myByPosition = toGamerMap(my);
		return this;
	}

	public Gaming withPlayfield(final List<String> rows) {
		return withPlayfield(Playfield.from(rows));
	}

	protected Gaming withPlayfield(final Playfield from) {
		playfield = from;
		return this;
	}

	/**
	 * Output a very short view of playfield with gamers overlay
	 */
	@Override
	public String toString() {
		return playfield.toDebugString(myByPosition, enemy);
	}

	protected Gamer findMyAt(final int gamerIndex) {
		for(final Gamer g : myByPosition.values()) {
			if(g.index==gamerIndex) {
				return g;
			}
		}
		throw new UnsupportedOperationException("Il n'y a pas de joueur à l'index "+gamerIndex);
	}
}