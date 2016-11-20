package org.ndx.codingame.hypersonic;

import org.ndx.codingame.hypersonic.content.Box;
import org.ndx.codingame.hypersonic.content.Content;
import org.ndx.codingame.hypersonic.content.ContentAdapter;
import org.ndx.codingame.hypersonic.content.FireThenItem;
import org.ndx.codingame.hypersonic.content.Item;
import org.ndx.codingame.hypersonic.content.Nothing;
import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.Playground;

public class OpportunitiesFinder extends PlaygroundAdapter<Playground<Integer>> {

	private class OpportunitiesContentFinder extends ContentAdapter<Void> {
		private void markOpportunitiesAround(int value) {
			for(Direction d : Direction.DIRECTIONS) {
				for (int index = 1; index < range; index++) {
					int l_x = x+d.x*index;
					int l_y = y+d.y*index;
					if(source.contains(l_x, l_y)) {
						if(source.get(l_x, l_y).canFire().equals(CanFire.YES)) {
							returned.set(l_x, l_y, returned.get(l_x, l_y)+value);
						} else {
							break;
						}
					} else {
						break;
					}
				}
			}
		}
		
		@Override
		public Void visitBox(Box box) {
			markOpportunitiesAround(EvolvableConstants.OPPORTUNITY_BOMB);
			return null;
		}

//		@Override
//		public Void visitItem(Item item) {
//			markOpportunitiesAround(EvolvableConstants.OPPORTUNITY_ITEM);
//			return super.visitItem(item);
//		}
		
		@Override
		public Void visitGamer(Gamer bomber) {
			markOpportunitiesAround(EvolvableConstants.OPPORTUNITY_ENEMY);
			return null;
		}
		
		@Override
		public Void visitFireThenItem(FireThenItem fire) {
			markOpportunitiesAround(EvolvableConstants.OPPORTUNITY_FIRE_THEN_ITEM);
			return super.visitFireThenItem(fire);
		}
	}

	private OpportunitiesContentFinder contentVisitor;
	private final int range;
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

	public OpportunitiesFinder(int range) {
		super();
		this.range = range;
	}
}