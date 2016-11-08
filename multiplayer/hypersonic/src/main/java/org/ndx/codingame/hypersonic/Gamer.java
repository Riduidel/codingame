package org.ndx.codingame.hypersonic;

import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.Playground;

public class Gamer extends Entity {
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
		// First, score playfield positions according to bomb opportunities
		Playground<Integer> opportunities = findOpportunities(playground);
		// then, define an action to perform (bomb or move)
		if(opportunities.get(this)>0) {
			// what will happen if we bomb there ?
		}
		// if bomb, simulate the bomb drop on next 8 action
		// if this result in player death, rollback bomb action
		return "MOVE 0 0";
	}
	
	public Playground<Integer> findOpportunities(Playfield playground) {
		// The descendant(8) allows to get 8-level next, where all other bombs should have detonated
		return playground.descendant(8).accept(new OpportunitiesFinder());
	}
}