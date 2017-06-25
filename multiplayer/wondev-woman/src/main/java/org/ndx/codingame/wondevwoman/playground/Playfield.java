package org.ndx.codingame.wondevwoman.playground;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.ndx.codingame.gaming.tounittest.ToUnitTestFiller;
import org.ndx.codingame.gaming.tounittest.ToUnitTestHelpers;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.lib2d.discrete.PlaygroundAdapter;
import org.ndx.codingame.wondevwoman.Constants;
import org.ndx.codingame.wondevwoman.actions.Dual;
import org.ndx.codingame.wondevwoman.entities.Content;
import org.ndx.codingame.wondevwoman.entities.ContentAdapter;
import org.ndx.codingame.wondevwoman.entities.ContentVisitor;
import org.ndx.codingame.wondevwoman.entities.Floor;
import org.ndx.codingame.wondevwoman.entities.Gamer;
import org.ndx.codingame.wondevwoman.entities.Hole;

public class Playfield extends Playground<Content> implements ToUnitTestFiller {
	private final class ToStringVisitor extends PlaygroundAdapter<StringBuilder, Content> implements ContentVisitor<Character> {
		private ToStringVisitor(final StringBuilder defaultvalue) {
			super(defaultvalue);
		}

		@Override
		public void endVisitRow(final int y) {
			returned.append("\"");
			if(y<height-1) {
				returned.append(",");
			}
			returned.append("\n");
		}

		@Override
		public void startVisitRow(final int y) {
			returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("\"");
		}

		@Override
		public void visit(final int x, final int y, final Content content) {
			returned.append(content.accept(this));
		}

		@Override
		public Character visitFloor(final Floor floor) {
			return floor.heightToChar();
		}

		@Override
		public Character visitHole(final Hole hole) {
			return '.';
		}
	}

	public static Playfield from(final List<String> rows) {
		Playfield playfield = null;
		for (int i = 0; i < rows.size(); i++) {
			final String row = rows.get(i);
			if (i == 0) {
				playfield = new Playfield(row.length(), rows.size());
			}
			final byte[] bytes = row.getBytes();
			for (int j = 0; j < bytes.length; j++) {
				final byte heightByte = bytes[j];
				if (heightByte == '.') {
					playfield.set(j, i, Hole.instance);
				} else {
					playfield.set(j, i, Floor.heightToFloor(heightByte));
				}
			}
		}
		return playfield;
	}

	public static Playfield from(final String...rows) {
		return from(Arrays.asList(rows));
	}
	private Collection<Dual> actions;
	private List<Gamer> enemy;

	private List<Gamer> my;

	public Playfield(final int width, final int height) {
		super(width, height);
	}

	public Playfield(final int width, final int height, final Content defaultValue) {
		super(width, height, defaultValue);
	}

	public Playfield(final Playground<Content> playground) {
		super(playground);
	}

	@Override
	public StringBuilder build(final String effectiveCommand) {
		final StringBuilder returned = new StringBuilder();
		returned.append(ToUnitTestHelpers.declaredFilledContainer(ToUnitTestHelpers.CONTENT_PREFIX, my, List.class,
				Gamer.class, "my"));
		returned.append(ToUnitTestHelpers.declaredFilledContainer(ToUnitTestHelpers.CONTENT_PREFIX, enemy, List.class,
				Gamer.class, "enemy"));
		returned.append(ToUnitTestHelpers.declaredFilledContainer(ToUnitTestHelpers.CONTENT_PREFIX, actions, List.class,
				Dual.class, "actions"));
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("Playfield p = Playfield.from(\n");
		returned.append(playfieldToString());
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append(");\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("p.withMy(my)\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("\t.withEnemy(enemy)\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("\t.withActions(actions);\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX)
		.append("assertThat(p.computeMoves()).isNotEqualTo(\"")
		.append(effectiveCommand.replace(
				"\n",
				"\\n\"+\n"+ToUnitTestHelpers.CONTENT_PREFIX+"\""))
		.append("\");\n");
		return returned;
	}

	/**
	 * Compute move of given gamer. The list of previous ones is given to prevent collision
	 * @param gamer
	 * @return
	 */
	public List<Dual> computeMoveOf(final Gamer gamer) {
		final Set<DiscretePoint> allPositions = new HashSet<>();
		final List<DiscretePoint> myPositions = my.stream()
				.map((g) -> g.position)
				.collect(Collectors.toList());
		allPositions.addAll(myPositions);
		final List<DiscretePoint> enemyPositions = my.subList(my.indexOf(gamer)+1, my.size())
				.stream()
				.map((g) -> g.position)
				.collect(Collectors.toList());
		allPositions.addAll(enemyPositions);
		final List<Dual> returned = actions.stream()
				.filter((a) -> a.playerIndex==gamer.index)
				.filter((a) -> a.isPush() ? !myPositions.contains(computePositionAfterMove(gamer, a)): !allPositions.contains(computePositionAfterMove(gamer, a)))
				.filter((a) -> !allPositions.contains(computePositionAfterBuild(gamer, a)))
				.sorted((first, second) -> (int) Math.signum(scoreDual(gamer, second) - scoreDual(gamer, first)))
				.collect(Collectors.toList());
		return returned;
	}

	public String computeMoves() {
		final Map<Gamer, Dual> moves = computesMovesMap();
		return moves.entrySet().stream()
				.sorted((first, second) -> (int) Math.signum(scoreDual(second.getKey(), second.getValue()) - scoreDual(first.getKey(), first.getValue())))
				.map((entry) -> entry.getValue())
				.findFirst()
				.map((entry) -> entry.toCommandString())
				.get()
				;
	}

	Map<Gamer, Dual> computesMovesMap() {
		// Select gamer to be moved. First attempt : only move the first gamer
		final Map<Gamer, Dual> moves  = new HashMap<>();
		for(final Gamer g : my) {
			final List<Dual> move = computeMoveOf(g);
			if(!move.isEmpty()) {
				moves.put(g, move.get(0));
			}
		}
		return moves;
	}

	private DiscretePoint computePositionAfterBuild(final Gamer gamer, final Dual a) {
		final DiscretePoint positionAfterMove = computePositionAfterMove(gamer, a);
		return positionAfterMove.moveOf(a.build(), positionAfterMove);
	}

	private DiscretePoint computePositionAfterMove(final Gamer gamer, final Dual dual) {
		return gamer.position.moveOf(dual.move(), gamer.position);
	}

	/**
	 * Just count number of free position
	 * @param position
	 * @return
	 */
	private int countAvailablePositions(final DiscretePoint position) {
		final int currentHeight = get(position).accept(new ContentAdapter<Integer>(Integer.MIN_VALUE) {
			@Override
			public Integer visitFloor(final Floor floor) {
				return floor.height;
			}
		});
		return (int) Dual.DIRECTIONS.values().stream()
				.map((p) -> p.move(position))
				.filter((p) -> contains(p))
				.map((p) -> get(p))
				.filter((p) -> p instanceof Floor)
				.map((p) -> (Floor) p)
				.filter((p) -> p.height-1<=Constants.MAX_FLOOR)
				.filter((p) -> p.height-1<=currentHeight)
				.count();
	}

	@Override
	public boolean contains(final DiscretePoint point) {
		if(super.contains(point)) {
			return get(point).accept(new ContentVisitor<Boolean>() {

				@Override
				public Boolean visitFloor(final Floor floor) {
					return true;
				}

				@Override
				public Boolean visitHole(final Hole hole) {
					return true;
				}

			});
		} else {
			return false;
		}
	}

	private String playfieldToString() {
		return accept(new ToStringVisitor(new StringBuilder()))
				.toString();
	}

	public double scoreDual(final Gamer gamer, final Dual dual) {
		// Compute position of move
		final DiscretePoint positionAfterMove = computePositionAfterMove(gamer, dual);
		final double positionScore = countAvailablePositions(positionAfterMove)/8.0;
		final double heightPosition = get(positionAfterMove).accept(new ContentAdapter<Integer>(Integer.MIN_VALUE) {

			@Override
			public Integer visitFloor(final Floor floor) {
				return floor.height>Constants.MAX_FLOOR ? Integer.MIN_VALUE : ((Floor) get(positionAfterMove)).height;
			}
		})/3.0;

		final DiscretePoint positionAfterBuild = computePositionAfterBuild(gamer, dual);
		final double buildingReachabilityScore = get(positionAfterBuild).accept(new ContentAdapter<Integer>(Integer.MIN_VALUE) {
			@Override
			public Integer visitFloor(final Floor floor) {
				final long numberOfNeighbors = Dual.DIRECTIONS.values().stream()
						.map((p) -> p.move(positionAfterBuild))
						.filter((p) -> contains(p))
						.map((p) -> get(p))
						.filter((c) -> c instanceof Floor)
						.map((c) -> (Floor) c)
						.filter((f) -> floor.height<=f.height)
						.count();
				return numberOfNeighbors>0 ? (int) numberOfNeighbors : Integer.MIN_VALUE;
			}
		})/8.0;
		final double buildingHeightScore = get(positionAfterBuild).accept(new ContentAdapter<Integer>(Integer.MIN_VALUE) {
			@Override
			public Integer visitFloor(final Floor floor) {
				return floor.height<Constants.MAX_FLOOR ? floor.height : Integer.MIN_VALUE;
			}
		})/3.0;
		final double pushBonus = dual.isPush() ? 1 : 0;
		return positionScore +
				heightPosition*2 +
				buildingReachabilityScore * 2 +
				buildingHeightScore * 2 +
				pushBonus * 2
				;
	}

	public Playfield withActions(final Collection<Dual> actions) {
		this.actions = actions;
		return this;
	}

	public Playfield withEnemy(final List<Gamer> enemy) {
		this.enemy = enemy;
		return this;
	}

	public Playfield withMy(final List<Gamer> my) {
		this.my = my;
		return this;
	}
}