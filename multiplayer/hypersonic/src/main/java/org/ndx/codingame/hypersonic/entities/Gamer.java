package org.ndx.codingame.hypersonic.entities;

import java.util.Collection;

import org.ndx.codingame.hypersonic.EvolvableConstants;
import org.ndx.codingame.hypersonic.actions.Action;
import org.ndx.codingame.hypersonic.playground.OpportunitesLoader;
import org.ndx.codingame.hypersonic.playground.Playfield;
import org.ndx.codingame.hypersonic.score.Score;
import org.ndx.codingame.hypersonic.score.ScoreBuilder;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.lib2d.discrete.ScoredDirection;

public class Gamer extends Entity implements OpportunitesLoader {

	public final int id;
	public final int bombs;
	public final int range;
	public Gamer(final int id, final int x, final int y, final int bombs, final int range) {
		super(x, y);
		this.id = id;
		this.bombs = bombs;
		this.range = range;
	}
	@Override
	public <Type> Type accept(final ContentVisitor<Type> visitor) {
		return visitor.visitGamer(this);
	}

	@Override public boolean canBeWalkedOn() { return true; }
	@Override
	public String toString() {
		return "Gamer [id=" + id + ", bombs=" + bombs + ", range=" + range + ", x=" + x + ", y=" + y + "]";
	}
	public String compute(final Playfield playground) {
		// Then let enemies drop bombs (otherwise opportunities will be incorrectly computed : enemies will be replaced by ininteresting bombs)
		letEnemiesDropBombs(playground);
		// First, score playfield positions according to bomb opportunities
		final Playground<Integer> opportunities = findOpportunities(playground);
		ScoredDirection<Score> best = null;
		// then, define an action to perform (bomb or move)
		if(opportunities.get(this)>0 && bombs>0) {
			System.err.println(String.format("I can plant a bomb (opportunites at %s, %s contains %s and I have %s bombs)", x, y, opportunities.get(this), bombs));
			// what will happen if we bomb there ?
			final Playfield withBomb = new Playfield(playground);
			withBomb.set(this, new VirtualBomb(id, x, y, EvolvableConstants.BOMB_DELAY, range));
			best = findBestMoveIn(withBomb);
			if(best.getScore().survive()) {
				System.err.println("And I will survive");
				return show(Action.BOMB, best);
			} else {
				System.err.println("But it will kill me");
				// mark opportunity as BAD for the visible future
				for (int i = 0; i < EvolvableConstants.BOMB_DELAY; i++) {
					playground.descendant(i).getOpportunitiesAt(range).set(this, EvolvableConstants.SCORE_POTENTIAL_SUICIDE);
				}
			}
		} else {
			System.err.println("I have no reason to throw any bomb");
		}
		if(best==null || !best.getScore().survive()) {
			System.err.println("Let's try a good move");
			// If bombing is not a good idea, evaluate best mean move
			best = findBestMoveIn(playground);
		}
		return show(Action.MOVE, best);
	}
	
	private void letEnemiesDropBombs(final Playfield playground) {
		final Collection<Gamer> all = playground.accept(new GamerFinder());
		for(final Gamer g : all) {
			playground.set(g, new VirtualBomb(g.id, g.x, g.y, EvolvableConstants.BOMB_DELAY, g.range));
		}
	}
	private String show(final Action action, final ScoredDirection<Score> best) {
		return String.format("%s %d %d", action, best.x, best.y);
	}
	private ScoredDirection<Score> findBestMoveIn(final Playfield playground) {
		final Playfield scored = playground.next();
		final ScoreBuilder builder = new ScoreBuilder(scored, this);
		return builder.computeFor(scored, this);
		
	}

	@Override
	public Playground<Integer> findOpportunities(final Playfield playground) {
		// The descendant(8) allows to get 8-level next, where all other bombs should have detonated
		return playground.getOpportunitiesAt(range);
	}
}