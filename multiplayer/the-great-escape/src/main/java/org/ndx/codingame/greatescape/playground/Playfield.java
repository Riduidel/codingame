package org.ndx.codingame.greatescape.playground;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.ndx.codingame.gaming.ToUnitTest;
import org.ndx.codingame.greatescape.entities.GameElement;
import org.ndx.codingame.greatescape.entities.GameElementAdapter;
import org.ndx.codingame.greatescape.entities.GameElementVisitor;
import org.ndx.codingame.greatescape.entities.Gamer;
import org.ndx.codingame.greatescape.entities.Nothing;
import org.ndx.codingame.greatescape.entities.Orientation;
import org.ndx.codingame.greatescape.entities.Wall;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.lib2d.discrete.PlaygroundAdapter;

public class Playfield extends Playground<GameElement> {
	private final Collection<Gamer> gamers = new ArrayList<>();
	public final int visibleHeight;
	public final int visibleWidth;

	public Playfield(final int width, final int height) {
		super(width*2+1, height*2+1, GameElement.NOTHING);
		visibleWidth = width;
		visibleHeight = height;
	}

	public Collection<DiscretePoint> toPlayfieldPosition(final int visibleX, final int visibleY, final GameElement element) {
		return element.accept(new GameElementAdapter<Collection<DiscretePoint>>() {
			@Override
			protected Collection<DiscretePoint> defaultVisit(final GameElement nothing) {
				return Arrays.asList(toPlayfieldPositionForContent(visibleX, visibleY));
			}

			@Override
			public Collection<DiscretePoint> visitWall(final Wall wall) {
				final DiscretePoint source = toPlayfieldPositionForWall(visibleX, visibleY, wall.direction);
				return IntStream.range(0, 2)
					.mapToObj((increment) -> {
						switch(wall.direction) {
						case H:
							return new DiscretePoint(source.x+increment, source.y);
						case V:
							return new DiscretePoint(source.x, source.y+increment);
						default:
							throw new UnsupportedOperationException("Une direction inconnue ?");
						}
					})
					.collect(Collectors.toList());
			}

		});
	}
	public DiscretePoint toPlayfieldPositionForContent(final int visibleX, final int visibleY) {
		return new DiscretePoint(2*visibleX, 2*visibleY);
	}
	
	public DiscretePoint toPlayfieldPositionForWall(final int visibleX, final int visibleY, final Orientation direction) {
		switch(direction) {
		case H:
			return new DiscretePoint(2*visibleX, 2*visibleY+1);
		case V:
			return new DiscretePoint(2*visibleX+1, 2*visibleY);
		default:
			throw new UnsupportedOperationException("Une direction inconnue ?");
		}
	}
	/**
	 * Method used in Player class to put elements at their valid location
	 * @param visibleX
	 * @param visibleY
	 * @param gamer
	 */
	public void setAt(final int visibleX, final int visibleY, final GameElement element) {
		final Collection<DiscretePoint> position = toPlayfieldPosition(visibleX, visibleY, element);
		position.stream().forEach((p) -> Playfield.this.set(p, element));
	}
	
	@Override
	public void set(final int x, final int y, final GameElement c) {
		c.accept(new GameElementAdapter<Void>() {
			@Override
			public Void visitGamer(final Gamer gamer) {
				gamers.add(gamer);
				return super.visitGamer(gamer);
			}
		});
		super.set(x, y, c);
	}
	
	public static class ExportGameEntities extends PlaygroundAdapter<StringBuilder, GameElement> implements GameElementVisitor<Optional<StringBuilder>> {
		private final Gamer me;

		public ExportGameEntities(final Gamer me) {
			this.me = me;
		}
		@Override
		public void startVisit(final Playground<GameElement> playground) {
			returned = new StringBuilder();
			super.startVisit(playground);
		}
		@Override
		public void visit(final int x, final int y, final GameElement content) {
			final StringBuilder output = new StringBuilder();
			final Optional<StringBuilder> elementOutput = content.accept(this);
			elementOutput.ifPresent((s) -> 
				returned.append(ToUnitTest.CONTENT_PREFIX).append("tested.set(").append(x).append(", ").append(y).append(", ").append(s).append(");\n")
					);
		}

		@Override
		public Optional<StringBuilder> visitGamer(final Gamer gamer) {
			final StringBuilder used = new StringBuilder();
			if(gamer==me) {
				used.append("me = ");
			}
			used.append(gamer.toUnitTestConstructor(""));
			return Optional.of(used);
		}

		@Override
		public Optional<StringBuilder> visitNothing(final Nothing nothing) {
			return Optional.ofNullable(null);
		}

		@Override
		public Optional<StringBuilder> visitWall(final Wall wall) {
			return Optional.of(wall.toUnitTestConstructor(""));
		}
	}
	public static class ToDebugString extends PlaygroundAdapter<StringBuilder, GameElement> implements GameElementVisitor<StringBuilder> {
		@Override
		public void startVisit(final Playground<GameElement> playground) {
			returned = new StringBuilder();
			super.startVisit(playground);
		}
		
		@Override
		public void visit(final int x, final int y, final GameElement content) {
			returned.append(content.accept(this));
		}
		
		@Override
		public void endVisitRow(final int y) {
			returned.append("\n");
		}

		@Override
		public StringBuilder visitGamer(final Gamer gamer) {
			return new StringBuilder("G");
		}

		@Override
		public StringBuilder visitNothing(final Nothing nothing) {
			return new StringBuilder(" ");
		}

		@Override
		public StringBuilder visitWall(final Wall wall) {
			switch(wall.direction) {
			case H:
				return new StringBuilder("-");
			case V:
				return new StringBuilder("|");
			default:
				throw new UnsupportedOperationException("Une direction inconnue ?");
			}
		}
		
	}
	
	public String toDebugString() {
		return accept(new ToDebugString()).toString();
	}

	public String toUnitTestString(final Gamer me) {
		final StringBuilder returned = new StringBuilder();
		
		returned.append(ToUnitTest.METHOD_PREFIX+"// @PerfTest(invocations = INVOCATION_COUNT, threads = THREAD_COUNT) @Required(percentile99=PERCENTILE)\n");
		returned.append(ToUnitTest.METHOD_PREFIX+"@Test public void can_find_move_")
			.append(System.currentTimeMillis()).append("() {\n");
		returned.append(ToUnitTest.CONTENT_PREFIX+"Gamer me = null;\n");
		returned.append(ToUnitTest.CONTENT_PREFIX+"Playfield tested = new Playfield(")
			.append(visibleWidth).append(", ")
			.append(visibleHeight)
				.append(");\n");
		returned.append(accept(new ExportGameEntities(me)));
		returned.append(ToUnitTest.CONTENT_PREFIX+"assertThat(me.compute(tested)).isNotNull();\n");
		returned.append(ToUnitTest.METHOD_PREFIX+"}\n\n");
		return returned.toString();
	}

	public Collection<Gamer> getGamers() {
		return gamers;
	}
	
	@Override
	public String toString() {
		return toDebugString();
	}
}
