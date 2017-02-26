package org.ndx.codingame.greatescape.actions;

import org.ndx.codingame.lib2d.discrete.Direction;

public class MoveTo implements Action {

	public final Direction direction;
	private String format = "%s";

	public MoveTo(final Direction direction) {
		this.direction = direction;
	}

	public MoveTo(final Direction direction, final String format) {
		this(direction);
		this.format = format;
	}

	@Override
	public Action decorateWith(final String format) {
		return new MoveTo(direction, format);
	}

	@Override
	public String toCodingame() {
		return String.format(format, direction.name);
	}

}
