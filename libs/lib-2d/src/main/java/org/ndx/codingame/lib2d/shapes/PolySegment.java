package org.ndx.codingame.lib2d.shapes;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.ndx.codingame.lib2d.PointBuilder;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public class PolySegment {

	private Double length;
	private SortedMap<Double, Segment> byDistance = null;
	private final List<Segment> segments;

	public PolySegment(final List<Segment> asList) {
		segments = asList;
	}
	
	private void buildByDstance() {
		if(byDistance==null) {
			final SortedMap<Double, Segment> distanceMap = new TreeMap<>();
			double distance = 0;
			for(final Segment s : segments) {
				distanceMap.put(distance, s);
				distance+=s.length();
			}
			length = distance;
			byDistance = distanceMap;
		}
	}

	public PolySegment extendWith(final PolySegment polySegment) {
		final List<Segment> used = new ArrayList<>(segments());
		final Segment last = used.get(used.size()-1);
		final Segment first = polySegment.segments().get(0);
		if(!last.second.equals(first.first)) {
			final Segment connector = new Segment(last.second, first.first);
			used.add(connector);
		}
		used.addAll(polySegment.segments());
		return new PolySegment(used);
	}
	
	public List<Segment> segments() {
		return segments;
	}

	public double length() {
		if(length==null) {
			buildByDstance();
		}
		return length;
	}

	public <Type extends ContinuousPoint> Type pointAtDistance(final double distance, final PointBuilder<Type> builder) {
		final List<Segment> used = segments;
		if(distance>length()) {
			throw new UnsupportedOperationException("can't get point farther than full polysegment length");
		}
		final Double distanceOfSegment = getByDistance().headMap(distance).lastKey();
		return getByDistance().get(distanceOfSegment).pointAtDistance(distance-distanceOfSegment, builder);
	}

	private SortedMap<Double,Segment> getByDistance() {
		if(byDistance==null) {
			buildByDstance();
		}
		return byDistance;
	}

}
