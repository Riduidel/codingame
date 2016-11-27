package org.ndx.codingame.fantastic;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public class Wizard extends Entity {
	public static final int RADIUS = 400;

	public final int teamId;

	public final boolean holdingSnaffle;

	public Wizard(int id, double x, double y, double vx, double vy, int teamId, boolean b) {
		super(id, x, y, vx, vy);
		this.teamId = teamId;
		this.holdingSnaffle = b;
	}

	public String play(List<Entity> entities) {
		// Find nearest snaffle
		Map<ContinuousPoint, Entity> positions =  entities.stream()
				.filter(e -> e instanceof Snaffle)
				.map(e -> (Snaffle) e)
				.filter(s -> !s.isATarget)
				.collect(Collectors.toMap(s -> s.position,
                        Function.identity()));
		ContinuousPoint nearest = position.findNearestDistance2(positions.keySet());
		Snaffle found = (Snaffle) positions.get(nearest);
		if(found!=null) {
			found.isATarget = true;
			if(holdingSnaffle) {
				// Immediatly shoot to goal center at max speed
				return throwTo(getGoal());
			} else {
				// catch that goddam snaffle
				return moveTo(nearest);
			}
		} else {
			return moveTo(position);
		}
	}

	private ContinuousPoint getGoal() {
		return Playground.goals.get(1-teamId).pointAtNTimes(0.5);
	}

	private String throwTo(ContinuousPoint goal) {
		return String.format("THROW %d %d 500", (int) goal.x, (int) goal.y);
	}

	private String moveTo(ContinuousPoint nearest) {
		return String.format("MOVE %d %d 150", (int) nearest.x, (int) nearest.y);
	}

	@Override
	public <Type> Type accept(EntityVisitor<Type> visitor) {
		return visitor.visitWizard(this);
	}

	@Override
	protected double getRadius() {
		return RADIUS;
	}

}
