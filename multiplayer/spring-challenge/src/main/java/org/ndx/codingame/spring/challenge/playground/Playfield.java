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

import org.ndx.codingame.gaming.Delay;
import org.ndx.codingame.gaming.tounittest.ToUnitTestHelpers;
import org.ndx.codingame.lib2d.ImmutablePlayground;
import org.ndx.codingame.lib2d.MutablePlayground;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.lib2d.discrete.PlaygroundAdapter;
import org.ndx.codingame.spring.challenge.EvolvableConstants;
import org.ndx.codingame.spring.challenge.actions.MoveTo;
import org.ndx.codingame.spring.challenge.actions.PacAction;
import org.ndx.codingame.spring.challenge.actions.Speed;
import org.ndx.codingame.spring.challenge.entities.AbstractDistinctContent;
import org.ndx.codingame.spring.challenge.entities.AbstractPac;
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
import org.ndx.codingame.spring.challenge.entities.Type;
import org.ndx.codingame.spring.challenge.entities.Wall;
import org.ndx.codingame.spring.challenge.predictor.Predictor;

public class Playfield extends Playground<Content> implements SpringPlayfield {

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
			return pacTrace.toString();
		}

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
			case PacTrace.MINE:
				content = new PacTrace(x, rowIndex, -1, true, Type.DEAD, 0, 0);
				break;
			case PacTrace.ENEMY:
				content = new PacTrace(x, rowIndex, -1, true, Type.DEAD, 0, 0);
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

	public final Playground<Double> zero;

	private Set<SmallPill> smallPills = new HashSet<>();
	private Set<BigPill> bigPills = new HashSet<>();
	private Set<Pac> allPacs = new HashSet<Pac>();
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

	public void setSpecificContent(DiscretePoint location, Content previous, Content next) {
		next.accept(new SpecificContentSetter(this, previous, location, next));
	}

	public void init() {
		Delay delay = new Delay();
		cache = new Cache(this);
		cache.loadPointsByDistanceUntil(delay, 900);
		System.err.println("init took "+delay.howLong()+ "ms");
	}

	public String toCommands(Map<Pac, PacAction> actions) {
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

	/**
	 * Update playground by first clearing all view range excepted explicit elements
	 */
	private void updatePlayground() {
		Delay turnDuration = new Delay();
		for (AbstractPac pac : getMyPacs()) {
			for (List<DiscretePoint> directions : cache.getNextPointsCache(cache.NEXT_POINTS_VIEW_RANGE).get(pac)) {
				for (DiscretePoint point : directions) {
					Content content = get(point);
					if(Ground.instance.equals(content)) {
						// Pills don't reveal ground, so we have to take care of them by hand
						// There is a good reason they don't reveal ground, it's to be able to memorize them
						if(smallPills.contains(point)) {
							set(point, Ground.instance);
						}
					} else	if (content.revealsGround()) {
						set(point, Ground.instance);
					} else if(content instanceof SmallPill) {
						if(!smallPills.contains(content)) {
							set(point, Ground.instance);
						}
					} else {
					}
				}
			}
		}
		System.err.println("upading playground took "+turnDuration.howLong()+"ms");
	}

	/**
	 * maximum is a kind of deepness we used in real turn
	 * 
	 * @return
	 */
	public Map<Pac, PacAction> computeActions(int iterations) {
		Delay turnDuration = new Delay();
		try {
			updatePlayground();
			int count = 0;
			Predictor predictor = new Predictor(this);
			boolean continueToGrow;
			do {
				continueToGrow = predictor.grow(count++);
			} while(continueToGrow  &&
					((iterations<0 && turnDuration.howLong()<EvolvableConstants.DELAY_FOR_PREDICTION)
					||
					(iterations>0 && count<iterations)));
			String message = "c="+count+";";
			if(!continueToGrow) {
				message+="horizon reached!";
			}
			return predictor.getBestPredictions(message);
		} finally {
			System.err.println("Full computation took "+turnDuration.howLong()+"ms");
		}
	}

	public void advanceOneTurn() {
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
			AbstractPac pac = pacs.get(index);
			String name = (pac.mine ? "my" : "his") + "_p" + pac.id;
			init.append(ToUnitTestHelpers.CONTENT_PREFIX).append("\t").append(name).append(" = new Pac(").append(pac.x)
					.append(", ").append(pac.y).append(", ").append(pac.id).append(", ").append(pac.mine).append(", ")
					.append("Type.").append(pac.type).append(", ").append(pac.speedTurnsLeft).append(", ")
					.append(pac.abilityCooldown).append(")").append(",\n");
			usage.append(name).append(", ");
		}
		// Remove last "," and replace it by ";"
		init.replace(init.length()-2, init.length()-1, ";");
		List<BigPill> bigs = new ArrayList<>(bigPills);
		if(!bigs.isEmpty()) {
			usage.append("\n").append(ToUnitTestHelpers.CONTENT_PREFIX);
			init.append(ToUnitTestHelpers.CONTENT_PREFIX).append("BigPill\n");
			for (int index = 0; index < bigPills.size(); index++) {
				BigPill b = bigs.get(index);
				String name = "big_" + index;
				init.append(ToUnitTestHelpers.CONTENT_PREFIX).append("\t").append(name)
					.append(" = new BigPill(").append(b.x).append(", ").append(b.y).append("),\n");
				usage.append(name).append(", ");
			}
			init.replace(init.length()-2, init.length()-1, ";");
		}
		List<SmallPill> small = new ArrayList<>(smallPills);
		if(!small.isEmpty()) {
			usage.append("\n").append(ToUnitTestHelpers.CONTENT_PREFIX);
			init.append(ToUnitTestHelpers.CONTENT_PREFIX).append("SmallPill\n");
			for (int index = 0; index < small.size(); index++) {
				SmallPill b = small.get(index);
				String name = "small_" + index;
				init.append(ToUnitTestHelpers.CONTENT_PREFIX).append("\t").append(name)
					.append(" = new SmallPill(").append(b.x).append(", ").append(b.y).append("),\n");
				usage.append(name).append(", ");
			}
			init.replace(init.length()-2, init.length(), ";");
		}
		usage.replace(usage.length()-2, usage.length()-1, "");
		returned.append(init.append(";\n"));
		returned.append(usage.append(");\n"));
		returned.append(
				ToUnitTestHelpers.CONTENT_PREFIX + "Map<Pac, PacAction> actions = tested.computeActions(/* TODO replace by pac value */-1);\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX + "assertThat(actions).isNotEmpty();\n");
		returned.append(ToUnitTestHelpers.METHOD_PREFIX + "}\n\n");
		return returned.toString();
	}

	public void terminateNearestPointsLoading() {
		cache.loadPointsByDistanceUntil(new Delay(), 200);
	}
	
	public Set<Pac> getAllPacs() {
		return allPacs;
	}

	public Playground<Double> getZero() {
		return zero;
	}

	public Set<SmallPill> getSmallPills() {
		return smallPills;
	}

	public Set<BigPill> getBigPills() {
		return bigPills;
	}

	public Cache getCache() {
		return cache;
	}

	/**
	 * Apply pac actions and clean the ground
	 */
	public void terminateTurn(Map<Pac, PacAction> actions) {
		for(PacAction action : actions.values()) {
			action.update(this);
		}
		for(BigPill big : new ArrayList<>(bigPills)) {
			set(big, Ground.instance);
		}
		// Don't forget to load cache of next points
		terminateNearestPointsLoading();
	}
}
