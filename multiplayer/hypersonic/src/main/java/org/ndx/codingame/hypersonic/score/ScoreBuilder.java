package org.ndx.codingame.hypersonic.score;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.ndx.codingame.hypersonic.EvolvableConstants;
import org.ndx.codingame.hypersonic.entities.ContentAdapter;
import org.ndx.codingame.hypersonic.entities.Item;
import org.ndx.codingame.hypersonic.entities.Nothing;
import org.ndx.codingame.hypersonic.playground.OpportunitesLoader;
import org.ndx.codingame.hypersonic.playground.Playfield;
import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.lib2d.discrete.ScoredDirection;

public class ScoreBuilder {
	private final Playground<List<Direction>> directions;
	private final OpportunitesLoader opportunitiesLoader;
	private final List<Map<Integer, PlaygroundScoreBuilder>> builders = new ArrayList<>();
//	private final List<Map<List<Item>, PlaygroundScoreBuilder>> builders = new ArrayList<>();

	/**
	 * Build the score builder graph from given playground and opportunities loader
	 * @param playground
	 * @param opportunitiesLoader
	 */
	public ScoreBuilder(final Playfield playground, final OpportunitesLoader opportunitiesLoader) {
		this.opportunitiesLoader = opportunitiesLoader;
		directions = new Playground<>(playground.width, playground.height);
		contributeToBuildersFrom(playground, 0);
	}
	
	private void contributeToBuildersFrom(final Playfield playground, final int iteration) {
		Playfield current = playground;
		for (int index = iteration; index <= EvolvableConstants.HORIZON; index++) {
			while(builders.size()<=index) {
				builders.add(new HashMap<>());
			}
			final List<Item> key = current.getAll(Item.class);
			builders.get(index).put(key.size(), new PlaygroundScoreBuilder(this, current, index));
			current = current.next();
		}
	}

	public List<Direction> getDirectionsFor(final DiscretePoint point) {
		if(directions.get(point)==null) {
			directions.set(point, createDirectionsFor(point));
		}
		return directions.get(point);
	}

	private List<Direction> createDirectionsFor(final DiscretePoint point) {
		List<Direction> returned = directions.get(point);
		if(returned==null) {
			returned = new ArrayList<>();
			if(point.x<directions.width/2) {
				returned.add(Direction.RIGHT);
				returned.add(Direction.LEFT);
			} else {
				returned.add(Direction.LEFT);
				returned.add(Direction.RIGHT);
			}
			if(point.y<directions.height/2) {
				returned.add(Direction.DOWN);
				returned.add(Direction.UP);
			} else {
				returned.add(Direction.UP);
				returned.add(Direction.DOWN);
			}
			returned.add(Direction.STAY);
			directions.set(point, returned);
		}
		return returned;
	}

	public ScoredDirection<Score> computeFor(final Playfield source, final DiscretePoint point) {
		final ScoredDirection<Score> computed = computeFor(0, source.getAll(Item.class), point);
		if(!computed.getScore().survive()) {
			return computed;
		} else {
			return computed.getScore().bestChild;
}
	}

	private ScoredDirection<Score> computeFor(final int iteration, final List<Item> all, final DiscretePoint point) {
		return builders.get(iteration).get(all.size()).computeFor(point);
	}

	public Playground<Integer> computeOpportunitiesFor(final Playfield playground) {
		return opportunitiesLoader.findOpportunities(playground);
	}

	public ScoredDirection<Score> computeNextFor(final Playfield next, final ScoredDirection<Score> move, final int i) {
		List<Item> key = next.getAll(Item.class);
		key = next.get(move).accept(new ContentAdapter<List<Item>>(key) {
			@Override
			public List<Item> visitItem(final Item item) {
				// key is imutable by default, so filter it out
				return returnedFromContent.stream().filter(i -> !item.equals(i)).collect(Collectors.toList());
			}
		});
		final Map<Integer, PlaygroundScoreBuilder> potentialScorers = builders.get(i);
		final int keySize = key.size();
		if(!potentialScorers.containsKey(keySize)) {
			// now duplicate playground, replace item by nothing, and build its inheritence
			final Playfield duplicate = new Playfield(next);
			duplicate.set(move, Nothing.instance);
			contributeToBuildersFrom(duplicate, i);
		}
		return potentialScorers.get(keySize).computeFor(move);
	}

}
