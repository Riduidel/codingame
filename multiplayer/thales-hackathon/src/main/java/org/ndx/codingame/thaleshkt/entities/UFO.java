package org.ndx.codingame.thaleshkt.entities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.ndx.codingame.gaming.tounittest.ConstructableInUnitTest;
import org.ndx.codingame.lib2d.Geometry;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.lib2d.shapes.Vector;
import org.ndx.codingame.thaleshkt.Constants;
import org.ndx.codingame.thaleshkt.actions.GoToAdversaryFlag;
import org.ndx.codingame.thaleshkt.actions.ComeBackWithFlag;
import org.ndx.codingame.thaleshkt.actions.DefendFlag;
import org.ndx.codingame.thaleshkt.actions.Move;
import org.ndx.codingame.thaleshkt.actions.MoveType;
import org.ndx.codingame.thaleshkt.actions.PursuitCapturedFlag;
import org.ndx.codingame.thaleshkt.playground.Participant;
import org.ndx.codingame.thaleshkt.playground.Playfield;
import org.ndx.codingame.thaleshkt.status.CanBoost;
import org.ndx.codingame.thaleshkt.status.MySide;

public class UFO extends AbstractEntity implements ConstructableInUnitTest {

	public final ContinuousPoint speed;
	public final boolean hasFlag;
	public final Participant participant;
	public final Gamer gamer;
	/**
	 * Shared state allowing other team members to know where this UFO is going
	 */
	private boolean aimingMy;
	private Vector lineOfThrust;

	public UFO(Participant p, Gamer g, int x, int y, int vx, int vy, int flag) {
		super(x, y, Constants.UFO_RADIUS);
		this.participant = p;
		this.gamer = g;
		this.speed = new ContinuousPoint(vx, vy);
		this.hasFlag = flag!=0;
	}

	@Override
	public StringBuilder toUnitTestConstructor(String multilinePrefix) {
		return new StringBuilder(multilinePrefix)
				.append("new UFO(")
				.append("Participant.").append(participant.name()).append(", ")
				.append("Gamer.").append(gamer.name()).append(", ")
				.append((int) position.center.x).append(", ")
				.append((int) position.center.y).append(", ")
				.append((int) speed.x).append(", ")
				.append((int) speed.y).append(", ")
				.append(hasFlag ? 1 : 0)
				.append(")");
	}

	public Move computeComeback(Playfield playfield) {
		Move returned;
		// Just aim for position with same y, but on our side
		returned = new ComeBackWithFlag(this, Geometry.at(playfield.status.get(MySide.class).my.edge.first.x, position.center.y),
				100,
				playfield.status.get(CanBoost.class).first.canBoost());
		return returned;
	}

	public Move computeCapture(Playfield playfield) {
		AbstractEntity adversaryFlagEntity = playfield.getFlagOwner(Participant.ADVERSARY);
		ContinuousPoint adversaryFlag = adversaryFlagEntity.position.center;
		int thrust = computeThrust(adversaryFlag);
		return new GoToAdversaryFlag(this, adversaryFlag, thrust,
				thrust==100 ? playfield.status.get(CanBoost.class).first.canBoost() : false);
	}

	/**
	 * Defense is quite simple : if the destination is my flag (and not an enemy holding the flag),
	 * got to barycenter
	 * @param playfield
	 * @return
	 */
	public Move computeDefense(Playfield playfield) {
		return playfield.getFlagOwner(Participant.MY).accept(new EntityVisitor<Move>() {
			@Override
			public Move visitFlag(Flag flag) {
				ContinuousPoint flagCenter = playfield.my.flag.position.center;
				ContinuousPoint asDefender = flagCenter.moveOf(playfield.status.get(MySide.class).my.asDefender);
				ContinuousPoint destination = Geometry.barycenterOf(Arrays.asList(asDefender,
						playfield.adversary.first.position.center,
						playfield.adversary.second.position.center));
				return new DefendFlag(UFO.this, destination, computeThrust(destination),
						false);
			}
			@Override
			public Move visitUFO(UFO ufo) {
				ContinuousPoint destination = ufo.position.center.moveOf(ufo.speed);
				return new PursuitCapturedFlag(UFO.this, destination, computeThrust(destination),
						playfield.status.get(CanBoost.class).first.canBoost());
			}
		});
	}

	private int computeThrust(ContinuousPoint destination) {
		return Math.max(0,  Math.min(100, 
				(int) position.center.distance2To(destination)-Constants.UFO_RADIUS));
	}

	@Override
	public <Type> Type accept(EntityVisitor<Type> visitor) {
		return visitor.visitUFO(this);
	}

	public Vector lineOfThrust() {
		if(lineOfThrust==null) {
			lineOfThrust = Geometry.from(position.center).vectorOf(position.center.moveOf(speed));
		}
		return lineOfThrust;
	}

	public Map<MoveType, Move> computeMoves(Playfield playfield) {
		Map<MoveType, Move> returned = new HashMap<>();
		returned.put(MoveType.ATTACK, computeCapture(playfield));
		returned.put(MoveType.DEFENSE, computeDefense(playfield));
		return returned;
	}
}
