package org.ndx.codingame.thaleshkt.actions;

import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import org.ndx.codingame.lib2d.Geometry;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.lib2d.shapes.Segment;
import org.ndx.codingame.lib2d.shapes.Vector;
import org.ndx.codingame.thaleshkt.Constants;
import org.ndx.codingame.thaleshkt.MetaParameters;
import org.ndx.codingame.thaleshkt.entities.UFO;
import org.ndx.codingame.thaleshkt.playground.Collision;
import org.ndx.codingame.thaleshkt.playground.Playfield;

public class Move {
	public final UFO moving;
	public final ContinuousPoint destination;
	public final int thrust;
	public final boolean boost;
	

	public Move(UFO moving, ContinuousPoint at, int i, boolean boost) {
		this.moving = moving;
		this.destination = at;
		this.thrust = i;
		this.boost = boost;
	}
	@Override
	public String toString() {
		if(boost) {
			return String.format("%d %d BOOST", (int) destination.x, (int) destination.y);
		} else {
			return String.format("%d %d %d", (int) destination.x, (int) destination.y, thrust);
		}
	}
	public Move resolveCollisions(Playfield p) {
		return this;
	}
	public Vector lineOfThrust() {
		Vector returned = Geometry.from(moving.position.center).vectorOf(destination);
		ContinuousPoint atThrust;
		if(boost) {
			atThrust = returned.pointAtDistance(Constants.BOOST_POWER, moving.position.center);
		} else {
			atThrust = returned.pointAtDistance(thrust, moving.position.center);
		}
		return Geometry.from(returned.first).vectorOf(atThrust);
	}
	protected Move avoidCollisions(Playfield p) {
		SortedSet<Collision> collisions = p.computeCollisionsOf(this);
		if(collisions.isEmpty()) {
			return this;
		} else {
			Collision first = collisions.first();
	
			// We have a collision. Is it below our treshold
			if(first.turns<MetaParameters.COLLISION_HORIZON) {
				// Now find the three magic spots.
				// First one is after horizon
				Vector lineOfThrust = first.colliding.lineOfThrust();
				ContinuousPoint afterHorizon = lineOfThrust.pointAtNTimes(MetaParameters.AFTER_HORIZON);
				// Now find points to turn around
				Segment orthogonal = lineOfThrust.orthogonal(lineOfThrust.first).getDefiningSegment();
				ContinuousPoint onOneSide = orthogonal.pointAtDistance(MetaParameters.ORTHOGONAL_POINTS, orthogonal.first);
				ContinuousPoint onOtherSide = orthogonal.pointAtDistance(-1*MetaParameters.ORTHOGONAL_POINTS, orthogonal.first);
				
				return new GoToAdversaryFlag(moving, findBestStep(first, afterHorizon, onOneSide, onOtherSide), thrust, boost);
			} else {
				return this;
			}
		}
	}
	private ContinuousPoint findBestStep(Collision collision, ContinuousPoint...points) {
		SortedMap<Double, ContinuousPoint> sorted = new TreeMap<>();
		for (ContinuousPoint continuousPoint : points) {
			Segment first = Geometry.from(moving.position.center).segmentTo(continuousPoint);
			if(!collision.colliding.lineOfThrust().intersectsWith(first)) {
				Segment second = Geometry.from(continuousPoint).segmentTo(destination);
				sorted.put(first.length()+second.length(), continuousPoint);
			}
		}
		return sorted.get(sorted.firstKey());
	}
}
