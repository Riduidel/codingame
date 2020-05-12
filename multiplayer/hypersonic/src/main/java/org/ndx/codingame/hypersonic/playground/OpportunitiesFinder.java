package org.ndx.codingame.hypersonic.playground;

import org.ndx.codingame.hypersonic.EvolvableConstants;
import org.ndx.codingame.hypersonic.entities.Box;
import org.ndx.codingame.hypersonic.entities.CanFire;
import org.ndx.codingame.hypersonic.entities.Content;
import org.ndx.codingame.hypersonic.entities.ContentAdapter;
import org.ndx.codingame.hypersonic.entities.FireThenItem;
import org.ndx.codingame.hypersonic.entities.Gamer;
import org.ndx.codingame.lib2d.ImmutablePlayground;
import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.lib2d.discrete.PlaygroundAdapter;

public class OpportunitiesFinder extends PlaygroundAdapter<Playground<Integer>, Content> {

	private class OpportunitiesContentFinder extends ContentAdapter<Void> {
		private void markOpportunitiesAround(final int value) {
			for(final Direction d : Direction.DIRECTIONS) {
				for (int index = 1; index < range; index++) {
					final int l_x = x+d.x*index;
					final int l_y = y+d.y*index;
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
		public Void visitBox(final Box box) {
			markOpportunitiesAround(EvolvableConstants.OPPORTUNITY_BOMB);
			return null;
		}

//		@Override
//		public Void visitItem(Item item) {
//			markOpportunitiesAround(EvolvableConstants.OPPORTUNITY_ITEM);
//			return super.visitItem(item);
//		}
		
		@Override
		public Void visitGamer(final Gamer bomber) {
			markOpportunitiesAround(EvolvableConstants.OPPORTUNITY_ENEMY);
			return null;
		}
		
		@Override
		public Void visitFireThenItem(final FireThenItem fire) {
			markOpportunitiesAround(EvolvableConstants.OPPORTUNITY_FIRE_THEN_ITEM);
			return super.visitFireThenItem(fire);
		}
	}

	private OpportunitiesContentFinder contentVisitor;
	private final int range;
	private int x;
	private int y;
	private ImmutablePlayground<Content> source;
	@Override
	public void startVisit(final ImmutablePlayground<Content> playground) {
		source = playground;
		returned = new Playground<>(playground.getWidth(), playground.getHeight(), 0);
		contentVisitor = new OpportunitiesContentFinder();
	}

	@Override
	public void visit(final int x, final int y, final Content content) {
		this.x = x;
		this.y = y;
		content.accept(contentVisitor);
	}

	public OpportunitiesFinder(final int range) {
		super();
		this.range = range;
	}
}