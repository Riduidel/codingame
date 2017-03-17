package org.ndx.codingame.hypersonic.playground;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.ndx.codingame.gaming.tounittest.ToUnitTestHelpers;
import org.ndx.codingame.hypersonic.EvolvableConstants;
import org.ndx.codingame.hypersonic.entities.Bomb;
import org.ndx.codingame.hypersonic.entities.Box;
import org.ndx.codingame.hypersonic.entities.Content;
import org.ndx.codingame.hypersonic.entities.ContentAdapter;
import org.ndx.codingame.hypersonic.entities.ContentVisitor;
import org.ndx.codingame.hypersonic.entities.Entity;
import org.ndx.codingame.hypersonic.entities.Fire;
import org.ndx.codingame.hypersonic.entities.FireThenItem;
import org.ndx.codingame.hypersonic.entities.Gamer;
import org.ndx.codingame.hypersonic.entities.Item;
import org.ndx.codingame.hypersonic.entities.Nothing;
import org.ndx.codingame.hypersonic.entities.VirtualBomb;
import org.ndx.codingame.hypersonic.entities.Wall;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.lib2d.discrete.PlaygroundAdapter;

public class Playfield extends Playground<Content> {
	public static final class ToCompleteString implements ContentVisitor<String> {
		@Override public String visitBomb(final Bomb bomb) { return "|B("+bomb.delay+","+bomb.range+")"; }
		@Override public String visitNothing(final Nothing nothing) { return "|  .   "; }
		@Override public String visitBox(final Box box) { return "|  0   "; }
		@Override public String visitWall(final Wall wall) { return "|  X   "; }
		@Override public String visitGamer(final Gamer gamer) { return "|G("+gamer.bombs+","+gamer.range+")"; }
		@Override public String visitItem(final Item item) { return "| I("+item.type+") "; }
		@Override public String visitFire(final Fire fire) { return "|  F   "; }
		@Override public String visitFireThenItem(final FireThenItem fire) { return "| F->I "; }
		@Override public String visitVirtualBomb(final VirtualBomb potentialBomb) { return "|V("+potentialBomb.delay+","+potentialBomb.range+")";}
	}
	public static final class ToPhysicalString extends ContentAdapter<String> {
		private ToPhysicalString() {
			super(".");
		}

		@Override public String visitBox(final Box box) { return "0"; }
		@Override public String visitWall(final Wall box) { return "X"; }
	}
	private static PlaygroundDeriver playgroundDeriver = new PlaygroundDeriver();
	private Playfield next;
	private Playground<Integer> opportunities;
	private String physical;
	private List<Item> key;

	public Playfield(final int width, final int height) {
		super(width, height);
	}

	/**
	 * Cloning constructor
	 * @param playground
	 */
	public Playfield(final Playfield playground) {
		super(playground);
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
			set(x, rowIndex, findContentFor(characters[x]));
		}
	}
	private Content findContentFor(final char c) {
		switch(c) {
		case '.':
			return Nothing.instance;
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			return Box.instance;
		case 'X':
			return Wall.instance;
		}
		return null;
	}
	
	public Collection<String> toStringCollection(final ContentVisitor<String> visitor) {
		return accept(new PlaygroundAdapter<Collection<String>, Content>() {
			private StringBuilder row;
			@Override
			public void startVisit(final Playground<Content> playground) {
				returned = new ArrayList<>(playground.height);
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
			@Override
			public void endVisitRow(final int y) {
				returned.add(row.toString());
			}
		});
	}
	
	public String toString(final ContentVisitor<String> visitor) {
		return toStringCollection(visitor).stream().reduce((r1, r2) -> r1+"\n"+r2).get();
	}
	
	public String toPhysicalString() {
		if(physical==null) {
			physical = toString(new ToPhysicalString()); 
		}
		return physical;
	}
	public static class ExportGameEntities extends PlaygroundAdapter<Collection<String>, Content> implements ContentVisitor<String> {
		public ExportGameEntities() {}
		@Override
		public void startVisit(final Playground<Content> playground) {
			returned = new ArrayList<>();
		}
		@Override
		public void visit(final int x, final int y, final Content content) {
			final String text = content.accept(this);
			if(text!=null) {
				returned.add(text);
			}
		}
		@Override public String visitNothing(final Nothing nothing) { return null; }
		@Override public String visitBox(final Box box) { return null; }
		@Override public String visitWall(final Wall wall) { return null; }
		@Override public String visitGamer(final Gamer gamer) {
			return String.format("new Gamer(%d, %d, %d, %d, %d)", gamer.id, gamer.x, gamer.y, gamer.bombs, gamer.range);
		}
		@Override public String visitBomb(final Bomb bomb) {
			return String.format("new Bomb(%d, %d, %d, %d, %d)", bomb.owner, bomb.x, bomb.y, bomb.delay, bomb.range);
		}
		@Override public String visitItem(final Item item) {
			return String.format("new Item(%d, %d, %d, %d, %d)", 0, item.x, item.y, item.type, 0);
		}
		@Override public String visitFire(final Fire fire) { return null; }
		@Override public String visitFireThenItem(final FireThenItem fireThenItem) { return null; }
		@Override public String visitVirtualBomb(final VirtualBomb bomb) {
			return String.format("new PotentialBomb(%d, %d, %d, %d, %d)", bomb.owner, bomb.x, bomb.y, bomb.delay, bomb.range);
		}
	}
	public String toUnitTestString(final Gamer me) {
		final StringBuilder returned = new StringBuilder();
		
		returned.append(ToUnitTestHelpers.METHOD_PREFIX+"// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)\n");
		returned.append(ToUnitTestHelpers.METHOD_PREFIX+"@Test public void can_find_move_")
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
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX+"\t));\n");
		returned.append(String.format(ToUnitTestHelpers.CONTENT_PREFIX+"Gamer me = new Gamer(%d, %d, %d, %d, %d);\n", me.id, me.x, me.y, me.bombs, me.range));
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX+"tested.readGameEntities(\n");
		final Collection<String> entities = accept(new ExportGameEntities());
		final Iterator<String> entitiesIter = entities.iterator();
		while (entitiesIter.hasNext()) {
			final String string = entitiesIter.next();
			returned.append(ToUnitTestHelpers.CONTENT_PREFIX+"\t").append(string);
			if(entitiesIter.hasNext()) {
				returned.append(",");
			}
			returned.append("\n");
		}
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX+"\t);\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX+"assertThat(me.compute(tested)).isNotNull();\n");
		returned.append(ToUnitTestHelpers.METHOD_PREFIX+"}\n\n");
		return returned.toString();
	}
	@Override
	public String toString() {
		return toString(new ToCompleteString());
	}
	public void readGameEntities(final Entity...entities) {
		for (final Entity entity : entities) {
			set(entity.x, entity.y, entity);
		}
	}
	/**
	 * Generate next iteration of playground
	 * @return
	 */
	public Playfield next() {
		if(next==null) {
			next =  accept(playgroundDeriver);
		}
		return next;
	}
	@Override
	public void clear() {
		next = null;
	}

	public Playfield descendant(int i) {
		Playfield p = this;
		while(i>0) {
			p = p.next();
			i--;
		}
		return p;
	}
/*
	public int findDelayBeforeBombFor(int id) {
		return accept(new BombDelayFinder(id));
	}
*/	
	public Playground<Integer> getOpportunitiesAt(final int range) {
		if(opportunities==null) {
			opportunities = createOpportunitiesAt(range);
		}
		return opportunities;
	}

	public Playground<Integer> createOpportunitiesAt(final int range) {
		return descendant(EvolvableConstants.BOMB_DELAY).accept(new OpportunitiesFinder(range));
	}
	
	public void clearOpportunities() {
		opportunities = null;
	}

	public List<Item> getKey() {
		if(key==null) {
			key = getAll(Item.class);
		}
		return key;
	}
}