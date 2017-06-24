package org.ndx.codingame.wondevwoman.playground;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.ndx.codingame.gaming.tounittest.ToUnitTestFiller;
import org.ndx.codingame.gaming.tounittest.ToUnitTestHelpers;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.lib2d.discrete.PlaygroundAdapter;
import org.ndx.codingame.wondevwoman.actions.Dual;
import org.ndx.codingame.wondevwoman.entities.Content;
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
		public void startVisitRow(final int y) {
			returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("\"");
		}

		@Override
		public void visit(final int x, final int y, final Content content) {
			returned.append(content.accept(this));
		}

		@Override
		public void endVisitRow(final int y) {
			returned.append("\"");
			if(y<height-1) {
				returned.append(" +");
			}
			returned.append("\n");
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

	private List<Gamer> my;
	private List<Gamer> enemy;
	private Collection<Dual> actions;

	public Playfield(final int width, final int height, final Content defaultValue) {
		super(width, height, defaultValue);
	}

	public Playfield(final int width, final int height) {
		super(width, height);
	}

	public Playfield(final Playground<Content> playground) {
		super(playground);
	}

	public Playfield withMy(final List<Gamer> my) {
		this.my = my;
		return this;
	}

	public Playfield withEnemy(final List<Gamer> enemy) {
		this.enemy = enemy;
		return this;
	}

	public Playfield withActions(final Collection<Dual> actions) {
		this.actions = actions;
		return this;
	}

	public String computeMoves() {
		return my.stream()
				.map(this::computeMoveOf)
				.map(Dual::toCommandString)
				.collect(Collectors.joining("\n"))
				;
	}

	public Dual computeMoveOf(final Gamer gamer) {
		return actions.stream()
				.filter((a) -> a.playerIndex==gamer.index)
				.findFirst()
				.get();
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
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append(")\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("p.withMy(my)\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("\t.withEnemy(enemy)\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("\t.withActions(actions)\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX)
		.append("assertThat(p.computeMoves()).isNotEqualTo(\"").append(effectiveCommand).append("\");\n");
		return returned;
	}

	private String playfieldToString() {
		return accept(new ToStringVisitor(new StringBuilder()))
				.toString();
	}
}