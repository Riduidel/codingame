package org.ndx.codingame.greatescape.entities;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.ndx.codingame.greatescape.actions.Trap;
import org.ndx.codingame.greatescape.playground.DistanceInfo;
import org.ndx.codingame.greatescape.playground.Playfield;
import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.lib2d.discrete.PlaygroundAdapter;
import org.ndx.codingame.lib2d.discrete.ScoredDirection;

/* Visit playfield to get all positions. 
// For each position, try to trap all enemies, eventually moving trap to the right location.
// Then compute score for trap according to combined distances. Do not forget to minus by my
// own distance (I don't want to run on my own walls)!
*/
public class TrapGenerator extends PlaygroundAdapter<Trap, GameElement>{
	private static class TrapCollector extends PlaygroundAdapter<SortedSet<Trap>, Trap> {
		@Override
		public void startVisit(final Playground<Trap> playground) {
			returned = new TreeSet<>();
		}
		
		@Override
		public void visit(final int x, final int y, final Trap content) {
			if(content!=null) {
				returned.add(content);
			}
		}
	}

	private final Gamer me;
	private final List<Gamer> gamers;
	private Playground<Trap> temporary;
	private Playfield tested;

	public TrapGenerator(final List<Gamer> gamers, final Gamer me) {
		this.me = me;
		this.gamers = gamers;
	}

	@Override
	public void startVisit(final Playground<GameElement> playground) {
		tested = (Playfield) playground;
		temporary = new Playground<>(playground.width, playground.height);
	}

	/**
	 * Now we have aggregated all traps, let's build a sorted set of them
	 */
	@Override
	public Trap endVisit(final Playground<GameElement> playground) {
		final SortedSet<Trap> allTraps = temporary.accept(new TrapCollector());
		return allTraps.stream()
				.filter((t) -> t.putSomewhereValid(tested))
				.findFirst()
				.get();
	}
	
	@Override
	public void visit(final int x, final int y, final GameElement content) {
		if(x%2==0 && y%2==0) {
			final DiscretePoint position = new DiscretePoint(x, y);
			for(final Gamer g : gamers) {
				final DistanceInfo info = g.getDistanceMap().get(x, y);
				if(info!=null) {
					if(!g.equals(me)) {
						final int distanceBetweenPoints = (int) position.distance1To(g.toPlayfieldPosition());
						// this is a hack to only get best path
						final int localScore = Math.max(2*info.getDistance() - distanceBetweenPoints, 0);
						incrementScoresAt(g, position, localScore, info.getDistance());
					}
				}
			}
		}
	}

	private void incrementScoresAt(final Gamer g, final DiscretePoint position, final int localScore, final int distance) {
		for(final Direction d : Direction.DIRECTIONS) {
			final ScoredDirection<Object> inWall = d.move(position);
			// Notice that if playground contains wall, it ALSO contains the cell on the other wall side
			if(tested.contains(inWall)) {
				if(!Wall.class.isInstance(tested.get(inWall))) {
					final ScoredDirection<Object> onGround = d.move(inWall);
					final DistanceInfo distanceInfo = g.getDistanceMap().get(onGround);
					if(distanceInfo==null || distanceInfo.getDistance()<distance) {
						addTrapAt(position, inWall, localScore);
					}
				}
			}
		}
	}

	private void addTrapAt(final DiscretePoint position, final ScoredDirection<Object> inWall, final int localScore) {
		Trap t = temporary.get(inWall);
		if(t==null) {
			final Orientation orientation = position.x==inWall.x ? Orientation.H : Orientation.V;
			t = new Trap(orientation, new DiscretePoint(Math.min((inWall.x+1)/2, tested.width/2-2), Math.max((inWall.y+1)/2, 1)));
			temporary.set(inWall, t);
		}
		t.addScore(localScore);
	}
}