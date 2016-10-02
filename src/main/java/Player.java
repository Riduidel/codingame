import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Auto-generated code below aims at helping you parse the standard input
 * according to the problem statement.
 **/
class Player {
	private static final int[] HOLD = {0, 0}; 
	private static final int[] LEFT = {-1, 0}; 
	private static final int[] RIGHT = {1, 0}; 
	private static final int[] TOP = {0, -1}; 
	private static final int[] DOWN = {0, 1}; 
	public static final int[][] DIRECTIONS = new int[][] {
		LEFT,
		RIGHT,
		TOP,
		DOWN
	};
	public static final int[][] POSSIBLE_DIRECTIONS = new int[][] {
//		HOLD,
		LEFT,
		RIGHT,
		TOP,
		DOWN
	};
	public static class EvolvableConstants {

		public static final int HORIZON = 16;
		public static final int BOMB_DELAY = 8;
		public static final int COUNT_ENOUGH_TRAJECTORIES = 2000;
		public static final int ITERATIVE_TIME_LIMIT = 3;
		public Integer SCORE_NO_EVASION_MALUS = -1;
		public Integer SCORE_ON_THE_ROAD = 4;
		public Integer SCORE_ENDANGERED = -100;
		public Integer SCORE_BAD_MOVE = -200;
		public Integer SCORE_CATCHED_ITEM = 20;
		public Integer SCORE_START_BOMB_CHAIN = 5;
		public Integer SCORE_EXPLODE_BOX = 50;
		public Integer SCORE_SUICIDE = -10000;
		public Integer SCORE_KILL_ENEMY = 10;
		public Integer SCORE_BURNT_ITEM = -5;
		public int SCORE_DROP_BOMB_TAX = -10;
		public final long DELAY_CREATE_TRAJECTORIES = 80;
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (DELAY_CREATE_TRAJECTORIES ^ (DELAY_CREATE_TRAJECTORIES >>> 32));
			result = prime * result + ((SCORE_BAD_MOVE == null) ? 0 : SCORE_BAD_MOVE.hashCode());
			result = prime * result + ((SCORE_BURNT_ITEM == null) ? 0 : SCORE_BURNT_ITEM.hashCode());
			result = prime * result + ((SCORE_CATCHED_ITEM == null) ? 0 : SCORE_CATCHED_ITEM.hashCode());
			result = prime * result + SCORE_DROP_BOMB_TAX;
			result = prime * result + ((SCORE_ENDANGERED == null) ? 0 : SCORE_ENDANGERED.hashCode());
			result = prime * result + ((SCORE_EXPLODE_BOX == null) ? 0 : SCORE_EXPLODE_BOX.hashCode());
			result = prime * result + ((SCORE_KILL_ENEMY == null) ? 0 : SCORE_KILL_ENEMY.hashCode());
			result = prime * result + ((SCORE_NO_EVASION_MALUS == null) ? 0 : SCORE_NO_EVASION_MALUS.hashCode());
			result = prime * result + ((SCORE_ON_THE_ROAD == null) ? 0 : SCORE_ON_THE_ROAD.hashCode());
			result = prime * result + ((SCORE_START_BOMB_CHAIN == null) ? 0 : SCORE_START_BOMB_CHAIN.hashCode());
			result = prime * result + ((SCORE_SUICIDE == null) ? 0 : SCORE_SUICIDE.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			EvolvableConstants other = (EvolvableConstants) obj;
			if (DELAY_CREATE_TRAJECTORIES != other.DELAY_CREATE_TRAJECTORIES)
				return false;
			if (SCORE_BAD_MOVE == null) {
				if (other.SCORE_BAD_MOVE != null)
					return false;
			} else if (!SCORE_BAD_MOVE.equals(other.SCORE_BAD_MOVE))
				return false;
			if (SCORE_BURNT_ITEM == null) {
				if (other.SCORE_BURNT_ITEM != null)
					return false;
			} else if (!SCORE_BURNT_ITEM.equals(other.SCORE_BURNT_ITEM))
				return false;
			if (SCORE_CATCHED_ITEM == null) {
				if (other.SCORE_CATCHED_ITEM != null)
					return false;
			} else if (!SCORE_CATCHED_ITEM.equals(other.SCORE_CATCHED_ITEM))
				return false;
			if (SCORE_DROP_BOMB_TAX != other.SCORE_DROP_BOMB_TAX)
				return false;
			if (SCORE_ENDANGERED == null) {
				if (other.SCORE_ENDANGERED != null)
					return false;
			} else if (!SCORE_ENDANGERED.equals(other.SCORE_ENDANGERED))
				return false;
			if (SCORE_EXPLODE_BOX == null) {
				if (other.SCORE_EXPLODE_BOX != null)
					return false;
			} else if (!SCORE_EXPLODE_BOX.equals(other.SCORE_EXPLODE_BOX))
				return false;
			if (SCORE_KILL_ENEMY == null) {
				if (other.SCORE_KILL_ENEMY != null)
					return false;
			} else if (!SCORE_KILL_ENEMY.equals(other.SCORE_KILL_ENEMY))
				return false;
			if (SCORE_NO_EVASION_MALUS == null) {
				if (other.SCORE_NO_EVASION_MALUS != null)
					return false;
			} else if (!SCORE_NO_EVASION_MALUS.equals(other.SCORE_NO_EVASION_MALUS))
				return false;
			if (SCORE_ON_THE_ROAD == null) {
				if (other.SCORE_ON_THE_ROAD != null)
					return false;
			} else if (!SCORE_ON_THE_ROAD.equals(other.SCORE_ON_THE_ROAD))
				return false;
			if (SCORE_START_BOMB_CHAIN == null) {
				if (other.SCORE_START_BOMB_CHAIN != null)
					return false;
			} else if (!SCORE_START_BOMB_CHAIN.equals(other.SCORE_START_BOMB_CHAIN))
				return false;
			if (SCORE_SUICIDE == null) {
				if (other.SCORE_SUICIDE != null)
					return false;
			} else if (!SCORE_SUICIDE.equals(other.SCORE_SUICIDE))
				return false;
			return true;
		}
		
	}
	public static class Delay {
		public final long start = System.currentTimeMillis();
		public long howLong() {
			return System.currentTimeMillis()-start;
		}
		public boolean isElapsed(long delay) {
			return howLong()>delay;
		}
	}
	public static interface ContentVisitor<Type> {

		Type visitNothing(Nothing nothing);

		Type visitBox(Box box);

		Type visitWall(Wall wall);

		Type visitGamer(Gamer gamer);

		Type visitBomb(Bomb bomb);

		Type visitItem(Item item);

		Type visitFire(Fire fire);

		Type visitFireThenItem(FireThenItem fireThenItem);

		Type visitBombDanger(BombDanger bombDanger);
	}
	public static class ContentAdapter<Type> implements ContentVisitor<Type> {
		private Type returned = null;

		public ContentAdapter() { }
		public ContentAdapter(Type returned) { this.returned = returned; }
		@Override public Type visitNothing(Nothing nothing) { return returned; }
		@Override public Type visitBombDanger(BombDanger nothing) { return returned; }
		@Override public Type visitBox(Box box) { return returned; }
		@Override public Type visitWall(Wall wall) { return returned; }
		@Override public Type visitGamer(Gamer bomber) { return returned; }
		@Override public Type visitBomb(Bomb bomb) { return returned; }
		@Override public Type visitItem(Item item) { return returned; }
		@Override public Type visitFire(Fire fire) { return returned; }
		@Override public Type visitFireThenItem(FireThenItem fire) { return returned; }
	}
	public static enum CanFire {
		NOT,
		END_PROPAGATION,
		YES
	}
	public static interface Content {
		public <Type> Type accept(ContentVisitor<Type> visitor);

		public CanFire canFire();

		public boolean canBeWalkedOn();
	}
	public static class Nothing implements Content {
		static Nothing instance = new Nothing();

		@Override
		public <Type> Type accept(ContentVisitor<Type> visitor) {
			return visitor.visitNothing(this);
		}

		@Override public CanFire canFire() { return CanFire.YES; }

		@Override public boolean canBeWalkedOn() { return true; }
	}
	public static class BombDanger implements Content {
		static BombDanger instance = new BombDanger();

		@Override
		public <Type> Type accept(ContentVisitor<Type> visitor) {
			return visitor.visitBombDanger(this);
		}

		@Override public CanFire canFire() { return CanFire.YES; }

		@Override public boolean canBeWalkedOn() { return true; }
	}
	public static class Box implements Content {
		static Box instance = new Box();

		@Override
		public <Type> Type accept(ContentVisitor<Type> visitor) {
			return visitor.visitBox(this);
		}

		@Override public CanFire canFire() { return CanFire.END_PROPAGATION; }

		@Override public boolean canBeWalkedOn() { return false; }
	}
	public static class Wall implements Content {
		static Wall instance = new Wall();

		@Override
		public <Type> Type accept(ContentVisitor<Type> visitor) {
			return visitor.visitWall(this);
		}

		@Override public CanFire canFire() { return CanFire.NOT; }

		@Override public boolean canBeWalkedOn() { return false; }
	}
	public static class Fire implements Content {
		static Fire instance = new Fire();

		@Override
		public <Type> Type accept(ContentVisitor<Type> visitor) {
			return visitor.visitFire(this);
		}

		@Override public CanFire canFire() { return CanFire.YES; }

		@Override public boolean canBeWalkedOn() { return true; }
	}
	public static class FireThenItem extends Fire {
		static FireThenItem instance = new FireThenItem();

		@Override
		public <Type> Type accept(ContentVisitor<Type> visitor) {
			return visitor.visitFireThenItem(this);
		}

		@Override public CanFire canFire() { return CanFire.YES; }

		@Override public boolean canBeWalkedOn() { return false; }
	}
	public static abstract class Entity implements Content {
		public final int x;
		public final int y;
		public Entity(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

		@Override public CanFire canFire() { return CanFire.YES; }

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Entity other = (Entity) obj;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Entity [x=" + x + ", y=" + y + "]";
		}
	}
	public static enum Action {
		MOVE,
		BOMB;
	}
	public static class TrajectoryBuilder {
		public static final Random random = new Random();
		public final Playground source;
		public final Delay delay;
		public final EvolvableConstants constants;
		public TrajectoryBuilder(Playground source, Delay delay, EvolvableConstants constants) {
			super();
			this.source = source;
			this.delay = delay;
			this.constants = constants;
		}
		public Trajectory findBest(Gamer me) {
			int count = 0;
			Trajectory returned = null, tested;
			while(!delay.isElapsed(constants.DELAY_CREATE_TRAJECTORIES) 
					&& count<=constants.COUNT_ENOUGH_TRAJECTORIES) {
				tested = build(me, count);
				if(returned==null) {
					returned = tested;
				} else {
					if(returned.score<tested.score)
						returned = tested;
				}
				count++;
			}
			System.err.println(String.format("Tested %d trajectories", count));
			return returned;
		}
		public Trajectory build(Gamer me, int count) {
			Trajectory returned = new Trajectory();
			Entity current = me;
			Playground playground = source;
			int length = 0;
			int speedBonus = constants.HORIZON;
			while(!delay.isElapsed(constants.DELAY_CREATE_TRAJECTORIES)
					&& length<EvolvableConstants.HORIZON) {
				Step next = createStep(current, me, playground, returned.steps.size(), count);
				if(next==null) {
					break;
				}
				returned.add(next, speedBonus);
				current = next;
				playground = next.playground;
				length++;
				speedBonus--;
			}
			return returned;
		}
		public Step createStep(Entity current, Gamer me, Playground playground, int time, int count) {
			// We test positions on default next playground, whcih gives a good idea of what will happen
			Playground test = playground.next();
			int[][] availableDirections = test.getAvailableDirectionsAt(current);
			int[] directions = null;
			if(availableDirections.length==0) {
				// Too bad, I'm dead !
				directions = new int[] {current.x, current.y};
			} else {
//				if(time<constants.ITERATIVE_TIME_LIMIT) {
//					directions = availableDirections[((count+1)*((time+1)*availableDirections.length))%availableDirections.length];
//				} else {
					int randomDirection = random.nextInt(availableDirections.length);
					directions = availableDirections[randomDirection]; 
//				}
			}
			List<Action> availableActions = new ArrayList<>(2);
			availableActions.add(Action.MOVE);
			// Now we have our directions, check what actions are available
			if(me.bombs>0) {
				// OK, gamer can bomb. But should he bomb ?
				// We assume yes
				availableActions.add(Action.BOMB);
			}
			int randomAction = random.nextInt(availableActions.size());
			Action nextAction = availableActions.get(randomAction);
			return new Step(nextAction, directions[0], directions[1], me, current)
						.computeScore(playground, constants);
		}
	}
	public static class Trajectory {
		public int score;
		public final List<Step> steps = new ArrayList<>(EvolvableConstants.HORIZON);
		public void add(Step next, int speedBonus) {
			steps.add(next);
			score += next.score*speedBonus;
		}
		public String toCommandString() {
			return steps.get(0).toCommandString();
		}
	}
	public static class Step extends Entity {
		private final class BombScoreComputer extends ContentAdapter<Integer> {
			private EvolvableConstants constants;

			private BombScoreComputer(EvolvableConstants contants) {
				super(0);
				this.constants = contants;
			}

			@Override public Integer visitBomb(Bomb bomb) { return constants.SCORE_START_BOMB_CHAIN; }

			@Override public Integer visitBox(Box box) { return constants.SCORE_EXPLODE_BOX; }

			@Override public Integer visitGamer(Gamer bomber) { 
				if(bomber==Step.this.gamer) {
					return constants.SCORE_SUICIDE;
				} else {
					return constants.SCORE_KILL_ENEMY;
				}
			}

			@Override public Integer visitItem(Item item) { return constants.SCORE_BURNT_ITEM; }
			
			@Override public Integer visitFire(Fire fire) {return constants.SCORE_SUICIDE; }
			
			@Override public Integer visitFireThenItem(FireThenItem fire) { return constants.SCORE_SUICIDE; }
		}

		private final class MoveScoreComputer extends ContentAdapter<Integer> {
			private EvolvableConstants constants;

			private MoveScoreComputer(EvolvableConstants constants) {
				super(constants.SCORE_BAD_MOVE);
				this.constants = constants;
			}
			
			@Override public Integer visitNothing(Nothing nothing) {
				// Lower that count by the number of evasive actions
				int noExit = 4;
				for (int directionIndex = 0; directionIndex < DIRECTIONS.length; directionIndex++) {
					int[] direction = DIRECTIONS[directionIndex];
					int p_x = current.x+direction[0];
					int p_y = current.x+direction[1];
					if(playground.contains(p_x, p_y)) {
						if(playground.get(p_x,p_y).canBeWalkedOn()) {
							noExit--;
						}
					}
					
				}
				return constants.SCORE_ON_THE_ROAD+noExit*constants.SCORE_NO_EVASION_MALUS;
			}
			@Override public Integer visitBombDanger(BombDanger nothing) { return constants.SCORE_ENDANGERED; }

			@Override public Integer visitGamer(Gamer bomber) { return 0; }

			@Override public Integer visitItem(Item item) { 
				return constants.SCORE_CATCHED_ITEM;
			}
			
			@Override public Integer visitFire(Fire fire) {return constants.SCORE_SUICIDE; }
			
			@Override public Integer visitFireThenItem(FireThenItem fire) { return constants.SCORE_SUICIDE; }
		}

		public int score = 0;
		public final Entity current;
		public Playground playground;
		public final Action action;
		public final Gamer gamer;

		public Step(Action action, int x, int y, Gamer gamer, Entity current) {
			super(x, y);
			this.action = action;
			this.gamer = gamer;
			this.current = current;
		}

		public String toCommandString() {
			return String.format("%s %d %d", action, x, y);
		}

		public Step computeScore(Playground source, EvolvableConstants constants) {
			int evaluatedScore = 0;
			// now we have a playground, compute score according to action
			switch(action) {
			case BOMB: 
				playground = source.next(this);
				evaluatedScore += computeScoreForBombAction(constants);
				break;
			case MOVE:
				playground = source.next();
				evaluatedScore += computeScoreForMoveAction(constants);
				break;
			}
			score = evaluatedScore;
			return this;
		}

		private Integer computeScoreForMoveAction(EvolvableConstants constants) {
			return playground.get(x, y).accept(new MoveScoreComputer(constants));
		}

		private int computeScoreForBombAction(EvolvableConstants constants) {
			int evaluatedScore = constants.SCORE_DROP_BOMB_TAX;
			Playground iterative, 
				next;
			iterative = next = playground;
			int time = 0;
			while(!Fire.instance.equals(next.get(current.x, current.y)) && time<10) {
				iterative = next;
				next = iterative.next();
				time++;
			}
			// Grab bomb range from playground at t-1
			Content currentContent = iterative.get(current.x, current.y);
			if(Fire.instance.equals(currentContent))
				return constants.SCORE_SUICIDE;
			Bomb toExplode = (Bomb) currentContent;
			// Now count what that bomb could have detonated
			for (int directionIndex = 0; directionIndex < DIRECTIONS.length; directionIndex++) {
				int[] direction = DIRECTIONS[directionIndex];
				for (int i = 0; i < toExplode.range; i++) {
					int p_x = current.x+direction[0]*i;
					int p_y = current.x+direction[1]*i;
					if(iterative.contains(p_x, p_y)) {
						Content content = iterative.get(p_x, p_y);
						switch(content.canFire()) {
						case NOT:
							break;
						case END_PROPAGATION:
							evaluatedScore+=content.accept(new BombScoreComputer(constants));
						case YES:
						}
					} else {
						break;
					}
				}
			}
			return evaluatedScore;
		}

		@Override public boolean canBeWalkedOn() { return true; }

		public boolean moves() {
			return !(x==current.x && y==current.y);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((action == null) ? 0 : action.hashCode());
			result = prime * result + ((current == null) ? 0 : current.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			Step other = (Step) obj;
			if (action != other.action)
				return false;
			if (current == null) {
				if (other.current != null)
					return false;
			} else if (!current.equals(other.current))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Step [score=" + score + ", current=" + current + ", action=" + action + ", x=" + x + ", y=" + y
					+ "]";
		}

		/**
		 * Invoked when generating a playground from a step
		 * @param playground 
		 * @param content
		 */
		public void mutateContent(final Playground real, Content content) {
			switch(action) {
			case BOMB:
				if(content.canBeWalkedOn()) {
					real.set(current.x, current.y, new Bomb(gamer.id, current.x, current.y, EvolvableConstants.BOMB_DELAY, gamer.range));
				}
				break;
			case MOVE:
				break;
			}
		}

		@Override
		public <Type> Type accept(ContentVisitor<Type> visitor) {
			throw new UnsupportedOperationException("A Step should never be visited");
		}
	}
	public static class Gamer extends Entity {
		public final int id;
		public final int bombs;
		public final int range;
		public Gamer(int id, int x, int y, int bombs, int range) {
			super(x, y);
			this.id = id;
			this.bombs = bombs;
			this.range = range;
		}
		@Override
		public <Type> Type accept(ContentVisitor<Type> visitor) {
			return visitor.visitGamer(this);
		}

		@Override public boolean canBeWalkedOn() { return true; }
		@Override
		public String toString() {
			return "Gamer [id=" + id + ", bombs=" + bombs + ", range=" + range + ", x=" + x + ", y=" + y + "]";
		}
	}
	public static class Bomb extends Entity {
		public final int owner;
		public final int delay;
		public final int range;
		public Bomb(int owner, int x, int y, int delay, int range) {
			super(x, y);
			this.owner = owner;
			this.delay = delay;
			this.range = range;
		}
		@Override
		public <Type> Type accept(ContentVisitor<Type> visitor) {
			return visitor.visitBomb(this);
		}

		@Override public boolean canBeWalkedOn() { return false; }
		@Override
		public String toString() {
			return "Bomb [owner=" + owner + ", delay=" + delay + ", range=" + range + ", x=" + x + ", y=" + y + "]";
		}
	}
	public static class Item extends Entity {
		public final int type;

		public Item(int ignored0, int x, int y, int type, int ignored2) {
			super(x, y);
			this.type = type;
		}

		@Override
		public <Type> Type accept(ContentVisitor<Type> visitor) {
			return visitor.visitItem(this);
		}
		@Override public CanFire canFire() { return CanFire.END_PROPAGATION; }

		@Override public boolean canBeWalkedOn() { return true; }
	}
	public static class PlaygroundAdapter<Type> implements PlaygroundVisitor<Type>{
		public Type returned;
		public PlaygroundAdapter() { }
		public PlaygroundAdapter(Type defaultvalue) { returned = defaultvalue; }
		@Override public void startVisit(Playground playground) { }
		@Override public void startVisitRow(int y) { }
		@Override public void endVisitRow(int y) { }
		@Override public Type endVisit(Playground playground) { return returned; }
		@Override public void visit(int x, int y, Content content) {}
	}
	public static interface PlaygroundVisitor<Type> {

		void startVisit(Playground playground);

		void startVisitRow(int y);

		void endVisitRow(int y);

		Type endVisit(Playground playground);

		void visit(int x, int y, Content content);
		
	}
	public static class Playground {
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
		public final int width;
		public final int height;
		/** Do not forget to exchange x and y to access that array */
		private Content[][] positions;
		private Map<Object, Playground> nextPlaygrounds = new TreeMap<>();
		private Map<String, int[][]> availableDirections = new TreeMap<>();
		private static PlaygroundDeriver playgroundDeriver = new PlaygroundDeriver();
		public Playground(int width, int height) {
			super();
			this.width = width;
			this.height = height;
			this.positions = new Content[height][];
			for (int y = 0; y < positions.length; y++) {
				positions[y] = new Content[width];
			}
		}
		public int[][] getAvailableDirectionsAt(Entity current) {
			String key = current.x+";"+current.y;
			if(availableDirections.get(key)==null) {
				// This array directly contains new positions, and not updates we should recompute next
				List<int[]> availableDirectionsFor = new ArrayList<>(5);
				for (int direction = 0; direction < POSSIBLE_DIRECTIONS.length; direction++) {
					int[] currentDir = POSSIBLE_DIRECTIONS[direction];
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
	public static class PlaygroundDeriver extends PlaygroundAdapter<Playground> {
		public class PlaygroundCellDeriver implements ContentVisitor<Void> {
			public int x;
			public int y;
			@Override public Void visitNothing(Nothing nothing) {
				Content alreadyWritten = derived.get(x, y);
				if(!Fire.instance.equals(alreadyWritten) && !BombDanger.instance.equals(alreadyWritten))
					derived.set(x, y, nothing);
				return null;
			}
			@Override public Void visitBombDanger(BombDanger danger) {
				if(!Fire.instance.equals(derived.get(x, y)))
					derived.set(x, y, danger);
				return null;
			}
			@Override
			public Void visitBox(Box box) {
				Content derivedContent = derived.get(x, y);
				if(Fire.instance.equals(derivedContent) || FireThenItem.instance.equals(derivedContent))
					derived.set(x, y, FireThenItem.instance);
				else
					derived.set(x, y, box);
				return null;
			}
			@Override
			public Void visitWall(Wall wall) {
				derived.set(x, y, wall);
				return null;
			}
			@Override
			public Void visitGamer(Gamer gamer) {
				derived.set(x, y, gamer);
				return null;
			}
			@Override
			public Void visitItem(Item item) {
				if(!Fire.instance.equals(derived.get(x, y)))
				derived.set(x, y, item);
				return null;
			}
			@Override
			public Void visitFire(Fire fire) {
				derived.set(x, y, Nothing.instance);
				return null;
			}
			@Override
			public Void visitFireThenItem(FireThenItem fire) {
				derived.set(x, y, new Item(0, x, y, 0, 0));
				return null;
			}
			@Override
			public Void visitBomb(Bomb bomb) {
				if(bomb.delay>1) {
					if(Fire.instance.equals(derived.get(x, y))) {
						fireBomb(bomb);
					} else {
						derived.set(x, y, new Bomb(bomb.owner, bomb.x, bomb.y, bomb.delay-1, bomb.range));
						// Do not forget to extend bomb with danger
						int dangerZone = bomb.range;
//						int dangerZone = Math.min(bomb.delay, bomb.range);
						for (int directionIndex = 0; directionIndex < DIRECTIONS.length; directionIndex++) {
							int[] direction = DIRECTIONS[directionIndex];
							for (int extension = 1; extension <= dangerZone; extension++) {
								int p_x = bomb.x+direction[0]*extension;
								int p_y = bomb.y+direction[1]*extension;
								if(source.contains(p_x, p_y)) {
									if(Nothing.instance.equals(source.get(p_x, p_y))) {
										derived.set(p_x, p_y, BombDanger.instance);
									} else {
										break;
									}
								} else {
									break;
								}
							}
						}
					}
				} else {
					// Fire that bomb !
					fireBomb(bomb);
				}
				// We may change coordinates to follow explosion, so reset them here
				this.x = bomb.x;
				this.y = bomb.y;
				return null;
			}
			private void fireBomb(Bomb bomb) {
				derived.set(bomb.x, bomb.y, Fire.instance);
				for (int i = 0; i < DIRECTIONS.length; i++) {
					int[] direction = DIRECTIONS[i];
					for (int extension = 1; extension <= bomb.range; extension++) {
						int p_x = bomb.x+direction[0]*extension;
						int p_y = bomb.y+direction[1]*extension;
						if(source.contains(p_x, p_y)) {
							CanFire canFire = source.get(p_x, p_y).canFire();
							if(CanFire.YES.equals(canFire)||CanFire.END_PROPAGATION.equals(canFire)) {
								this.x = p_x;
								this.y = p_y;
								if(!Fire.instance.equals(derived.get(p_x, p_y))) {
									source.get(p_x, p_y).accept(this);
									derived.set(p_x, p_y, Fire.instance);
								}
							}
							if(CanFire.NOT.equals(canFire)||CanFire.END_PROPAGATION.equals(canFire)) {
								break;
							}
						} else {
							break;
						}
					}
				}
			}
		}
		private PlaygroundCellDeriver cellDeriver = new PlaygroundCellDeriver();
		public Playground source;
		public Playground derived;
		@Override
		public void startVisit(Playground playground) {
			source = playground;
			returned = derived = new Playground(playground.width, playground.height);
		}
		@Override
		public void visit(int x, int y, Content content) {
			cellDeriver.x = x;
			cellDeriver.y = y;
			content.accept(cellDeriver);
		}
		@Override
		public Playground endVisit(Playground playground) {
			return super.endVisit(playground);
		}
	}
	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		int width = in.nextInt();
		int height = in.nextInt();
		Playground playground = new Playground(width, height);
		int myId = in.nextInt();
		in.nextLine();
		// game loop
		while (true) {
			playground.clear();
			Delay delay = new Delay();
			for (int rowIndex = 0; rowIndex < height; rowIndex++) {
				String row = in.nextLine();
				playground.readRow(row, rowIndex);
			}
			int entities = in.nextInt();
			Gamer me = null;
			for (int i = 0; i < entities; i++) {
				int entityType = in.nextInt();
				int owner = in.nextInt();
				int x = in.nextInt();
				int y = in.nextInt();
				int param1 = in.nextInt();
				int param2 = in.nextInt();
				switch (entityType) {
				case 0:
					// bomber
					if(owner==0)
						me = new Gamer(owner, x, y, param1, param2);
					else
						playground.readGameEntities(new Gamer(owner, x, y, param1, param2));
					break;
				case 1:
					// bomb
					playground.readGameEntities(new Bomb(owner, x, y, param1, param2));
					break;
				case 2:
					// item
					playground.readGameEntities(new Item(owner, x, y, param1, param2));
					break;
				}
			}
			in.nextLine();
			Player.Trajectory best = new Player.TrajectoryBuilder(playground, delay, new Player.EvolvableConstants())
					.findBest(me);
			
			System.err.println("It took "+delay.howLong());
//			System.err.println(playground.toString());

			System.out.println(best.toCommandString());
		}
	}
}