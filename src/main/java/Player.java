import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Scanner;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {
	public static enum Actions {
		MOVE, BOMB;
	}
	public static enum Content {
		NOTHING,
		BOX,
		WALL,
		FIRE;

		public static Content valueOf(char c) {
			switch(c) {
			case '.':
				return NOTHING;
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
				return BOX;
			case 'X':
				return WALL;
			default:
				throw new UnsupportedOperationException(String.format("There is no content for character %s", c));
			}
		}
		@Override
		public String toString() {
			return name();
		}
	}
	public static class Playground {
		public final int farest;
		public Content[][] positions;
		private int width;
		private int height;

		public Playground(int width, int height) {
			this.width = width;
			this.height = height;
			farest = Math.max(width, height);
			positions = new Content[width][];
			for(int index=0; index<width; index++) {
				positions[index] = new Content[height];
			}
		}

		public void readRow(String string, int y) {
			char[] characters = string.toCharArray();
			for (int x = 0; x < characters.length; x++) {
				positions[x][y] = Content.valueOf(characters[x]);
			}
		}

		/** 
		 * Uses Manhattan distance to find nearest content.
		 * 
		 * TODO improve implementation !
		 * @param content
		 * @param me
		 * @return
		 */
		public Location findNearest(Content content, Entity me) {
			Collection<Location> nearest = sortByDistanceTo(content, me, 1);
			if(nearest.size()==1)
				return nearest.iterator().next();
			return null;
		}

		/**
		 * Build a collection of maxium size count sorting items by their distance to given location
		 * @param contentType
		 * @param me
		 * @param count
		 * @return
		 */
		public Collection<Location> sortByDistanceTo(Content contentType, Entity center, int count) {
			// TODO find the one centering the bomber
			int[][] MULTIPLIERS = new int[][] {
				{1, 1},
				{1, -1},
				{-1, -1},
				{-1, 1},
			};
			Collection<Location> targets = new LinkedHashSet<>();
			if(positions[center.x][center.y]==contentType) {
				targets.add(new Location(contentType, center.x, center.y));
			}
			for (int extension = 1; extension <= farest; extension++) {
				for (int orthogonal = 0; orthogonal <= extension; orthogonal++) {
					for (int direction = 0; direction < MULTIPLIERS.length; direction++) {
						int[] multipliers = MULTIPLIERS[direction];
						int p_x = center.x+multipliers[0]*(extension-orthogonal);
						int p_y = center.y+multipliers[1]*orthogonal;
						if(p_x>=0 && p_x<width) {
							if(p_y>=0 && p_y<height) {
								if(positions[p_x][p_y]==contentType) {
									targets.add(getLocation(p_x, p_y));
									if(targets.size()>=count) {
										return targets;
									}
								}
							}
						}
					}
				}
			}
			return targets;
		}

		public Location getLocation(int x, int y) {
			return new Location(positions[x][y], x, y);
		}
	}
	
	public static abstract class Entity {
		public final int x;
		public final int y;
		public Entity(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}
		public int distanceTo(int o_x, int o_y) {
			return Math.abs(x-o_x)+Math.abs(y-o_y);
		}
		public int distanceTo(Entity other) {
			return distanceTo(other.x, other.y);
		}

		public String perform(Actions action) {
			return String.format("%s %d %d", action, x, y);
		}
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
	}
	
	public static class GameEntity extends Entity {
		public final int id;
		public final int param1;
		public final int param2;
		public GameEntity(int id, int x, int y, int param1, int param2) {
			super(x, y);
			this.id = id;
			this.param1 = param1;
			this.param2 = param2;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + id;
			result = prime * result + param1;
			result = prime * result + param2;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			GameEntity other = (GameEntity) obj;
			if (id != other.id)
				return false;
			if (param1 != other.param1)
				return false;
			if (param2 != other.param2)
				return false;
			return true;
		}
	}
	public static class Location extends Entity {
		public final Content content;

		public Location(Content content, int x, int y) {
			super(x, y);
			this.content = content;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((content == null) ? 0 : content.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			Location other = (Location) obj;
			if (content != other.content)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Location [content=" + content + ", x=" + x + ", y=" + y + "]";
		}
	}
	public static class Item extends GameEntity {

		public Item(int id, int x, int y, int param1, int param2) {
			super(id, x, y, param1, param2);
		}
		
	}
	public static class Bomb extends GameEntity {

		public Bomb(int id, int x, int y, int param1, int param2) {
			super(id, x, y, param1, param2);
		}

		/**
		 * Clear playground from all elements that the bomb will destroy
		 * @param playground
		 */
		public void clearPlayground(Playground playground) {
			int xmin = Math.max(0, x-param1);
			int xmax = Math.min(playground.width-1, x+param1);
			int ymin = Math.max(0, y-param1);
			int ymax = Math.min(playground.height-1, y+param1);
			for (int x = xmin; x <= xmax; x++) {
				playground.positions[x][y] = Content.FIRE;
			}
			for (int y = ymin; y <= ymax; y++) {
				playground.positions[x][y] = Content.FIRE;
			}
		}

		@Override
		public String toString() {
			return "Bomb [id=" + id + ", param1=" + param1 + ", param2=" + param2 + ", x=" + x + ", y=" + y + "]";
		}
	}

	public static class Bomber extends GameEntity {

		public Bomber(int id, int x, int y, int param1, int param2) {
			super(id, x, y, param1, param2);
		}

		public String attackNearestBox(Playground playground, Collection<Bomb> dropped, Collection<Item> items) {
			int bombsIDropped = 0;
			// before to even search nearest bomb, remove the ones marked by bombs
			for (Bomb bomb : dropped) {
				bomb.clearPlayground(playground);
				if(bomb.id==id) {
					// This is a bomb I dropped !
					bombsIDropped++;
				}
			}
			Collection<Location> boxes = playground.sortByDistanceTo(Content.BOX, this, 2);
			if(boxes.size()>0) {
				Location target = findDestination(playground, boxes);
				// test if there is an item nearby
				int distanceToBox = target.distanceTo(this);
				if(distanceToBox<5) {
					for (Item item : items) {
						if(item.distanceTo(this)<=distanceToBox+1) {
							System.err.println(String.format("Found a nearby item at %s", item));
							return item.perform(Actions.MOVE);
						}
					}
				}
				if(distanceToBox<param2 && bombsIDropped<param1) {
					if(target.x==x || target.y==y) {
						System.err.println(String.format("box %s can be hit ! so dropping a bomb now at location %s!", target, this));
						return perform(Actions.BOMB);
					}
				}
				return target.perform(Actions.MOVE);
			} else {
				return perform(Actions.MOVE);
			}
		}

		public Location findDestination(Playground playground, Collection<Location> boxes) {
			Iterator<Location> iterator = boxes.iterator();
			Location box = iterator.next();
			Location firstTarget = box;
//			if(iterator.hasNext()) {
//				Location secondTarget = iterator.next();
//				if(box.distanceTo(secondTarget)<=param2) {
//					// Can we bomb both at once ?
//					// Compute intersection points
//					Location firstAttempt = playground.getLocation(firstTarget.x, secondTarget.y);
//					Location secondAttempt = playground.getLocation(secondTarget.x, firstTarget.y);
//					Location selected = firstAttempt.distanceTo(this)<secondAttempt.distanceTo(this) ?
//							firstAttempt : secondAttempt;
//					if(selected.content!=Content.WALL) {
//						return selected;
//					}
//				}
//			}
			return firstTarget;
		}

		@Override
		public String toString() {
			return "Bomber [id=" + id + ", param1=" + param1 + ", param2=" + param2 + ", x=" + x + ", y=" + y + "]";
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
            Bomber me = null;
            Collection<Bomb> droppedBombs = new ArrayList<>();
            Collection<Item> items= new ArrayList<>();

            for (int rowIndex = 0; rowIndex < height; rowIndex++) {
                String row = in.nextLine();
                playground.readRow(row, rowIndex);
            }
            int entities = in.nextInt();
            for (int i = 0; i < entities; i++) {
                int entityType = in.nextInt();
                int owner = in.nextInt();
                int x = in.nextInt();
                int y = in.nextInt();
                int param1 = in.nextInt();
                int param2 = in.nextInt();
                switch(entityType) {
                case 0:
                	if(owner==myId) {
                		System.err.println(String.format("Found my id %d", myId));
                		me = new Bomber(owner, x, y, param1, param2);
                	}
                	break;
                case 1:
                	droppedBombs.add(new Bomb(owner, x, y, param1, param2));
                	break;
                case 2:
                	items.add(new Item(owner, x, y, param1, param2));
                	break;
                }
            }
            in.nextLine();

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            System.out.println(me.attackNearestBox(playground, droppedBombs, items));
        }
    }
}