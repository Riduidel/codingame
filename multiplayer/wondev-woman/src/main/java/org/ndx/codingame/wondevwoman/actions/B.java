package org.ndx.codingame.wondevwoman.actions;

import org.ndx.codingame.lib2d.discrete.Direction;
import org.ndx.codingame.wondevwoman.entities.Gamer;

public class B {
	public class MoveBuildString extends BuildString<Move> {

		public MoveBuildString(final String direction) {
			super(direction);
		}

		@Override
		public Move b(final String dir2) {
			return new Move(index, direction, dir2);
		}

	}

	public class PushBuildString extends BuildString<Push> {

		public PushBuildString(final String direction) {
			super(direction);
		}

		@Override
		public Push b(final String dir2) {
			return new Push(index, direction, dir2);
		}

	}

	public class MoveBuildDirection extends BuildDirection<Move> {

		public MoveBuildDirection(final Direction direction) {
			super(direction);
		}

		@Override
		public Move b(final Direction dir2) {
			return new Move(index, direction, dir2);
		}

	}

	public class PushBuildDirection extends BuildDirection<Push> {

		public PushBuildDirection(final Direction direction) {
			super(direction);
		}

		@Override
		public Push b(final Direction dir2) {
			return new Push(index, direction, dir2);
		}

	}

	private final int index;

	public B(final int index) {
		this.index = index;
	}

	public static B g(final int index) {
		return new B(index);
	}

	public MoveBuildString m(final String dir1) {
		return new MoveBuildString(dir1);
	}

	public PushBuildString p(final String dir1) {
		return new PushBuildString(dir1);
	}

	public MoveBuildDirection m(final Direction dir1) {
		return new MoveBuildDirection(dir1);
	}

	public PushBuildDirection p(final Direction dir1) {
		return new PushBuildDirection(dir1);
	}

	public static B g(final Gamer gamer) {
		return g(gamer.index);
	}

}
