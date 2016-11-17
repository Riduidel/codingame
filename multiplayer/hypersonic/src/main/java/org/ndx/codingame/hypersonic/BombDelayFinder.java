package org.ndx.codingame.hypersonic;

import org.ndx.codingame.hypersonic.content.Bomb;
import org.ndx.codingame.hypersonic.content.Content;
import org.ndx.codingame.hypersonic.content.ContentAdapter;
import org.ndx.codingame.hypersonic.content.ContentVisitor;

public class BombDelayFinder extends PlaygroundAdapter<Integer> {
	private class BombDelayContentFinder extends ContentAdapter<Integer> {

		public BombDelayContentFinder() {
			super(0);
		}
		
		@Override
		public Integer visitBomb(Bomb bomb) {
			if(bomb.owner==ownerId) {
				return bomb.delay;
			} else {
				return super.visitBomb(bomb);
			}
		}
	}
	final int ownerId; 
	private ContentVisitor<Integer> contentVisitor;
	public BombDelayFinder(int ownerId) {
		super(0);
		this.ownerId = ownerId;
		 contentVisitor = new BombDelayContentFinder();
	}
	@Override
	public void visit(int x, int y, Content content) {
		int cellValue = content.accept(contentVisitor);
		if(cellValue>0) {
			if(returned==0)
				returned = cellValue;
			else
				returned = Math.min(cellValue, returned);
		}
	}
}