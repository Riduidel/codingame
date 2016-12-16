package org.ndx.codingame.fantastic.actions;

import java.util.Arrays;

import org.ndx.codingame.fantastic.Constants;
import org.ndx.codingame.fantastic.Playground;
import org.ndx.codingame.fantastic.entities.Entity;
import org.ndx.codingame.fantastic.entities.Wizard;
import org.ndx.codingame.fantastic.status.Status;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.lib2d.continuous.shapes.bezier.PolynomialBezierCurve;
import org.ndx.codingame.lib2d.shapes.Vector;

public class MoveTo implements Action {

	public final Vector direction;
	public final ContinuousPoint next;
	public final Entity target;
	private double score = -1;

	public MoveTo(final Wizard wizard, final Entity entity) {
		target = entity;
		
		final PolynomialBezierCurve curve = new PolynomialBezierCurve(wizard.position, Arrays.asList(wizard.getNextPosition(), entity.position), entity.getNextPosition());
		next = curve.pointAtDistance(Math.min(curve.length(), Constants.WIZARD_MAX_SPEED));
		direction = new Vector(wizard.position, entity.direction.second);
	}

	@Override
	public double getScore() {
		if(score<0) {
			score  =  Playground.WIDTH*direction.first.distance2To(next)/direction.length();
		}
		return score;
	}

	@Override
	public boolean conflictsWith(final Status status, final Action current) {
		return current.accept(new ActionAdapter<Boolean>(false) {

			@Override
			public Boolean visit(final MoveTo moveTo) {
				return moveTo.direction.intersectsWith(direction);
			}
			
		});
	}

	@Override
	public <Type> Type accept(final ActionVisitor<Type> visitor) {
		return visitor.visit(this);
	}

	@Override
	public String toCommand() {
		return Actions.moveTo(direction.second, Constants.WIZARD_MAX_SPEED);
	}

	@Override
	public String toString() {
		return String.format("MoveTo [next=%s, target=%s, score=%s]", next, target, getScore());
	}

	@Override
	public void updateStatus(final Status status) {
		// Nothing to do
	}

}
