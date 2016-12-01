package org.ndx.codingame.fantastic.entities;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.stream.Collectors;

import org.ndx.codingame.fantastic.Playground;
import org.ndx.codingame.fantastic.spells.AccioSpell;
import org.ndx.codingame.fantastic.spells.FlipendoSpell;
import org.ndx.codingame.fantastic.spells.ObliviateSpell;
import org.ndx.codingame.fantastic.spells.Spell;
import org.ndx.codingame.fantastic.spells.SpellContext;
import org.ndx.codingame.fantastic.status.Status;
import org.ndx.codingame.lib2d.Line;
import org.ndx.codingame.lib2d.PointBuilder;
import org.ndx.codingame.lib2d.Segment;
import org.ndx.codingame.lib2d.base.AbstractPoint;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public class Wizard extends Entity {
	private static Collection<Spell> spells	 = Arrays.asList(
			new FlipendoSpell(),
			new AccioSpell(),
			new ObliviateSpell()
			);
	
	public static final int RADIUS = 400;

	private static final int THROW_POWER = 500;

	private static final int MAX_SPEED = 150;

	public final int teamId;

	public final boolean holdingSnaffle;

	private boolean attacking;

	public Wizard(final int id, final double x, final double y, final double vx, final double vy, final int teamId, final boolean holdingSnaffle) {
		super(id, x, y, vx, vy);
		this.teamId = teamId;
		this.holdingSnaffle = holdingSnaffle;
	}
	public Wizard(final int id, final double x, final double y, final double vx, final double vy, final int teamId, final boolean holdingSnaffle, final boolean attacking) {
		this(id, x, y, vx, vy, teamId, holdingSnaffle);
		this.attacking = attacking;
	}

	public Snaffle findBestSnaffleFor(final Entities exploded, final Wizard wizard) {
		final SortedMap<ContinuousPoint, Snaffle> snaffles = exploded.sortSnafflesFor(wizard);
		// fuzzy position to make sure item immediatly behind won't be forgotten
		final ContinuousPoint point = new ContinuousPoint(wizard.position.x + (wizard.isAttacking() ? -Wizard.RADIUS : Wizard.RADIUS), wizard.position.y);
		final SortedMap<ContinuousPoint, Snaffle> goodOnes = snaffles.headMap(point);
		final SortedMap<ContinuousPoint, Snaffle> badOnes = snaffles.tailMap(point);
		ContinuousPoint key = null;
		if(!goodOnes.isEmpty()) {
			key = findNearestPointIn(point, goodOnes);
		}
		if(key==null) {
			key = findNearestPointIn(point, badOnes);
		}
		return snaffles.get(key);
	}


	private ContinuousPoint findNearestPointIn(final ContinuousPoint point, final SortedMap<ContinuousPoint, Snaffle> goodOnes) {
		return goodOnes.keySet().stream()
				.sorted(new AbstractPoint.PositionByDistance2To(point))
				.filter(p -> !goodOnes.get(p).isATarget)
				.findFirst()
				.orElse(null);
	}
	
	public String play(final Status status, final List<Entity> entities, final List<Wizard> myTeam) {
		// Find nearest snaffle
		final Entities exploded = new Entities(entities, myTeam, getAttackedGoal(), getDefendedGoal());
		SpellContext context;
		for(final Spell spell : spells) {
			context = spell.shouldCast(status, this, exploded);
			if(context.shouldCast()) {
				return spell.cast(status, context);
			}
		}
		final Snaffle found = findBestSnaffleFor(exploded, this);
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

	private String throwInDirectionOf(final List<Entity> entities, final Segment goal) {
		final List<Entity> toAvoid = entities.stream()
				.filter(e -> e.isBetween(this, goal))
				.filter(e -> e.position.getX()!=position.getX() || e.position.getY()!=position.getY())
				.collect(Collectors.toList());
		final ContinuousPoint goalCenter = goal.pointAtNTimes(0.5);
		final Segment direct = new Segment(position, goalCenter);
		ContinuousPoint target = null;
		int angle = 0;
		boolean found = false;
		while(!found && angle<90) {
			for (int multipler = -1; multipler < 2 && !found; multipler+=2) {
				target = direct.pointAtAngle(position, angle*multipler, THROW_POWER, PointBuilder.DEFAULT);
				final Line obstacleFinder = new Line(position, target);
				found = true;
				final Iterator<Entity> entity = toAvoid.iterator();
				while(entity.hasNext() && found) {
					final Entity tested = entity.next();
					found = !obstacleFinder.intersectsWith(tested.getCircle(tested.getRadius()+Snaffle.RADIUS*2));
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

	private String throwTo(final ContinuousPoint goal) {
		return String.format("THROW %d %d %d", (int) goal.x, (int) goal.y, THROW_POWER);
	}

	private String moveTo(final ContinuousPoint nearest) {
		return String.format("MOVE %d %d %d", (int) nearest.x, (int) nearest.y, MAX_SPEED);
	}

	@Override
	public <Type> Type accept(final EntityVisitor<Type> visitor) {
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

	public void setAttacking(final boolean b) {
		attacking= true;
	}

	public boolean isAttacking() {
		return attacking;
	}

}
