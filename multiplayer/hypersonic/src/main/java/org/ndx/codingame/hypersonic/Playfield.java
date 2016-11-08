package org.ndx.codingame.hypersonic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.lib2d.discrete.Playground;

public class Playfield extends Playground<Content> {
	public static final class ToCompleteString implements ContentVisitor<String> {
		@Override public String visitBomb(Bomb bomb) { return "|B("+bomb.delay+","+bomb.range+")"; }
		@Override public String visitNothing(Nothing nothing) { return "|  .   "; }
		@Override public String visitBox(Box box) { return "|  0   "; }
		@Override public String visitWall(Wall wall) { return "|  X   "; }
		@Override public String visitGamer(Gamer gamer) { return "|G("+gamer.bombs+","+gamer.range+")"; }
		@Override public String visitItem(Item item) { return "| I("+item.type+") "; }
		@Override public String visitFire(Fire fire) { return "|  F   "; }
		@Override public String visitFireThenItem(FireThenItem fire) { return "| F->I "; }
	}
	public static final class ToPhysicalString extends ContentAdapter<String> {
		private ToPhysicalString() {
			super(".");
		}

		@Override public String visitBox(Box box) { return "0"; }
		@Override public String visitWall(Wall box) { return "X"; }
	}
	private static PlaygroundDeriver playgroundDeriver = new PlaygroundDeriver();
	private Map<Object, Playfield> nextPlaygrounds = new TreeMap<>();

	public Playfield(int width, int height) {
		super(width, height);
	}

	public boolean allow(int p_x, int p_y) {
		if(contains(p_x, p_y)) {
			return get(p_x, p_y).canBeWalkedOn();
		}
		return false;
	}

	public void readRow(String row, int rowIndex) {
		char[] characters = row.toCharArray();
		for (int x = 0; x < characters.length; x++) {
			set(x, rowIndex, findContentFor(characters[x]));
		}
	}
	private Content findContentFor(char c) {
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
	
	public <Type> Type accept(PlaygroundVisitor<Type> visitor) {
		visitor.startVisit(this);
		for (int y = 0; y < height; y++) {
			visitor.startVisitRow(y);
			for (int x = 0; x < width; x++) {
				visitor.visit(x, y, get(x, y));
			}
			visitor.endVisitRow(y);
		}
		return visitor.endVisit(this);
	}
	
	public Collection<String> toStringCollection(final ContentVisitor<String> visitor) {
		return accept(new PlaygroundAdapter<Collection<String>>() {
			private StringBuilder row;
			@Override
			public void startVisit(Playfield playground) {
				returned = new ArrayList<>(playground.height);
			}
			@Override
			public void startVisitRow(int y) {
				row = new StringBuilder();
			}
			@Override
			public void visit(int x, int y, Content content) {
				row.append(content.accept(visitor));
			}
			@Override
			public void endVisitRow(int y) {
				returned.add(row.toString());
			}
		});
	}
	
	public String toString(ContentVisitor<String> visitor) {
		return toStringCollection(visitor).stream().reduce((r1, r2) -> r1+"\n"+r2).get();
	}
	
	public String toPhysicialString() {
		return toString(new ToPhysicalString());
	}
	public static class ExportGameEntities extends PlaygroundAdapter<Collection<String>> implements ContentVisitor<String> {
		public ExportGameEntities() {}
		@Override
		public void startVisit(Playfield playground) {
			returned = new ArrayList<>();
		}
		@Override
		public void visit(int x, int y, Content content) {
			String text = content.accept(this);
			if(text!=null) {
				returned.add(text);
			}
		}
		@Override public String visitNothing(Nothing nothing) { return null; }
		@Override public String visitBox(Box box) { return null; }
		@Override public String visitWall(Wall wall) { return null; }
		@Override public String visitGamer(Gamer gamer) {
			String prefix = gamer.id==0 ? "me = " : "";
			return String.format("%snew Player.Gamer(%d, %d, %d, %d, %d)", prefix, gamer.id, gamer.x, gamer.y, gamer.bombs, gamer.range);
		}
		@Override public String visitBomb(Bomb bomb) {
			return String.format("new Player.Bomb(%d, %d, %d, %d, %d)", bomb.owner, bomb.x, bomb.y, bomb.delay, bomb.range);
		}
		@Override public String visitItem(Item item) {
			return String.format("new Player.Item(%d, %d, %d, %d, %d)", 0, item.x, item.y, item.type, 0);
		}
		@Override public String visitFire(Fire fire) { return null; }
		@Override public String visitFireThenItem(FireThenItem fireThenItem) { return null; }
	}
	public String toUnitTestString() {
		final StringBuilder returned = new StringBuilder();
		returned.append("\t\t\t@Test public void can_find_move_")
			.append(System.currentTimeMillis()).append("() {\n");
		returned.append("\t\t\t\tPlayer.Delay delay = new Player.Delay();\n");
		returned.append("\t\t\t\tPlayer.Playground tested = read(Arrays.asList(\n");
		Collection<String> physical = toStringCollection(new ToPhysicalString());
		Iterator<String> physicalIter = physical.iterator();
		while (physicalIter.hasNext()) {
			String row = (String) physicalIter.next();
			returned.append("\t\t\t\t\t\"").append(row).append("\"");
			if(physicalIter.hasNext()) {
				returned.append(",");
			}
			returned.append("\n");
		}
		returned.append("\t\t\t\t\t));\n");
		returned.append("\t\t\t\tPlayer.Gamer me = null;\n");
		returned.append("\t\t\t\ttested.readGameEntities(\n");
		Collection<String> entities = accept(new ExportGameEntities());
		Iterator<String> entitiesIter = entities.iterator();
		while (entitiesIter.hasNext()) {
			String string = entitiesIter.next();
			returned.append("\t\t\t\t\t").append(string);
			if(entitiesIter.hasNext())
				returned.append(",");
			returned.append("\n");
		}
		returned.append("\t\t\t\t\t);\n");
		returned.append("\t\t\t\tPlayer.Trajectory best = new Player.TrajectoryBuilder(")
			.append("\t\t\t\t\t\ttested,\n")
			.append("\t\t\t\t\t\tdelay,\n")
			.append("\t\t\t\t\t\tnew Player.EvolvableConstants())\n")
			.append("\t\t\t\t\t.findBest(me);\n");
		returned.append("\t\t\t}\n");
		return returned.toString();
	}
	public String toString() {
		return toString(new ToCompleteString());
	}
	public void readGameEntities(Entity...entities) {
		for (Entity entity : entities) {
			set(entity.x, entity.y, entity);
		}
	}
	/**
	 * Generate next iteration of playground
	 * @return
	 */
	public Playfield next() {
		if(!nextPlaygrounds.containsKey("playground")) {
			nextPlaygrounds.put("playground", accept(playgroundDeriver));
		}
		return nextPlaygrounds.get("playground");
	}
	public void clear() {
		nextPlaygrounds.clear();
	}

	public Playfield descendant(int i) {
		Playfield p = this;
		while(i>0) {
			p = p.next();
			i--;
		}
		return p;
	}
}