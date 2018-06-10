package org.ndx.codingame.fantastic.actions;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.ndx.codingame.fantastic.Constants;
import org.ndx.codingame.fantastic.Playground;
import org.ndx.codingame.fantastic.entities.Entity;
import org.ndx.codingame.fantastic.entities.Snaffle;
import org.ndx.codingame.fantastic.entities.Wizard;
import org.ndx.codingame.fantastic.status.FantasticStatus;
import org.ndx.codingame.fantastic.status.TeamStatus;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.lib2d.shapes.Circle;
import org.ndx.codingame.lib2d.shapes.Segment;
import org.ndx.codingame.lib2d.shapes.Vector;
import org.ndx.codingame.libstatus.Status;

public class ThrowTo implements Action {

	public final Vector direction;

	public ThrowTo(final Collection<Entity> entities, final Status status, final Wizard wizard, final Snaffle snaffle) {
		// Throw snaffle in direction of goal (taking care of correction)
		// Potentially use bounces on walls
		direction = findDirection(entities, status, wizard);
	}

	private Vector findDirection(final Collection<Entity> entities, final Status status, final Wizard wizard) {
		final Segment goal = status.get(TeamStatus.class).getAttacked();
		final List<Circle> toAvoid = entities.stream()
				.filter(e -> e.isBetween(wizard, goal))
				.filter(e -> e.position.getX()!=wizard.position.getX() || e.position.getY()!=wizard.position.getY())
				.flatMap(e -> Arrays.asList(
						new Circle(e.direction.first, e.getRadius()+Constants.RADIUS_SNAFFLE), 
						new Circle(e.direction.second, e.getRadius()+Constants.RADIUS_SNAFFLE))
					.stream())
				.collect(Collectors.toList());
		final ContinuousPoint goalCenter = goal.pointAtNTimes(0.5);
		final List<ContinuousPoint> targets = Arrays.asList(goalCenter,
				goal.pointAtNTimes(0.25),
				goal.pointAtNTimes(0.75),
				// up symetric
				Playground.TOP.symetricOf(goalCenter),
				// down symetric
				Playground.BOTTOM.symetricOf(goalCenter),
				// final point allow fire
				goalCenter);
		Vector direct = null;
		for(final ContinuousPoint t : targets) {
			direct = new Vector(wizard.direction.second, t);
			if(!collidesWith(toAvoid, direct)) {
				return direct;
			}
		}
		return direct;
	}

	private boolean collidesWith(final List<Circle> toAvoid, final Segment direct) {
		return toAvoid.stream().anyMatch(c -> direct.intersectsWith(c));
	}

	@Override
	public double getScore() {
		return direction.getX();
	}

	@Override
	public boolean conflictsWith(final FantasticStatus status, final Action current) {
		return false;
	}

	@Override
	public <Type> Type accept(final ActionVisitor<Type> visitor) {
		return visitor.visit(this);
	}

	@Override
	public String toCommand() {
		return Actions.throwTo(direction.second);
	}

	@Override
	public String toString() {
		return String.format("ThrowTo [direction=%s, score=%s]", direction, getScore());
	}

	@Override
	public void updateStatus(final FantasticStatus status) {
		// Nothing to do
	}

}
