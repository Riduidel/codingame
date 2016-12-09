package org.ndx.codingame.hypersonic.playground;

import org.ndx.codingame.hypersonic.entities.Bomb;
import org.ndx.codingame.hypersonic.entities.Box;
import org.ndx.codingame.hypersonic.entities.CanFire;
import org.ndx.codingame.hypersonic.entities.Content;
import org.ndx.codingame.hypersonic.entities.ContentVisitor;
import org.ndx.codingame.hypersonic.entities.Fire;
import org.ndx.codingame.hypersonic.entities.FireThenItem;
import org.ndx.codingame.hypersonic.entities.Gamer;
import org.ndx.codingame.hypersonic.entities.Item;
import org.ndx.codingame.hypersonic.entities.Nothing;
import org.ndx.codingame.hypersonic.entities.Wall;
import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.lib2d.discrete.PlaygroundAdapter;

public class PlaygroundDeriver extends PlaygroundAdapter<Playfield, Content> {
		public class PlaygroundCellDeriver implements ContentVisitor<Void> {
			public int x;
			public int y;
			@Override public Void visitNothing(final Nothing nothing) {
				final Content alreadyWritten = derived.get(x, y);
				if(!Fire.instance.equals(alreadyWritten)) {
					derived.set(x, y, nothing);
				}
				return null;
			}
			@Override
			public Void visitBox(final Box box) {
				final Content derivedContent = derived.get(x, y);
				if(Fire.instance.equals(derivedContent) || FireThenItem.instance.equals(derivedContent)) {
					derived.set(x, y, FireThenItem.instance);
				} else {
					derived.set(x, y, box);
				}
				return null;
			}
			@Override
			public Void visitWall(final Wall wall) {
				derived.set(x, y, wall);
				return null;
			}
			@Override
			public Void visitGamer(final Gamer gamer) {
				derived.set(x, y, gamer);
				return null;
			}
			@Override
			public Void visitItem(final Item item) {
				if(!Fire.instance.equals(derived.get(x, y))) {
					derived.set(x, y, item);
				}
				return null;
			}
			@Override
			public Void visitFire(final Fire fire) {
				if(!Fire.instance.equals(derived.get(x, y))) {
					derived.set(x, y, Nothing.instance);
				}
				return null;
			}
			@Override
			public Void visitFireThenItem(final FireThenItem fire) {
				derived.set(x, y, new Item(0, x, y, 0, 0));
				return null;
			}
			@Override
			public Void visitBomb(final Bomb bomb) {
				if(bomb.delay>1) {
					if(Fire.instance.equals(derived.get(x, y))) {
						fireBomb(bomb);
					} else {
						derived.set(x, y, new Bomb(bomb.owner, bomb.x, bomb.y, bomb.delay-1, bomb.range));
					}
				} else {
					// Fire that bomb !
					fireBomb(bomb);
				}
				// We may change coordinates to follow explosion, so reset them here
				x = bomb.x;
				y = bomb.y;
				return null;
			}
			private void fireBomb(final Bomb bomb) {
				derived.set(bomb.x, bomb.y, Fire.instance);
				for(final Direction d : Direction.DIRECTIONS) {
					fireBombInDirection(bomb, d);
				}
			}
			private void fireBombInDirection(final Bomb bomb, final Direction d) {
				for (int extension = 1; extension < bomb.range; extension++) {
					final int p_x = bomb.x+d.x*extension;
					final int p_y = bomb.y+d.y*extension;
					if(source.contains(p_x, p_y)) {
						final CanFire canFire = source.get(p_x, p_y).canFire();
						if(CanFire.YES.equals(canFire)||CanFire.END_PROPAGATION.equals(canFire)) {
							x = p_x;
							y = p_y;
							if(!Fire.instance.equals(derived.get(p_x, p_y))) {
								derived.set(p_x, p_y, Fire.instance);
								source.get(p_x, p_y).accept(this);
							}
						}
						if(CanFire.NOT.equals(canFire)||CanFire.END_PROPAGATION.equals(canFire)) {
							break;
						}
					} else {
						break;
					}
				}
			}
		}
		private final PlaygroundCellDeriver cellDeriver = new PlaygroundCellDeriver();
		public Playground<Content> source;
		public Playfield derived;
		@Override
		public void startVisit(final Playground<Content> playground) {
			source = playground;
			returned = derived = new Playfield(playground.width, playground.height);
		}
		@Override
		public void visit(final int x, final int y, final Content content) {
			cellDeriver.x = x;
			cellDeriver.y = y;
			content.accept(cellDeriver);
		}
		@Override
		public Playfield endVisit(final Playground<Content> playground) {
			return super.endVisit(playground);
		}
	}