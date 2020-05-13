package org.ndx.codingame.spring.challenge.playground;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.ndx.codingame.gaming.tounittest.ToUnitTestHelpers;
import org.ndx.codingame.lib2d.ImmutablePlayground;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.lib2d.discrete.PlaygroundAdapter;
import org.ndx.codingame.spring.challenge.EvolvableConstants;
import org.ndx.codingame.spring.challenge.actions.MoveTo;
import org.ndx.codingame.spring.challenge.actions.PacAction;
import org.ndx.codingame.spring.challenge.actions.Speed;
import org.ndx.codingame.spring.challenge.entities.AbstractDistinctContent;
import org.ndx.codingame.spring.challenge.entities.BigPill;
import org.ndx.codingame.spring.challenge.entities.Content;
import org.ndx.codingame.spring.challenge.entities.ContentAdapter;
import org.ndx.codingame.spring.challenge.entities.ContentVisitor;
import org.ndx.codingame.spring.challenge.entities.Ground;
import org.ndx.codingame.spring.challenge.entities.Nothing;
import org.ndx.codingame.spring.challenge.entities.Pac;
import org.ndx.codingame.spring.challenge.entities.PacTrace;
import org.ndx.codingame.spring.challenge.entities.PotentialSmallPill;
import org.ndx.codingame.spring.challenge.entities.SmallPill;
import org.ndx.codingame.spring.challenge.entities.Wall;

public class Playfield extends Playground<Content> {

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

		public String visitPacTrace(PacTrace pacTrace) {
			return " ";
		}

	}

	public DiscretePoint putBackOnPlayground(DiscretePoint point) {
		int x = point.x, y = point.y;
		if (x < 0) {
			x = getWidth() - 1;
		} else if (x >= getWidth()) {
			x = 0;
		}
		if (y < 0) {
			y = getHeight() - 1;
		} else if (y >= getHeight()) {
			y = 0;
		}
		return new DiscretePoint(x, y);
	}

	public boolean allow(final DiscretePoint position) {
		return allow(position.x, position.y);
	}

	public boolean allow(final int p_x, final int p_y) {
		if (contains(p_x, p_y)) {
			return get(p_x, p_y).canBeWalkedOn();
		}
		return false;
	}

	public void readRow(final String row, final int rowIndex) {
		final char[] characters = row.toCharArray();
		for (int x = 0; x < characters.length; x++) {
			char character = characters[x];
			Content content = null;
			switch (character) {
			case Ground.CHARACTER:
				content = Ground.instance;
				break;
			case Wall.CHARACTER:
				content = Wall.instance;
				break;
			case BigPill.CHARACTER:
				content = new BigPill(x, rowIndex);
				break;
			case SmallPill.CHARACTER:
				content = new SmallPill(x, rowIndex);
				break;
			case PotentialSmallPill.CHARACTER:
				content = PotentialSmallPill.instance;
				break;
			}
			set(x, rowIndex, content);
		}
	}

	public String toString(final ContentVisitor<String> visitor) {
		return toStringCollection(visitor).stream().reduce((r1, r2) -> r1 + "\n" + r2).get();
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
				if (content == null) {
					row.append(Nothing.instance.accept(visitor));
				} else {
					row.append(content.accept(visitor));
				}
			}
		});
	}

	private Map<BigPill, ScoringSystem> bigPillsInfos = new HashMap<>();
	public final Playground<Double> zero;

	Set<SmallPill> smallPills = new HashSet<>();
	Set<BigPill> bigPills = new HashSet<>();
	Set<Pac> allPacs = new HashSet<Pac>();
	private Cache cache;

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
		cache = new Cache(this);
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
		return toCommands(computeActions());
	}

	/**
	 * Update playground by first clearing all view range excepted explicit elements
	 */
	private void updatePlayground() {
		for (Pac pac : getMyPacs()) {
			for (List<DiscretePoint> directions : cache.nextPointsCache.get(cache.NEXT_POINTS_VIEW_RANGE).get(pac)) {
				for (DiscretePoint point : directions) {
					if (get(point) instanceof PotentialSmallPill) {
						set(point, Ground.instance);
					}
				}
			}
		}
	}

	/**
	 * maximum is a kind of deepness we used in real turn
	 * 
	 * @return
	 */
	public Map<Pac, PacAction> computeActions() {
		updatePlayground();
		Map<Pac, PacAction> returned = new HashMap<>();
		for (Pac my : getMyPacs()) {
			returned.put(my, computeActionFor(my));
		}
		return returned;
	}

	private ImmutablePlayground<Double> usingDistance(DiscretePoint system) {
		return cache.distancesToPoints.get(system).distancesOnPlaygroundSquared;
	}

	private PacAction computeActionFor(Pac my) {
		set(my, Ground.instance);
		ImmutablePlayground<Double> scores = buildDistancesScoresForPills(my);
		scores = scores.apply(buildDistancesScoresForPacs(my),
				(a, b) -> a+b);
		SortedMap<Double, PacAction> actions = new TreeMap<>(Comparator.reverseOrder());
		if (my.abilityCooldown == 0) {
			actions.putAll(computeSpeed(my, scores));
		}
		actions.putAll(computeActionMapFor(my, scores));
		Double bestScore = actions.firstKey();
		PacAction returned = actions.get(bestScore);
		Pac myFuture = returned.transform();
		set(myFuture, myFuture);
		return returned.withMessage("s=" + bestScore);
	}

	private Map<? extends Double, ? extends PacAction> computeSpeed(Pac my, ImmutablePlayground<Double> scores) {
		Speed speed = new Speed(my);
		SortedMap<Double, PacAction> actions = computeActionMapFor(speed.transform(), scores);
		SortedMap<Double, PacAction> returned = new TreeMap<>(Comparator.reverseOrder());
		returned.put(actions.firstKey(), speed);
		return returned;
	}

	private SortedMap<Double, PacAction> computeActionMapFor(Pac my, ImmutablePlayground<Double> scores) {
		SortedMap<Double, PacAction> actions = new TreeMap<>(Comparator.reverseOrder());
		List<List<DiscretePoint>> accessibleNextPoints = cache.nextPointsCache
				.get(my.speedTurnsLeft > 0 ? Cache.NEXT_POINTS_SPEED : Cache.NEXT_POINTS_NORMAL).get(my);
		for (List<DiscretePoint> direction : accessibleNextPoints) {
			double score = 0;
			DiscretePoint last = null;
			for (DiscretePoint point : direction) {
				if (get(point).canBeWalkedOn()) {
					last = point;
					score = scores.get(point);
				} else {
					break;
				}
			}
			if (last != null) {
				actions.put(score, new MoveTo(my, last));
			}
		}
		return actions;
	}

	private ImmutablePlayground<Double> buildDistancesScoresForPacs(Pac my) {
		ImmutablePlayground<Double> scores = zero;
		for (Pac pac : allPacs) {
		}
		return scores;
	}

	private ImmutablePlayground<Double> buildDistancesScoresForPills(Pac my) {
		ImmutablePlayground<Double> scores = zero;
		for (BigPill big : bigPills) {
			scores = scores.apply(usingDistance(big),
					(a, b) -> a + EvolvableConstants.INTERNAL_SCORE_FOR_BIG_PILL / (1 + b), false);
		}
		for (SmallPill small : smallPills) {
			scores = scores.apply(usingDistance(small),
					(a, b) -> a + EvolvableConstants.INTERNAL_SCORE_FOR_SMALL_PILL / (1 + b), false);
		}
		if (bigPills.isEmpty() && smallPills.isEmpty()) {
			// Now add the n nearest potential pills
			int usablePotentialPillsCount = 0;
			DiscretePoint preceding = null;
			for (DiscretePoint point : cache.nearestPoints.get(my)) {
				if (PotentialSmallPill.instance == get(point)) {
					if(preceding!=null) {
						// We want to target the nearest cluster of points
						if(preceding.distance1To(point)>1) {
							break;
						}
					}
					scores = scores.apply(usingDistance(point),
							(a, b) -> a + EvolvableConstants.INTERNAL_SCORE_FOR_POTENTIAL_SMALL_PILL / (1 + b), false);
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

		returned.append(ToUnitTestHelpers.METHOD_PREFIX
				+ "// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)\n");
		returned.append(ToUnitTestHelpers.METHOD_PREFIX + "@Test public void can_find_action_at_turn_").append(turn)
				.append("_").append(System.currentTimeMillis()).append("() {\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX + "Playfield tested = read(Arrays.asList(\n");
		final Collection<String> physical = toStringCollection(new ToPhysicalString());
		final Iterator<String> physicalIter = physical.iterator();
		while (physicalIter.hasNext()) {
			final String row = physicalIter.next();
			returned.append(ToUnitTestHelpers.CONTENT_PREFIX + "\t\"").append(row).append("\"");
			if (physicalIter.hasNext()) {
				returned.append(",");
			}
			returned.append("\n");
		}
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("\t));\n");
		List<Pac> pacs = new ArrayList<>(getAllPacs());
		StringBuffer init = new StringBuffer(ToUnitTestHelpers.CONTENT_PREFIX).append("Pac\n");
		StringBuffer usage = new StringBuffer(ToUnitTestHelpers.CONTENT_PREFIX).append("tested.readGameEntities(");
		for (int index = 0; index < pacs.size(); index++) {
			if (index > 0) {
				init.append(",\n");
				usage.append(", ");
			}
			Pac pac = pacs.get(index);
			String name = (pac.mine ? "my" : "his") + "_p" + pac.id;
			init.append(ToUnitTestHelpers.CONTENT_PREFIX).append("\t").append(name).append(" = new Pac(").append(pac.x)
					.append(", ").append(pac.y).append(", ").append(pac.id).append(", ").append(pac.mine).append(", ")
					.append("Type.").append(pac.type).append(", ").append(pac.speedTurnsLeft).append(", ")
					.append(pac.abilityCooldown).append(")");
			usage.append(name);
		}
		returned.append(init.append(";\n"));
		returned.append(usage.append(");\n"));
		returned.append(
				ToUnitTestHelpers.CONTENT_PREFIX + "Map<Pac, PacAction> actions = tested.computeActions();\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX + "assertThat(actions).isNotEmpty();\n");
		returned.append(ToUnitTestHelpers.METHOD_PREFIX + "}\n\n");
		return returned.toString();
	}

	public Set<Pac> getAllPacs() {
		return allPacs;
	}
}
