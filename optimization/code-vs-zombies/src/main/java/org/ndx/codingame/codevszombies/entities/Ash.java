package org.ndx.codingame.codevszombies.entities;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.ndx.codingame.codevszombies.playground.Playfield;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public class Ash implements Entity {
	public static final int SPEED = 1000;
	public static final int SHOOT = 2000;

	private final ContinuousPoint position;

	public Ash(final int x, final int y) {
		position = new ContinuousPoint(x, y);
	}

	@Override
	public StringBuilder toUnitTestConstructor(final String multilinePrefix) {
		final StringBuilder returned = new StringBuilder();
		returned.append("new Ash(").append((int)position.x).append(", ").append((int)position.x).append(")");
		return returned;
	}

	public ContinuousPoint findBestMoveOn(final Playfield playfield) {
		final SortedMap<Double, Human> sortedByDistance = byDistanceTo(playfield.getHumans());
		final SortedMap<Double, Human> toSave = new TreeMap<>(sortedByDistance);
		Human candidate = null;
		while(candidate==null && !toSave.isEmpty()) {
			candidate = toSave.remove(toSave.firstKey());
			if(!survive(playfield, candidate)) {
				candidate=null;
			}
		}
		if(candidate==null) {
			if(sortedByDistance.isEmpty()) {
				return playfield.center();
			} else {
				return sortedByDistance.get(sortedByDistance.firstKey()).getPosition();
			}
		} else {
			return candidate.getPosition();
		}
	}

	private boolean survive(final Playfield playfield, final Human candidate) {
		final int distance = (int) position.distance2To(candidate.getPosition());
		final int turns = distance/SPEED-1; // remove one turn for shooting range
		if(turns<=0) {
			return true;
		}
		final List<Playfield> futurePlayfield = playfield.predictAtLeast(turns);
		final Playfield future =futurePlayfield.get(turns);
		return future.getHumans().contains(candidate);
	}

	@Override
	public ContinuousPoint getPosition() {
		return position;
	}

}
