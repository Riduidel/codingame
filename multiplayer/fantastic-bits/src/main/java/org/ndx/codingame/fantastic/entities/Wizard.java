package org.ndx.codingame.fantastic.entities;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.ndx.codingame.fantastic.Playground;
import org.ndx.codingame.fantastic.spells.Spell;
import org.ndx.codingame.fantastic.spells.SpellContext;
import org.ndx.codingame.fantastic.status.Status;
import org.ndx.codingame.lib2d.Line;
import org.ndx.codingame.lib2d.PointBuilder;
import org.ndx.codingame.lib2d.Segment;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public class Wizard extends Entity {
	public static final int RADIUS = 400;

	private static final int THROW_POWER = 500;

	private static final int MAX_SPEED = 150;

	public final int teamId;

	public final boolean holdingSnaffle;

	private boolean attacking;

	public Wizard(int id, double x, double y, double vx, double vy, int teamId, boolean holdingSnaffle) {
		super(id, x, y, vx, vy);
		this.teamId = teamId;
		this.holdingSnaffle = holdingSnaffle;
	}
	public Wizard(int id, double x, double y, double vx, double vy, int teamId, boolean holdingSnaffle, boolean attacking) {
		this(id, x, y, vx, vy, teamId, holdingSnaffle);
		this.attacking = attacking;
	}

	public String play(Status status, List<Entity> entities, List<Wizard> myTeam) {
		// Find nearest snaffle
		Entities exploded = new Entities(entities, myTeam, getAttackedGoal(), getDefendedGoal());
		SpellContext context;
		for(Spell spell : Spell.values()) {
			context = spell.shouldCast(status, this, exploded);
			if(context.shouldCast()) {
				return spell.cast(status, context);
			}
		}
		Snaffle found = exploded.findBestSnaffleFor(this);
		if(found!=null) {
			found.isATarget = true;
			if(holdingSnaffle) {
				// Immediatly shoot to goal center at max speed
				return throwInDirectionOf(entities, getAttackedGoal());
			} else {
				// catch that goddam snaffle by going directly to its next position
				return moveTo(found.getNextPosition());
			}
		} else {
			return moveTo(position);
		}
	}

	private String throwInDirectionOf(List<Entity> entities, Segment goal) {
		List<Entity> toAvoid = entities.stream()
				.filter(e -> e.isBetween(this, goal))
				.filter(e -> e.position.getX()!=position.getX() || e.position.getY()!=position.getY())
				.collect(Collectors.toList());
		ContinuousPoint goalCenter = goal.pointAtNTimes(0.5);
		Segment direct = new Segment(position, goalCenter);
		ContinuousPoint target = null;
		int angle = 0;
		boolean found = false;
		while(!found && angle<90) {
			for (int multipler = -1; multipler < 2 && !found; multipler+=2) {
				target = direct.pointAtAngle(position, angle*multipler, THROW_POWER, PointBuilder.DEFAULT);
				Line obstacleFinder = new Line(position, target);
				found = true;
				Iterator<Entity> entity = toAvoid.iterator();
				while(entity.hasNext() && found) {
					Entity tested = entity.next();
					found = !obstacleFinder.intersectsWith(tested.getExtendedCircle(tested.getRadius()+Snaffle.RADIUS*2));
				}
			}
			angle+=5;
		}
		if(!found) {
			target = goalCenter;
		}
		return throwTo(target);
	}

	public Segment getDefendedGoal() {
		return Playground.goals.get(teamId);
	}

	public Segment getAttackedGoal() {
		return Playground.goals.get(1-teamId);
	}

	private String throwTo(ContinuousPoint goal) {
		return String.format("THROW %d %d %d", (int) goal.x, (int) goal.y, THROW_POWER);
	}

	private String moveTo(ContinuousPoint nearest) {
		return String.format("MOVE %d %d %d", (int) nearest.x, (int) nearest.y, MAX_SPEED);
	}

	@Override
	public <Type> Type accept(EntityVisitor<Type> visitor) {
		return visitor.visitWizard(this);
	}

	@Override
	protected double getRadius() {
		return RADIUS;
	}

	@Override
	public String toString() {
		return String.format("Wizard [teamId=%s, holdingSnaffle=%s, id=%s, position=%s, direction=%s]", teamId,
				holdingSnaffle, id, position, direction);
	}

	public void setAttacking(boolean b) {
		this.attacking= true;
	}

	public boolean isAttacking() {
		return attacking;
	}

}
