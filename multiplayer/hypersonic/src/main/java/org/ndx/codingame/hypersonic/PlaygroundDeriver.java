package org.ndx.codingame.hypersonic;

public class PlaygroundDeriver extends PlaygroundAdapter<Playground> {
		public class PlaygroundCellDeriver implements ContentVisitor<Void> {
			public int x;
			public int y;
			@Override public Void visitNothing(Nothing nothing) {
				Content alreadyWritten = derived.get(x, y);
				if(!Fire.instance.equals(alreadyWritten) && !BombDanger.instance.equals(alreadyWritten))
					derived.set(x, y, nothing);
				return null;
			}
			@Override public Void visitBombDanger(BombDanger danger) {
				if(!Fire.instance.equals(derived.get(x, y)))
					derived.set(x, y, danger);
				return null;
			}
			@Override
			public Void visitBox(Box box) {
				Content derivedContent = derived.get(x, y);
				if(Fire.instance.equals(derivedContent) || FireThenItem.instance.equals(derivedContent))
					derived.set(x, y, FireThenItem.instance);
				else
					derived.set(x, y, box);
				return null;
			}
			@Override
			public Void visitWall(Wall wall) {
				derived.set(x, y, wall);
				return null;
			}
			@Override
			public Void visitGamer(Gamer gamer) {
				derived.set(x, y, gamer);
				return null;
			}
			@Override
			public Void visitItem(Item item) {
				if(!Fire.instance.equals(derived.get(x, y)))
				derived.set(x, y, item);
				return null;
			}
			@Override
			public Void visitFire(Fire fire) {
				derived.set(x, y, Nothing.instance);
				return null;
			}
			@Override
			public Void visitFireThenItem(FireThenItem fire) {
				derived.set(x, y, new Item(0, x, y, 0, 0));
				return null;
			}
			@Override
			public Void visitBomb(Bomb bomb) {
				if(bomb.delay>1) {
					if(Fire.instance.equals(derived.get(x, y))) {
						fireBomb(bomb);
					} else {
						derived.set(x, y, new Bomb(bomb.owner, bomb.x, bomb.y, bomb.delay-1, bomb.range));
						// Do not forget to extend bomb with danger
						int dangerZone = bomb.range;
//						int dangerZone = Math.min(bomb.delay, bomb.range);
						for (int directionIndex = 0; directionIndex < Player.DIRECTIONS.length; directionIndex++) {
							int[] direction = Player.DIRECTIONS[directionIndex];
							for (int extension = 1; extension <= dangerZone; extension++) {
								int p_x = bomb.x+direction[0]*extension;
								int p_y = bomb.y+direction[1]*extension;
								if(source.contains(p_x, p_y)) {
									if(Nothing.instance.equals(source.get(p_x, p_y))) {
										derived.set(p_x, p_y, BombDanger.instance);
									} else {
										break;
									}
								} else {
									break;
								}
							}
						}
					}
				} else {
					// Fire that bomb !
					fireBomb(bomb);
				}
				// We may change coordinates to follow explosion, so reset them here
				this.x = bomb.x;
				this.y = bomb.y;
				return null;
			}
			private void fireBomb(Bomb bomb) {
				derived.set(bomb.x, bomb.y, Fire.instance);
				for (int i = 0; i < Player.DIRECTIONS.length; i++) {
					int[] direction = Player.DIRECTIONS[i];
					for (int extension = 1; extension <= bomb.range; extension++) {
						int p_x = bomb.x+direction[0]*extension;
						int p_y = bomb.y+direction[1]*extension;
						if(source.contains(p_x, p_y)) {
							CanFire canFire = source.get(p_x, p_y).canFire();
							if(CanFire.YES.equals(canFire)||CanFire.END_PROPAGATION.equals(canFire)) {
								this.x = p_x;
								this.y = p_y;
								if(!Fire.instance.equals(derived.get(p_x, p_y))) {
									source.get(p_x, p_y).accept(this);
									derived.set(p_x, p_y, Fire.instance);
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
		}
		private PlaygroundCellDeriver cellDeriver = new PlaygroundCellDeriver();
		public Playground source;
		public Playground derived;
		@Override
		public void startVisit(Playground playground) {
			source = playground;
			returned = derived = new Playground(playground.width, playground.height);
		}
		@Override
		public void visit(int x, int y, Content content) {
			cellDeriver.x = x;
			cellDeriver.y = y;
			content.accept(cellDeriver);
		}
		@Override
		public Playground endVisit(Playground playground) {
			return super.endVisit(playground);
		}
	}