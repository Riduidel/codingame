package org.ndx.codingame.spring.challenge.playground;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.ndx.codingame.gaming.Delay;
import org.ndx.codingame.gaming.tounittest.ToUnitTestHelpers;
import org.ndx.codingame.lib2d.ImmutablePlayground;
import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.spring.challenge.EvolvableConstants;
import org.ndx.codingame.spring.challenge.actions.ActionTree;
import org.ndx.codingame.spring.challenge.actions.PacAction;
import org.ndx.codingame.spring.challenge.entities.AbstractDistinctContent;
import org.ndx.codingame.spring.challenge.entities.BigPill;
import org.ndx.codingame.spring.challenge.entities.Content;
import org.ndx.codingame.spring.challenge.entities.ContentAdapter;
import org.ndx.codingame.spring.challenge.entities.Ground;
import org.ndx.codingame.spring.challenge.entities.Pac;
import org.ndx.codingame.spring.challenge.entities.PotentialSmallPill;
import org.ndx.codingame.spring.challenge.entities.SmallPill;
import org.ndx.codingame.spring.challenge.entities.Wall;

public class Playfield extends Playground<Content> implements SpringChallengePlayground {
	public static final class ToPhysicalString extends ContentAdapter<String> {
		private ToPhysicalString() {
			super("X");
		}
		@Override
		public String visitBigPill(BigPill bigPill) {
			return Character.toString(BigPill.CHARACTER);
		}
		@Override
		public String visitGround(Ground ground) {
			return Character.toString(Ground.CHARACTER);
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
			return Character.toString(SmallPill.CHARACTER);
		}
		@Override
		public String visitWall(Wall wall) {
			return Character.toString(Wall.CHARACTER);
		}
		public String visitPotentialSmallPill(PotentialSmallPill potentialSmallPill) {
			return Character.toString(PotentialSmallPill.CHARACTER);
		};
	}
	

	
	private Map<BigPill, ScoringSystem> bigPillsInfos = new HashMap<>();
	private Playground<Integer> zero;
	private Playground<List<DiscretePoint>> nextPointsForNormal;
	private Playground<List<List<DiscretePoint>>> nextPointsForSpeed;
	private Playground<List<List<DiscretePoint>>> nextPointsForViewRange;

	private Set<SmallPill> smallPills = new HashSet<>();
	private Set<BigPill> bigPills = new HashSet<>();
	private Set<Pac> pacs = new HashSet<Pac>();

	public Playfield(final int width, final int height) {
		super(width, height, PotentialSmallPill.instance);
	}

	/**
	 * Cloning constructor
	 * 
	 * @param playground
	 */
	public Playfield(final Playfield playground) {
		super(playground);
	}

	@Override
	public void set(DiscretePoint p, Content c) {
		setSpecificContent(get(p), c);
		super.set(p, c);
	}

	private void setSpecificContent(Content previous, Content next) {
		if (next instanceof BigPill) {
			bigPills.add((BigPill) next);
		} else if (next instanceof Pac) {
			pacs.add((Pac) next);
		} else if (next instanceof SmallPill) {
			smallPills.add((SmallPill) next);
		} else if (next instanceof Ground) {
			if (previous instanceof BigPill) {
				bigPills.remove((BigPill) previous);
			} else if (next instanceof Pac) {
				pacs.remove((Pac) previous);
			}
		}
	}

	@Override
	public void set(int x, int y, Content c) {
		setSpecificContent(get(x, y), c);
		super.set(x, y, c);
	}

	public void init() {
		// Force init of zero !
		zero();
		// Compute valid directions for each point
		Playground<List<Direction>> directions = computePossibleDirections();
		nextPointsForNormal = computeNextPointsForNormal(directions);
		nextPointsForSpeed = computeNextPointsList(directions, 2);
		nextPointsForViewRange = computeNextPointsList(directions, Integer.MAX_VALUE);
	}

	private Playground<List<DiscretePoint>> computeNextPointsForNormal(Playground<List<Direction>> directions) {
		Playground<List<DiscretePoint>> points = new Playground<>(width, height);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				List<Direction> directionsFor = directions.get(x, y);
				DiscretePoint p = new DiscretePoint(x, y);
				List<DiscretePoint> successors = new LinkedList<>();
				for (Direction d : directionsFor) {
					DiscretePoint next = putBackOnPlayground(d.move(p));
					if (get(next).canBeWalkedOn()) {
						successors.add(next);
					}
				}
				points.set(p, successors);
			}
		}
		return points;
	}

	private Playground<List<List<DiscretePoint>>> computeNextPointsList(Playground<List<Direction>> directions,
			int limit) {
		Playground<List<List<DiscretePoint>>> points = new Playground<>(width, height);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				List<Direction> directionsFor = directions.get(x, y);
				DiscretePoint p = new DiscretePoint(x, y);
				List<List<DiscretePoint>> successors = new LinkedList<>();
				for (Direction d : directionsFor) {
					List<DiscretePoint> pointsInThatDirection = new LinkedList<>();
					DiscretePoint point = p;
					while (get(point).canBeWalkedOn()) {
						pointsInThatDirection.add(point);
						point = putBackOnPlayground(d.move(point));
					}
					successors.add(pointsInThatDirection);
				}
				points.set(p, successors);
			}
		}
		return points;
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

	@Override
	public ScoringSystem cacheDistanceMapTo(BigPill c) {
		if (!bigPillsInfos.containsKey(c)) {
			bigPillsInfos.put(c, computeScoringInfos(c, EvolvableConstants.HORIZON_FOR_BIG_PILLS));
		}
		return bigPillsInfos.get(c);
	}

	protected ScoringSystem computeScoringInfos(AbstractDistinctContent c, int limit) {
		return new ScoringSystem(c, computeDistancesTo(c, limit));
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

	@Override
	public Playground<Integer> zero() {
		if (zero == null) {
			zero = new Playground<Integer>(width, height, 0);
		}
		return zero;
	}

	public List<List<DiscretePoint>> speedPointsAt(DiscretePoint p) {
		return nextPointsForSpeed.get(p);
	}

	public List<DiscretePoint> nextPointsAt(DiscretePoint p) {
		return nextPointsForNormal.get(p);
	}

	private List<Pac> getMyPacs() {
		List<Pac> returned = new ArrayList<>();
		for (Pac pac : pacs) {
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
		updatePlayground();
		Map<Pac, PacAction> actions = computeActions(-1);
		return toCommands(actions);
	}

	/**
	 * Update playground by first clearing all view range excepted explicit elements
	 */
	private void updatePlayground() {
		for (Pac pac : getMyPacs()) {
			for (List<DiscretePoint> directions : nextPointsForViewRange.get(pac)) {
				for (DiscretePoint point : directions) {
					if(get(point) instanceof PotentialSmallPill) {
						set(point, Ground.instance);
					}
				}
			}
		}
	}

	public Map<Pac, PacAction> computeActions(int maximum) {
		Turn turn = new Turn(this, getBigPillsDistances());
		List<Pac> myPacs = getMyPacs();
		Map<Pac, ActionTree> actionList = new HashMap<>();
		for (Pac pac : myPacs) {
			actionList.put(pac, new ActionTree(this, turn, pac, pac, 0));
		}
		int computations = 0;
		Delay delay = new Delay();
		do {
			for (Pac pac : myPacs) {
				actionList.get(pac).grow();
			}
			computations++;
		} while ((maximum < 0 && !delay.isElapsed(EvolvableConstants.DELAY_FOR_PREDICTION)) || computations <= maximum);

		Map<Pac, PacAction> returned = new HashMap<>();
		for (Map.Entry<Pac, ActionTree> action : actionList.entrySet()) {
			ActionTree actionTree = action.getValue();
			returned.put(action.getKey(), actionTree.getFirstAction()
					.withMessage(String.format("c=%d;s=%d", computations, actionTree.score())));
		}
		return returned;
	}

	public void advanceOneTurn() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				set(x, y, get(x, y).advanceOneTurn());
			}
		}
		bigPills.clear();
		smallPills.clear();
		pacs.clear();
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
		List<Pac> pacs = getMyPacs();
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

	@Override
	public ImmutablePlayground<Integer> getBigPillsDistances() {
		ImmutablePlayground<Integer> returned = zero();
		for(BigPill big : bigPills) {
			returned = returned.apply(cacheDistanceMapTo(big).scores, (a, b) -> a+b);
		}
		return returned;
	}

}
