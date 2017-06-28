package org.ndx.codingame.wondevwoman.playground;

import java.util.Collection;
import java.util.Map;

import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.wondevwoman.Constants;
import org.ndx.codingame.wondevwoman.actions.Move;
import org.ndx.codingame.wondevwoman.actions.Push;
import org.ndx.codingame.wondevwoman.actions.WonderAction;
import org.ndx.codingame.wondevwoman.actions.WonderActionVisitor;
import org.ndx.codingame.wondevwoman.entities.Content;
import org.ndx.codingame.wondevwoman.entities.ContentAdapter;
import org.ndx.codingame.wondevwoman.entities.Floor;
import org.ndx.codingame.wondevwoman.entities.Gamer;
import org.ndx.codingame.wondevwoman.entities.Hole;

public class GamingStep extends Gaming {
	private WonderAction applied;
	private int turn = 1;
	private double score;

	@Override
	public GamingStep withMy(final Collection<Gamer> my) {
		return (GamingStep) super.withMy(my);
	}

	@Override
	public GamingStep withEnemy(final Collection<Gamer> enemy) {
		return (GamingStep) super.withEnemy(enemy);
	}

	@Override
	public GamingStep withPlayfield(final Playfield playfield) {
		return (GamingStep) super.withPlayfield(playfield);
	}

	/**
	 * Apply given action and compute associated score
	 * @param action
	 */
	public void applyAction(final WonderAction action) {
		applied = action;
		// Now, really perform action
		applied.accept(new WonderActionVisitor<Void>() {

			@Override
			public Void visitMove(final Move move) {
				applyMoveAction(move); return null;
			}

			@Override
			public Void visitPush(final Push push) {
				applyPushAction(push); return null;
			}
		});
		// Now we have changed playfield and players, compute next actions
		withActions(computeAvailableActions());
		// And compute score components
		score = computeScore();
	}

	private double computeScore() {
		return computeRealScore() + computeFreedomScore();
	}

	private double computeRealScore() {
		return computeRealScore(myByPosition) - computeRealScore(enemy);
	}

	private double computeRealScore(final Map<DiscretePoint, Gamer> enemy) {
		int returned = 0;
		for(final DiscretePoint position : enemy.keySet()) {
			if(playfield.contains(position)) {
				returned += playfield.get(position).accept(new ContentAdapter<Integer>(0) {
					@Override
					public Integer visitFloor(final Floor floor) {
						if(floor.height==Constants.MAX_FLOOR) {
							return 1;
						} else {
							return 0;
						}
					}
				});
			}
		}
		return returned/myByPosition.size();
	}

	private int computeFreedomScore() {
		return actions.size()/Constants.MAX_MOVES_COUNT;
	}

	protected void applyPushAction(final Push push) {
		final Gamer playing = findMyAt(push.gamerIndex);
		final DiscretePoint newPosition = push.getPush().move(playing.position);
		final Gamer movedEnemy = enemy.remove(newPosition);
		final DiscretePoint pushedPosition = push.getPush().move(newPosition);
		enemy.put(pushedPosition, new Gamer(pushedPosition, movedEnemy.index));
		buildOn(newPosition, push);
	}

	protected void applyMoveAction(final Move move) {
		final Gamer playing = findMyAt(move.gamerIndex);
		// Move the player
		myByPosition.remove(playing.position);
		final DiscretePoint newPosition = move.getMove().move(playing.position);
		final Gamer newPlayer = new Gamer(newPosition, playing.index);
		myByPosition.put(newPosition, newPlayer);
		// Add one level to floor
		buildOn(newPosition, move);
	}

	private void buildOn(final DiscretePoint newPosition, final WonderAction action) {
		final DiscretePoint buildPlace = action.getBuild().move(newPosition);
		final Content next = playfield.get(buildPlace).accept(new ContentAdapter<Content>(Hole.instance) {
			@Override
			public Content visitFloor(final Floor floor) {
				return new Floor(floor.height+1);
			}
		});
		playfield.set(buildPlace, next);
	}

	public WonderAction getApplied() {
		return applied;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(final int turn) {
		this.turn = turn;
	}

	public GamingStep withTurn(final int turn) {
		setTurn(turn);
		return this;
	}

	@Override
	protected GamingStep deriveForAction(final WonderAction action) {
		return super.deriveForAction(action).withTurn(turn+1);
	}

	public double getScore() {
		return score;
	}
}
