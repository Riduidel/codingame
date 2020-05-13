package org.ndx.codingame.spring.challenge.playground;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.ndx.codingame.gaming.tounittest.ToUnitTestHelpers;
import org.ndx.codingame.lib2d.ImmutablePlayground;
import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.lib2d.discrete.PlaygroundAdapter;
import org.ndx.codingame.spring.challenge.EvolvableConstants;
import org.ndx.codingame.spring.challenge.actions.MoveTo;
import org.ndx.codingame.spring.challenge.actions.PacAction;
import org.ndx.codingame.spring.challenge.entities.AbstractDistinctContent;
import org.ndx.codingame.spring.challenge.entities.BigPill;
import org.ndx.codingame.spring.challenge.entities.Content;
import org.ndx.codingame.spring.challenge.entities.ContentAdapter;
import org.ndx.codingame.spring.challenge.entities.ContentVisitor;
import org.ndx.codingame.spring.challenge.entities.Ground;
import org.ndx.codingame.spring.challenge.entities.Nothing;
import org.ndx.codingame.spring.challenge.entities.Pac;
import org.ndx.codingame.spring.challenge.entities.PotentialSmallPill;
import org.ndx.codingame.spring.challenge.entities.SmallPill;
import org.ndx.codingame.spring.challenge.entities.Wall;

public class Playfield extends Playground<Content> {
	private static final int NEXT_POINTS_VIEW_RANGE = Integer.MAX_VALUE;
	static final int NEXT_POINTS_SPEED = 2;
	private static final int NEXT_POINTS_NORMAL = 1;
	static final List<Integer> NEXT_POINTS_LIST = Arrays.asList(
			NEXT_POINTS_NORMAL,
			NEXT_POINTS_SPEED,
			NEXT_POINTS_VIEW_RANGE);

	public static final class ToPhysicalString extends ContentAdapter<String> {
		private ToPhysicalString() {
			super("X");
		}
		@Override
		public String visitBigPill(BigPill bigPill) {
			return bigPill.toString();
		}
		@Override
		public String visitGround(Ground ground) {
			return ground.toString();
		}
		/**
		 * In physical display, pacs are hidden, since they are provided in an other way
		 */
		@Override
		public String visitPac(Pac pac) {
			return " ";
		}
		@Override
		public String visitSmallPill(SmallPill smallPill) {
			return smallPill.toString();
		}
		@Override
		public String visitWall(Wall wall) {
			return wall.toString();
		}
		public String visitPotentialSmallPill(PotentialSmallPill potentialSmallPill) {
			return potentialSmallPill.toString();
		};
	}
	

	public DiscretePoint putBackOnPlayground(DiscretePoint point) {
		int x = point.x,
			y = point.y;
		if(x<0) {
			x = getWidth()-1;
		} else if(x>=getWidth()) {
			x = 0;
		}
		if(y<0) {
			y = getHeight()-1;
		} else if(y>=getHeight()) {
			y = 0;
		}
		return new DiscretePoint(x, y);
	}

	public boolean allow(final DiscretePoint position) {
		return allow(position.x, position.y);
	}

	public boolean allow(final int p_x, final int p_y) {
		if(contains(p_x, p_y)) {
			return get(p_x, p_y).canBeWalkedOn();
		}
		return false;
	}

	public void readRow(final String row, final int rowIndex) {
		final char[] characters = row.toCharArray();
		for (int x = 0; x < characters.length; x++) {
			char character = characters[x];
			Content content = null;
			switch(character) {
			case Ground.CHARACTER: content = Ground.instance; break;
			case Wall.CHARACTER: content = Wall.instance; break;
			case BigPill.CHARACTER: content = new BigPill(x, rowIndex); break;
			case SmallPill.CHARACTER: content = new SmallPill(x, rowIndex); break;
			case PotentialSmallPill.CHARACTER: content = PotentialSmallPill.instance; break;
			}
			set(x, rowIndex, content);
		}
	}

	public String toString(final ContentVisitor<String> visitor) {
		return toStringCollection(visitor).stream().reduce((r1, r2) -> r1+"\n"+r2).get();
	}

	public Collection<String> toStringCollection(final ContentVisitor<String> visitor) {
		return accept(new PlaygroundAdapter<Collection<String>, Content>() {
			private StringBuilder row;
			@Override
			public void endVisitRow(final int y) {
				returned.add(row.toString());
			}
			@Override
			public void startVisit(final ImmutablePlayground<Content> playground) {
				returned = new ArrayList<>(playground.getHeight());
			}
			@Override
			public void startVisitRow(final int y) {
				row = new StringBuilder();
			}
			@Override
			public void visit(final int x, final int y, final Content content) {
				if(content==null) {
					row.append(Nothing.instance.accept(visitor));
				} else {
					row.append(content.accept(visitor));
				}
			}
		});
	}
	
	private Map<BigPill, ScoringSystem> bigPillsInfos = new HashMap<>();
	public final Playground<Double> zero;
	
	Map<Integer, Playground<List<List<DiscretePoint>>>> nextPointsCache = new HashMap<>();

	Set<SmallPill> smallPills = new HashSet<>();
	Set<BigPill> bigPills = new HashSet<>();
	Set<Pac> allPacs = new HashSet<Pac>();
	private ImmutablePlayground<ScoringSystem> distancesToPoints;
	Playground<List<Direction>> directions;

	public Playfield(final int width, final int height) {
		super(width, height, PotentialSmallPill.instance);
		zero = new Playground<>(width, height, 0d);
	}

	/**
	 * Cloning constructor
	 * 
	 * @param playground
	 */
	public Playfield(final Playfield playground) {
		super(playground);
		zero = new Playground<>(width, height, 0d);
	}

	@Override
	public void set(DiscretePoint p, Content c) {
		Content previous = get(p);
		super.set(p, c);
		setSpecificContent(p, previous, c);
	}

	@Override
	public void set(int x, int y, Content c) {
		Content previous = get(x, y);
		super.set(x, y, c);
		setSpecificContent(new DiscretePoint(x, y), previous, c);
	}

	private void setSpecificContent(DiscretePoint location, Content previous, Content next) {
		next.accept(new SpecificContentSetter(this, previous, location, next));
	}

	public void init() {
		// Compute distance2 for each point
		distancesToPoints = computeDistance2ToPoints();
		// Compute valid directions for each point
		directions = computePossibleDirections();
		for(Integer i : NEXT_POINTS_LIST) {
			nextPointsCache.put(i, computeNextPointsList(directions, i));
		}
	}

	private ImmutablePlayground<ScoringSystem> computeDistance2ToPoints() {
		Playground<ScoringSystem> returned = new Playground<>(width, height);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				DiscretePoint p = new DiscretePoint(x, y);
				Content content = get(p);
				if(content.canBeWalkedOn()) {
					returned.set(x, y, new ScoringSystem(content, 
							computeDistancesTo(p, NEXT_POINTS_VIEW_RANGE), computeDistance2SquaredTo(p)));
				}
			}
		}
		return returned;
	}

	private ImmutablePlayground<Double> computeDistance2SquaredTo(DiscretePoint center) {
		Playground<Double> returned = new Playground<>(width, height);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				DiscretePoint p = new DiscretePoint(x, y);
				returned.set(x, y, p.distance2SquaredTo(center));
			}
		}
		return returned;
	}

	private Playground<List<List<DiscretePoint>>> computeNextPointsList(Playground<List<Direction>> directions,
			int limit) {
		Playground<List<List<DiscretePoint>>> points = new Playground<>(width, height);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				DiscretePoint p = new DiscretePoint(x, y);
				List<List<DiscretePoint>> successors = computeNextPointsListAt(directions, p, limit);
				points.set(p, successors);
			}
		}
		return points;
	}

	List<List<DiscretePoint>> computeNextPointsListAt(Playground<List<Direction>> directions,
			DiscretePoint p, int limit) {
		List<Direction> directionsFor = directions.get(p);
		List<List<DiscretePoint>> successors = new ArrayList<>();
		for (Direction d : directionsFor) {
			List<DiscretePoint> pointsInThatDirection = new ArrayList<>();
			
			int deeepness = 0;
			for(DiscretePoint point = p;
					get(point).canBeWalkedOn() && deeepness<=limit;
					point = putBackOnPlayground(d.move(point))
					) {
				pointsInThatDirection.add(point);
				deeepness++;
			}
			// Since we always include this point, we have to make sure there is at least one other point 
			if(pointsInThatDirection.size()>1) {
				successors.add(pointsInThatDirection);
			}
		}
		return successors;
	}

	private Playground<List<Direction>> computePossibleDirections() {
		Playground<List<Direction>> directions = new Playground<>(width, height);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				List<Direction> directionsForPoint = new LinkedList<>();
				DiscretePoint point = new DiscretePoint(x, y);
				for (Direction d : Direction.DIRECTIONS) {
					DiscretePoint next = putBackOnPlayground(d.move(point));
					if (get(next).canBeWalkedOn()) {
						directionsForPoint.add(d);
					}
				}
				directions.set(point, directionsForPoint);
			}
		}
		return directions;
	}

	private Playground<Integer> computeDistancesTo(DiscretePoint c, int limit) {
		Playground<Integer> distances = new Playground<>(width, height, EvolvableConstants.DISTANCE_UNREACHABLE);
		List<DiscretePoint> points = Arrays.asList(c);
		int distance = 0;
		while (!points.isEmpty() && distance <= limit) {
			List<DiscretePoint> nextRound = new LinkedList<>();
			// First, set value in all known points
			for (DiscretePoint p : points) {
				distances.set(p, distance);
				// For each point, observe the list of neighbors which distance is greater than
				// distance + 1, and loop on them
				for (Direction d : Direction.DIRECTIONS) {
					DiscretePoint next = putBackOnPlayground(d.move(p));
					if (allow(next)) {
						if (distances.get(next) > distance + 1) {
							nextRound.add(next);
						}
					}
				}
			}
			distance++;
			points = nextRound;
		}
		return distances;
	}

	public List<Pac> getMyPacs() {
		List<Pac> returned = new ArrayList<>();
		for (Pac pac : allPacs) {
			if (pac.mine)
				returned.add(pac);
		}
		return returned;
	}

	private String toCommands(Map<Pac, PacAction> actions) {
		return actions.values().stream().map(action -> action.toCommandString()).collect(Collectors.joining("|"));
	}

	public void readGameEntities(AbstractDistinctContent... contents) {
		// Don't forget to restore bigpills and small pills
		bigPills.addAll(getAll(BigPill.class));
		smallPills.addAll(getAll(SmallPill.class));
		for (AbstractDistinctContent c : contents) {
			set(c, c);
		}
	}

	public String compute() {
		return toCommands(computeActions(-1));
	}

	/**
	 * Update playground by first clearing all view range excepted explicit elements
	 */
	private void updatePlayground() {
		for (Pac pac : getMyPacs()) {
			for (List<DiscretePoint> directions : nextPointsCache.get(NEXT_POINTS_VIEW_RANGE).get(pac)) {
				for (DiscretePoint point : directions) {
					if(get(point) instanceof PotentialSmallPill) {
						set(point, Ground.instance);
					}
				}
			}
		}
	}

	/**
	 * maximum is a kind of deepness we used in real turn
	 * @param maximum
	 * @return
	 */
	public Map<Pac, PacAction> computeActions(int maximum) {
		updatePlayground();
		Map<Pac, PacAction> returned = new HashMap<>();
		for(Pac my : getMyPacs()) {
			returned.put(my, computeActionFor(my));
		}
		return returned;
	}
	
	private ImmutablePlayground<Double> usingDistance(DiscretePoint system) {
		return distancesToPoints.get(system).distancesOnPlaygroundSquared;
	}

	private PacAction computeActionFor(Pac my) {
		set(my, Ground.instance);
		ImmutablePlayground<Double> scores = buildDistancesScoresFor(my);
		SortedMap<Double, PacAction> actions = new TreeMap<>(Comparator.reverseOrder());
		List<List<DiscretePoint>> accessibleNextPoints = nextPointsCache.get(NEXT_POINTS_NORMAL).get(my);
		for(List<DiscretePoint> direction : accessibleNextPoints) {
			double score = 0;
			DiscretePoint last = null;
			for(DiscretePoint point : direction) {
				if(get(point).canBeWalkedOn()) {
					last = point;
					score += scores.get(point);
				} else {
					break;
				}
			}
			if(last!=null) {
				actions.put(score, new MoveTo(my, last));
			}
		}
		PacAction returned = actions.get(actions.firstKey());
		Pac myFuture = returned.transform();
		set(myFuture, myFuture);
		return returned;
	}

	private ImmutablePlayground<Double> buildDistancesScoresFor(Pac my) {
		ImmutablePlayground<Double> scores = zero;
		for(BigPill big : bigPills) {
			scores = scores.apply(usingDistance(big), 
					(a, b) -> a+EvolvableConstants.INTERNAL_SCORE_FOR_BIG_PILL/(1+b), false);
		}
		for(SmallPill small : smallPills) {
			scores = scores.apply(usingDistance(small), 
					(a, b) -> a+EvolvableConstants.INTERNAL_SCORE_FOR_SMALL_PILL/(1+b), false);
		}
		for(Pac pac : allPacs) {
			if(pac.mine) {
/*				if(!pac.equals(my)) {
					scores = scores.apply(usingDistance(pac), 
							(a, b) -> a+EvolvableConstants.INTERNAL_SCORE_FOR_TEAMMATE_TOO_CLOSE/(1+b), false);
				}
*/			} else {
				if(pac.isDangerousFor(my)) {
					scores = scores.apply(usingDistance(pac), 
							(a, b) -> a+EvolvableConstants.INTERNAL_SCORE_FOR_ENEMY_PREDATOR/(1+b), false);
				} else {
					scores = scores.apply(usingDistance(pac),
							(a, b) -> a+EvolvableConstants.INTERNAL_SCORE_FOR_ENEMY_PREY/(1+b), false);
				}
			}
		}
		return scores;
	}

	public void advanceOneTurn() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				set(x, y, get(x, y).advanceOneTurn());
			}
		}
		bigPills.clear();
		smallPills.clear();
		allPacs.clear();
	}

	public String toUnitTestString(int turn) {
		final StringBuilder returned = new StringBuilder();
		
		returned.append(ToUnitTestHelpers.METHOD_PREFIX+"// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)\n");
		returned.append(ToUnitTestHelpers.METHOD_PREFIX+"@Test public void can_find_action_at_turn_")
			.append(turn)
			.append("_")
			.append(System.currentTimeMillis()).append("() {\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX+"Playfield tested = read(Arrays.asList(\n");
		final Collection<String> physical = toStringCollection(new ToPhysicalString());
		final Iterator<String> physicalIter = physical.iterator();
		while (physicalIter.hasNext()) {
			final String row = physicalIter.next();
			returned.append(ToUnitTestHelpers.CONTENT_PREFIX+"\t\"").append(row).append("\"");
			if(physicalIter.hasNext()) {
				returned.append(",");
			}
			returned.append("\n");
		}
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("\t));\n");
		List<Pac> pacs = new ArrayList<>(getAllPacs());
		StringBuffer init = new StringBuffer(ToUnitTestHelpers.CONTENT_PREFIX).append("Pac\n");
		StringBuffer usage = new StringBuffer(ToUnitTestHelpers.CONTENT_PREFIX).append("tested.readGameEntities(");
		for(int index=0; index<pacs.size(); index++) {
			if(index>0) {
				init.append(",\n");
				usage.append(", ");
			}
			Pac pac = pacs.get(index);
			String name = (pac.mine ? "my" : "his")+"_p"+pac.id;
			init.append(ToUnitTestHelpers.CONTENT_PREFIX).append("\t").append(name)
				.append(" = new Pac(")
					.append(pac.x).append(", ")
					.append(pac.y).append(", ")
					.append(pac.id).append(", ")
					.append(pac.mine).append(", ")
					.append("Type.").append(pac.type).append(", ")
					.append(pac.speedTurnsLeft).append(", ")
					.append(pac.abilityCooldown).append(")");
			usage.append(name);
		}
		returned.append(init.append(";\n"));
		returned.append(usage.append(");\n"));
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX+"Map<Pac, PacAction> actions = tested.computeActions(-1);\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX+"assertThat(actions).isNotEmpty();\n");
		returned.append(ToUnitTestHelpers.METHOD_PREFIX+"}\n\n");
		return returned.toString();
	}

	public Set<Pac> getAllPacs() {
		return allPacs;
	}
}
