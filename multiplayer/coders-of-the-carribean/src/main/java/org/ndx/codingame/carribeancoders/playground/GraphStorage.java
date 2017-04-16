package org.ndx.codingame.carribeancoders.playground;

import java.util.HashMap;
import java.util.Map;

import org.ndx.codingame.carribeancoders.Constants;
import org.ndx.codingame.carribeancoders.playground.Properties.Hexagonal;
import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.ScoredDirection;
import org.ndx.codingame.libgraph.DirectedGraph;
import org.ndx.codingame.libgraph.Graph;
import org.ndx.codingame.libgraph.GraphProperty;
import org.ndx.codingame.libgraph.Vertex;

public class GraphStorage {
	private static final GraphProperty<DiscretePoint> POINT = new GraphProperty<>("point");

	private static void fill(final Map<DiscretePoint, Vertex> coordinatesMapping, final Graph storage, final int width, final int height) {
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				final DiscretePoint position = new DiscretePoint(col, row);
				final Vertex vertex = storage.getOrCreateVertex(getId(col, row)).setProperty(GraphStorage.POINT, position);
				coordinatesMapping.put(position, vertex);
				// Now connect vertex to all previous ones
				if(col>0) {
					// connect to previous pointt
					final ScoredDirection<Object> previousPoint = Direction.LEFT.move(position);
					final Vertex previousVertex = coordinatesMapping.get(previousPoint);
					createGraphStorageEdge(Properties.Hexagonal.LEFT, storage, vertex, previousVertex);
				}
				if(row>0) {
					final ScoredDirection<Object> upperPoint = Direction.UP.move(position);
					final Map<Direction, Hexagonal> directions = new HashMap<>();
					// Replace by col to use pointy-top graph (instead of flat-top, but pointy side we use here)
					if(row%2==0) {
						directions.put(Direction.LEFT, Hexagonal.TOP_LEFT);
						directions.put(Direction.STAY, Hexagonal.TOP_RIGHT);
					} else {
						directions.put(Direction.STAY, Hexagonal.TOP_LEFT);
						directions.put(Direction.RIGHT, Hexagonal.TOP_RIGHT);
					}
					for(final Map.Entry<Direction, Hexagonal> d : directions.entrySet()) {
						final ScoredDirection<Object> target = d.getKey().move(upperPoint);
						if(target.x>=0 && target.x<width) {
							final Vertex previousVertex = coordinatesMapping.get(target);
							createGraphStorageEdge(d.getValue(), storage, vertex, previousVertex);
						}
					}
				}
			}
		}
	}

	private static void createGraphStorageEdge(final Hexagonal direction, final Graph graph, final Vertex first, final Vertex second) {
		graph.getOrCreateEdgeBetween(first, second).setProperty(Properties.DIRECTION, direction);
		graph.getOrCreateEdgeBetween(second, first).setProperty(Properties.DIRECTION, direction.opposite());
	}

	static int getId(final int col, final int row) {
		return col*100+row;
	}

	final Map<DiscretePoint, Vertex> coordinatesMapping = new HashMap<>();
	final Graph storage = new DirectedGraph();
	
	public GraphStorage() {
		this(Constants.WIDTH, Constants.HEIGHT);
	}
	public GraphStorage(final int width, final int height) {
		fill(coordinatesMapping, storage, width, height);
	}

	public Vertex get(final DiscretePoint key) {
		return coordinatesMapping.get(key);
	}
	public Vertex get(final int x, final int y) {
		return get(new DiscretePoint(x, y));
	}
}
