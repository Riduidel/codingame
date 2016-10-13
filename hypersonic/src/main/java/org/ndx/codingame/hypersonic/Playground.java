package org.ndx.codingame.hypersonic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Playground {
	public static final class ToCompleteString implements ContentVisitor<String> {
		@Override public String visitBomb(Bomb bomb) { return "|B("+bomb.delay+","+bomb.range+")"; }
		@Override public String visitNothing(Nothing nothing) { return "|  .   "; }
		@Override public String visitBombDanger(BombDanger nothing) { 
			return "|  !   ";
		}
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
	public final int width;
	public final int height;
	/** Do not forget to exchange x and y to access that array */
	private Content[][] positions;
	private Map<Object, Playground> nextPlaygrounds = new TreeMap<>();
	private Map<String, int[][]> availableDirections = new TreeMap<>();
	private Map<String, Step> knownSteps = new TreeMap<>();
	public Playground(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		this.positions = new Content[height][];
		for (int y = 0; y < positions.length; y++) {
			positions[y] = new Content[width];
		}
	}
	public Step getStep(Action nextAction, int x, int y, Gamer me, Entity current, EvolvableConstants constants) {
		String key = current.x+"; "+current.y+"=>"+nextAction.name()+"@"+x+";"+y;
		if(!knownSteps.containsKey(key)) {
			Step s = new Step(nextAction, x, y, me, current).computeScore(this, constants);
			knownSteps.put(key, s);
		}
		return knownSteps.get(key);
	}
	public int[][] getAvailableDirectionsAt(Entity current) {
		String key = current.x+";"+current.y;
		if(availableDirections.get(key)==null) {
			// This array directly contains new positions, and not updates we should recompute next
			List<int[]> availableDirectionsFor = new ArrayList<>(5);
			for (int direction = 0; direction < Player.POSSIBLE_DIRECTIONS.length; direction++) {
				int[] currentDir = Player.POSSIBLE_DIRECTIONS[direction];
				int p_x = current.x+currentDir[0];
				int p_y = current.y+currentDir[1];
				if(allow(p_x, p_y)) {
					availableDirectionsFor.add(new int[]{p_x, p_y});
				}
			}
			availableDirections.put(key, availableDirectionsFor.toArray(new int[availableDirectionsFor.size()][]));

		}
		return availableDirections.get(key);
	}
	public boolean contains(int p_x, int p_y) {
		if(p_x<0 || p_x>=width) {
			return false;
		}
		if(p_y<0 || p_y>=height) {
			return false;
		}
		return true;
	}
	public boolean allow(int p_x, int p_y) {
		if(contains(p_x, p_y)) {
			return get(p_x, p_y).canBeWalkedOn();
		}
		return false;
	}
	public Content get(int x, int y) {
		Content[] row = positions[y];
		return row[x];
	}
	public Content set(int x, int y, Content content) {
		Content returned = positions[y][x]; 
		positions[y][x] = content;
		return returned;
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
			public void startVisit(Playground playground) {
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
		public void startVisit(Playground playground) {
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
		@Override public String visitBombDanger(BombDanger nothing) { return null; }
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
	public Playground next() {
		if(!nextPlaygrounds.containsKey("playground")) {
			nextPlaygrounds.put("playground", accept(playgroundDeriver));
		}
		return nextPlaygrounds.get("playground");
	}
	/**
	 * Derive a playground taking in account the given step.
	 * For that, step is put at its current location, playground is derived, then 
	 * @param step
	 * @return
	 */
	public Playground next(Step step) {
		String key = step.toString();
		if(!nextPlaygrounds.containsKey(key)) {
			Content current = get(step.current.x, step.current.y);
			step.mutateContent(this, current);
			nextPlaygrounds.put(key, accept(playgroundDeriver));
			set(step.current.x, step.current.y, current);
		}
		return nextPlaygrounds.get(key);
	}
	public void clear() {
		nextPlaygrounds.clear();
	}
}