package org.ndx.codingame.hypersonic;

import org.ndx.codingame.hypersonic.content.Bomb;
import org.ndx.codingame.hypersonic.content.ContentVisitor;
import org.ndx.codingame.hypersonic.content.Fire;
import org.ndx.codingame.hypersonic.content.FireThenItem;
import org.ndx.codingame.hypersonic.content.Wall;
import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.lib2d.discrete.ScoredDirection;

public class Gamer extends Entity implements OpportunitesLoader {

	public final int id;
	public final int bombs;
	public final int range;
	public Gamer(int id, int x, int y, int bombs, int range) {
		super(x, y);
		this.id = id;
		this.bombs = bombs;
		this.range = range;
	}
	@Override
	public <Type> Type accept(ContentVisitor<Type> visitor) {
		return visitor.visitGamer(this);
	}

	@Override public boolean canBeWalkedOn() { return true; }
	@Override
	public String toString() {
		return "Gamer [id=" + id + ", bombs=" + bombs + ", range=" + range + ", x=" + x + ", y=" + y + "]";
	}
	public String compute(Playfield playground) {
		ScoredDirection<Score> best = null;
		// First, score playfield positions according to bomb opportunities
		Playground<Integer> opportunities = findOpportunities(playground);
		// then, define an action to perform (bomb or move)
		if(opportunities.get(this)>0 && bombs>0) {
			// what will happen if we bomb there ?
			Playfield withBomb = new Playfield(playground);
			withBomb.set(this, new Bomb(id, x, y, EvolvableConstants.BOMB_DELAY, range));
			best = findBestMoveIn(withBomb);
			if(best.getScore().survive()) {
				return show(Action.BOMB, best);
			} else {
				// mark opportunity as BAD for the visible future
				for (int i = 0; i < EvolvableConstants.BOMB_DELAY; i++) {
					playground.descendant(i).getOpportunitiesAt(range).set(this, EvolvableConstants.SCORE_POTENTIAL_SUICIDE);
				}
			}
		}
		if(best==null || !best.getScore().survive()) {
			// If bombing is not a good idea, evaluate best mean move
			best = findBestMoveIn(playground);
		}
		return show(Action.MOVE, best);
	}
	
	private String show(Action action, ScoredDirection best) {
		return String.format("%s %d %d", action, best.x, best.y);
	}
	private ScoredDirection<Score> findBestMoveIn(Playfield playground) {
		ScoreBuilder builder = new ScoreBuilder(playground.next(), this);
		return builder.computeFor(this);
		
	}

	public Playground<Integer> findOpportunities(Playfield playground) {
		// The descendant(8) allows to get 8-level next, where all other bombs should have detonated
		return playground.getOpportunitiesAt(range);
	}
}