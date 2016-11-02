package org.ndx.codingame.labyrinth;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.lib2d.discrete.ScoredDirection;

public class HeatMap extends Playground<Integer> {

	public final int MAX_DISTANCE;
	
	Set<DiscretePoint> frontier = new HashSet<>();

	public HeatMap(int width, int height) {
		super(width, height, width*height+1);
		this.MAX_DISTANCE = width*height+1;
	}

	public HeatMap(PlayField playground, DiscretePoint position, int range) {
		this(playground.width, playground.height);
		snapshot(playground, position, range, new ArrayDeque<>(), 0, 0);
	}

	private void snapshotDirectionsOf(PlayField playfield, DiscretePoint point, 
			int range, Deque<DiscretePoint> knownPoints,
			int startScore, int deepness) {
		for(Direction direction : Direction.DIRECTIONS) {
			ScoredDirection scored = direction.move(point);
			snapshot(playfield, scored, range, knownPoints, startScore, deepness+1);
		}
	}

	private void snapshot(PlayField playfield, DiscretePoint point, 
			int range, Deque<DiscretePoint> knownPoints, 
			int startScore, int deepness) {
		if(knownPoints.contains(point)) {
			return;
		} else if(range>0 && deepness>range) {
			if(!knownPoints.isEmpty()) {
				frontier.add(knownPoints.getLast());
			}
			return;
		} else {
			if(playfield.contains(point)) {
				char value = playfield.get(point);
				switch(value) {
				case '#':
					return;
				case '?':
					if(!knownPoints.isEmpty()) {
						frontier.add(knownPoints.getLast());
					}
					return;
				}
				knownPoints.add(point);
				int newHeat = startScore+deepness;
				int heat = get(point);
				if(heat>newHeat) {
					set(point, newHeat);
					snapshotDirectionsOf(playfield, point, range, knownPoints, startScore, deepness);
				}
				knownPoints.remove(point);
			}
		}
	}

	public void extend(PlayField playground) {
		// Sort those points by score on current heatmap
		Comparator<DiscretePoint> comparator = Comparator.comparingInt((DiscretePoint p) -> get(p))//.reversed()
				.thenComparingInt((DiscretePoint p) -> (int) p.getX())
				.thenComparingInt((DiscretePoint p) -> (int) p.getY());
		Set<DiscretePoint> oldFrontier = new TreeSet<>(comparator);
		oldFrontier.addAll(frontier);
		frontier = new HashSet<>();
		for (DiscretePoint explored : oldFrontier) {
			snapshotDirectionsOf(playground, explored, -1, new ArrayDeque<>(Arrays.asList(explored)), get(explored), 0);
		}
	}
}