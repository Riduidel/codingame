package org.ndx.codingame.spring.challenge.playground;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.ndx.codingame.gaming.tounittest.ToUnitTestHelpers;
import org.ndx.codingame.lib2d.ImmutablePlayground;
import org.ndx.codingame.lib2d.MutablePlayground;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.lib2d.discrete.PlaygroundAdapter;
import org.ndx.codingame.spring.challenge.entities.BigPill;
import org.ndx.codingame.spring.challenge.entities.Content;
import org.ndx.codingame.spring.challenge.entities.ContentAdapter;
import org.ndx.codingame.spring.challenge.entities.ContentVisitor;
import org.ndx.codingame.spring.challenge.entities.Ground;
import org.ndx.codingame.spring.challenge.entities.Nothing;
import org.ndx.codingame.spring.challenge.entities.Pac;
import org.ndx.codingame.spring.challenge.entities.SmallPill;
import org.ndx.codingame.spring.challenge.entities.Wall;

public interface SpringChallengePlayground extends MutablePlayground<Content> {
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
	}
	
	public default DiscretePoint putBackOnPlayground(DiscretePoint point) {
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

	public default boolean allow(final DiscretePoint position) {
		return allow(position.x, position.y);
	}

	public default boolean allow(final int p_x, final int p_y) {
		if(contains(p_x, p_y)) {
			return get(p_x, p_y).canBeWalkedOn();
		}
		return false;
	}

	public default void readRow(final String row, final int rowIndex) {
		final char[] characters = row.toCharArray();
		for (int x = 0; x < characters.length; x++) {
			char character = characters[x];
			Content content = null;
			switch(character) {
			case Ground.CHARACTER: content = Ground.instance; break;
			case Wall.CHARACTER: content = Wall.instance; break;
			case BigPill.CHARACTER: content = new BigPill(x, rowIndex); break;
			case SmallPill.CHARACTER: content = new SmallPill(x, rowIndex); break;
			}
			set(x, rowIndex, content);
		}
	}
	
	public default String toPhysicalString() {
		return toString(new ToPhysicalString());
	}

	public default String toString(final ContentVisitor<String> visitor) {
		return toStringCollection(visitor).stream().reduce((r1, r2) -> r1+"\n"+r2).get();
	}

	public default Collection<String> toStringCollection(final ContentVisitor<String> visitor) {
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

	public default String toUnitTestString() {
		final StringBuilder returned = new StringBuilder();
		
		returned.append(ToUnitTestHelpers.METHOD_PREFIX+"// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)\n");
		returned.append(ToUnitTestHelpers.METHOD_PREFIX+"@Test public void can_find_move_")
			.append(System.currentTimeMillis()).append("() {\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX+"Turn tested = read(Arrays.asList(\n");
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
		List<Pac> pacs = getAll(Pac.class);
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

	public <Type extends Content> List<Type> getAll(Class<Type> class1);

	public List<List<DiscretePoint>> speedPointsAt(DiscretePoint p);

	public List<DiscretePoint> nextPointsAt(DiscretePoint p);

	public Playground<Integer> zero();
	public ScoringSystem cacheDistanceMapTo(BigPill pill);

}
