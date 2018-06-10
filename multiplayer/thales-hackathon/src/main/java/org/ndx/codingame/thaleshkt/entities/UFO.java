package org.ndx.codingame.thaleshkt.entities;

import org.ndx.codingame.gaming.tounittest.ConstructableInUnitTest;
import org.ndx.codingame.lib2d.Geometry;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.lib2d.shapes.Circle;
import org.ndx.codingame.thaleshkt.Constants;
import org.ndx.codingame.thaleshkt.actions.Move;
import org.ndx.codingame.thaleshkt.playground.Participant;
import org.ndx.codingame.thaleshkt.playground.Playfield;
import org.ndx.codingame.thaleshkt.status.CanBoost;
import org.ndx.codingame.thaleshkt.status.MySide;

public class UFO implements ConstructableInUnitTest {

	public final Circle position;
	public final ContinuousPoint speed;
	public final boolean hasFlag;
	private ContinuousPoint destination;

	public UFO(int x, int y, int vx, int vy, int flag) {
		this.position = new Circle(new ContinuousPoint(x, y), Constants.UFO_RADIUS);
		this.speed = new ContinuousPoint(vx, vy);
		this.hasFlag = flag!=0;
	}

	@Override
	public StringBuilder toUnitTestConstructor(String multilinePrefix) {
		return new StringBuilder(multilinePrefix)
				.append("new UFO(")
				.append((int) position.center.x).append(", ")
				.append((int) position.center.y).append(", ")
				.append((int) speed.x).append(", ")
				.append((int) speed.y).append(", ")
				.append(hasFlag ? 1 : 0)
				.append(")");
	}

	public Move compute(Playfield playfield) {
		Move returned;
		if(hasFlag) {
			// Just aim for position with same y, but on our side
			returned = new Move(Geometry.at(playfield.status.get(MySide.class).my.edge.first.x, position.center.y),
					100,
					playfield.status.get(CanBoost.class).first.canBoost());
		} else {
			// Aim for nearest flag
			ContinuousPoint myFlag = playfield.getPositionOfFlag(Participant.MY);
			ContinuousPoint adversaryFlag = playfield.getPositionOfFlag(Participant.ENEMY);
			double distanceToMy = myFlag.distance2SquaredTo(position.center);
			double distanceToAdversary = adversaryFlag.distance2SquaredTo(position.center);
			boolean aimingMy = false;
			if(distanceToMy<distanceToAdversary) {
				destination = myFlag;
				aimingMy = true;
			} else {
				destination = adversaryFlag;
			}
			if(this==playfield.my.second) {
				if(destination.distance2SquaredTo(playfield.my.first.destination)<5) {
					if(aimingMy) {
						destination = adversaryFlag;
					} else {
						destination = myFlag;
					}
				}
			}
			returned = new Move(destination, Math.min(100, 
					(int) position.center.distance2To(destination)-Constants.UFO_RADIUS),
					playfield.status.get(CanBoost.class).first.canBoost());
		}
		destination = returned.destination;
		return returned;
	}
}
