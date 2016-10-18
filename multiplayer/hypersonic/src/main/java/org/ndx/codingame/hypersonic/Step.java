package org.ndx.codingame.hypersonic;

public class Step extends Entity {
	private final class BombScoreComputer extends ContentAdapter<Integer> {
		private EvolvableConstants constants;

		private BombScoreComputer(EvolvableConstants contants) {
			super(0);
			this.constants = contants;
		}

		@Override public Integer visitBomb(Bomb bomb) { return constants.SCORE_START_BOMB_CHAIN; }

		@Override public Integer visitBox(Box box) { return constants.SCORE_EXPLODE_BOX; }

		@Override public Integer visitGamer(Gamer bomber) { 
			if(bomber==Step.this.gamer) {
				return constants.SCORE_SUICIDE;
			} else {
				return constants.SCORE_KILL_ENEMY;
			}
		}

		@Override public Integer visitItem(Item item) { return constants.SCORE_BURNT_ITEM; }
		
		@Override public Integer visitFire(Fire fire) {return constants.SCORE_SUICIDE; }
		
		@Override public Integer visitFireThenItem(FireThenItem fire) { return constants.SCORE_SUICIDE; }
	}

	private final class MoveScoreComputer extends ContentAdapter<Integer> {
		private EvolvableConstants constants;

		private MoveScoreComputer(EvolvableConstants constants) {
			super(constants.SCORE_BAD_MOVE);
			this.constants = constants;
		}
		
		@Override public Integer visitNothing(Nothing nothing) {
			// Lower that count by the number of evasive actions
			int noExit = 4;
			for (int directionIndex = 0; directionIndex < Player.DIRECTIONS.length; directionIndex++) {
				int[] direction = Player.DIRECTIONS[directionIndex];
				int p_x = current.x+direction[0];
				int p_y = current.x+direction[1];
				if(playground.contains(p_x, p_y)) {
					if(playground.get(p_x,p_y).canBeWalkedOn()) {
						noExit--;
					}
				}
				
			}
			return constants.SCORE_ON_THE_ROAD+noExit*constants.SCORE_NO_EVASION_MALUS;
		}
		@Override public Integer visitBombDanger(BombDanger nothing) { return constants.SCORE_ENDANGERED; }

		@Override public Integer visitGamer(Gamer bomber) { return 0; }

		@Override public Integer visitItem(Item item) { 
			return constants.SCORE_CATCHED_ITEM;
		}
		
		@Override public Integer visitFire(Fire fire) {return constants.SCORE_SUICIDE; }
		
		@Override public Integer visitFireThenItem(FireThenItem fire) { return constants.SCORE_SUICIDE; }
	}

	public int score = 0;
	public final Entity current;
	public Playground playground;
	public final Action action;
	public final Gamer gamer;

	public Step(Action action, int x, int y, Gamer gamer, Entity current) {
		super(x, y);
		this.action = action;
		this.gamer = gamer;
		this.current = current;
	}

	public String toCommandString() {
		return String.format("%s %d %d", action, x, y);
	}

	public Step computeScore(Playground source, EvolvableConstants constants) {
		int evaluatedScore = 0;
		// now we have a playground, compute score according to action
		switch(action) {
		case BOMB: 
			playground = source.next(this);
			evaluatedScore += computeScoreForBombAction(constants);
			break;
		case MOVE:
			playground = source.next();
			evaluatedScore += computeScoreForMoveAction(constants);
			break;
		}
		score = evaluatedScore;
		return this;
	}

	private Integer computeScoreForMoveAction(EvolvableConstants constants) {
		return playground.get(x, y).accept(new MoveScoreComputer(constants));
	}

	private int computeScoreForBombAction(EvolvableConstants constants) {
		int evaluatedScore = constants.SCORE_DROP_BOMB_TAX;
		Playground iterative, 
			next;
		iterative = next = playground;
		int time = 0;
		while(!Fire.instance.equals(next.get(current.x, current.y)) && time<10) {
			iterative = next;
			next = iterative.next();
			time++;
		}
		// Grab bomb range from playground at t-1
		Content currentContent = iterative.get(current.x, current.y);
		if(Fire.instance.equals(currentContent))
			return constants.SCORE_SUICIDE;
		Bomb toExplode = (Bomb) currentContent;
		// Now count what that bomb could have detonated
		for (int directionIndex = 0; directionIndex < Player.DIRECTIONS.length; directionIndex++) {
			int[] direction = Player.DIRECTIONS[directionIndex];
			for (int i = 0; i < toExplode.range; i++) {
				int p_x = current.x+direction[0]*i;
				int p_y = current.x+direction[1]*i;
				if(iterative.contains(p_x, p_y)) {
					Content content = iterative.get(p_x, p_y);
					switch(content.canFire()) {
					case NOT:
						break;
					case END_PROPAGATION:
						evaluatedScore+=content.accept(new BombScoreComputer(constants));
					case YES:
					}
				} else {
					break;
				}
			}
		}
		return evaluatedScore;
	}

	@Override public boolean canBeWalkedOn() { return true; }

	public boolean moves() {
		return !(x==current.x && y==current.y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((current == null) ? 0 : current.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Step other = (Step) obj;
		if (action != other.action)
			return false;
		if (current == null) {
			if (other.current != null)
				return false;
		} else if (!current.equals(other.current))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Step [score=" + score + ", current.x=" + current.x + ", current.y=" + current.y + ", action=" + action + ", x=" + x + ", y=" + y
				+ "]";
	}

	/**
	 * Invoked when generating a playground from a step
	 * @param playground 
	 * @param content
	 */
	public void mutateContent(final Playground real, Content content) {
		switch(action) {
		case BOMB:
			if(content.canBeWalkedOn()) {
				real.set(current.x, current.y, new Bomb(gamer.id, current.x, current.y, EvolvableConstants.BOMB_DELAY, gamer.range));
			}
			break;
		case MOVE:
			break;
		}
	}

	@Override
	public <Type> Type accept(ContentVisitor<Type> visitor) {
		throw new UnsupportedOperationException("A Step should never be visited");
	}
}