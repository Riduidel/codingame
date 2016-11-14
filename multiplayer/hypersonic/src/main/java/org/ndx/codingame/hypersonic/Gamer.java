package org.ndx.codingame.hypersonic;

import java.util.ArrayList;
import java.util.List;

import org.ndx.codingame.hypersonic.content.Bomb;
import org.ndx.codingame.hypersonic.content.Box;
import org.ndx.codingame.hypersonic.content.Content;
import org.ndx.codingame.hypersonic.content.ContentAdapter;
import org.ndx.codingame.hypersonic.content.ContentVisitor;
import org.ndx.codingame.hypersonic.content.Item;
import org.ndx.codingame.hypersonic.content.Nothing;
import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.lib2d.discrete.ScoredDirection;

public class Gamer extends Entity {
	private static final ScoreComputer SCORE_COMPUTER = new ScoreComputer();
	private static final int BOMB_OPPORTUNITY = 1;
	private static final int ITEM_OPPORTUNITY = 4;
	private final class OpportunitiesFinder extends PlaygroundAdapter<Playground<Integer>> {
		private class OpportunitiesContentFinder extends ContentAdapter<Void> {

			@Override
			public Void visitBox(Box box) {
				for(Direction d : Direction.DIRECTIONS) {
					for (int index = 1; index <= range; index++) {
						int l_x = x+d.x*index;
						int l_y = y+d.y*index;
						if(source.contains(l_x, l_y)) {
							if(Nothing.instance.equals(source.get(l_x, l_y))) {
								returned.set(l_x, l_y, returned.get(l_x, l_y)+BOMB_OPPORTUNITY);
							} else {
								break;
							}
						} else {
							break;
						}
					}
				}
				return null;
			}
			
			@Override
			public Void visitItem(Item item) {
				returned.set(x, y, returned.get(x, y)+ITEM_OPPORTUNITY);
				return null;
			}
		}

		private OpportunitiesContentFinder contentVisitor;
		private int x;
		private int y;
		private Playfield source;
		@Override
		public void startVisit(Playfield playground) {
			this.source = playground;
			this.returned = new Playground<>(playground.width, playground.height, 0);
			this.contentVisitor = new OpportunitiesContentFinder();
		}

		@Override
		public void visit(int x, int y, Content content) {
			this.x = x;
			this.y = y;
			content.accept(contentVisitor);
		}
	}

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
		ScoredDirection best = null;
		// First, score playfield positions according to bomb opportunities
		Playground<Integer> opportunities = findOpportunities(playground);
		// then, define an action to perform (bomb or move)
		if(opportunities.get(this)>0 && bombs>0) {
			// what will happen if we bomb there ?
			Playfield withBomb = new Playfield(playground);
			withBomb.set(this, new Bomb(id, x, y, EvolvableConstants.BOMB_DELAY, range));
			best = findBestMoveIn(withBomb);
			if(best.getScore()>0) {
				return show(Action.BOMB, best);
			}
		}
		if(best==null || best.getScore()<0) {
			// If bombing is not a good idea, evaluate best mean move
			best = findBestMoveIn(playground);
		}
		return show(Action.MOVE, best);
	}
	
	private String show(Action action, ScoredDirection best) {
		return String.format("%s %d %d", action, best.x, best.y);
	}
	/**
	 * To find the best move, we will score them all over derived playground, then aggregate those moves through mean computation
	 * @param playground
	 * @return
	 */
	private ScoredDirection findBestMoveIn(Playfield playground) {
		List<Playground<Integer>> cache = new ArrayList<>();
		for (int index = 0; index <= EvolvableConstants.HORIZON; index++) {
			cache.add(new Playground<Integer>(playground.width, playground.height));
		}
		ScoredDirection best = null;
		for(Direction d : Direction.DIRECTIONS_AND_STAY) {
			ScoredDirection position = d.move(this);
			if(playground.contains(position)) {
				position.setScore(computeScoreAt(position, playground, 0, cache));
				if(best==null)
					best = position;
				else {
					if(best.getScore()<position.getScore()) {
						best = position;
					}
				}
			}
		}
		return best;
	}

	private int computeScoreAt(ScoredDirection position, Playfield playground, int i, List<Playground<Integer>> cache) {
		Playground<Integer> localCache = cache.get(i);
		if(playground.contains(position)) {
			Integer returned = localCache.get(position); 
			if(returned==null) {
				returned = playground.get(position).accept(SCORE_COMPUTER);
				if(i<EvolvableConstants.HORIZON) {
					for(Direction d : Direction.DIRECTIONS_AND_STAY) {
						returned+=computeScoreAt(d.move(position), playground.next(), i+1, cache);
					}
				}
				localCache.set(position, returned);
			}
			return returned;
		} else {
			return 0;
		}
	}
	public Playground<Integer> findOpportunities(Playfield playground) {
		// The descendant(8) allows to get 8-level next, where all other bombs should have detonated
		return playground.accept(new OpportunitiesFinder());
	}
}