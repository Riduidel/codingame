package org.ndx.codingame.wondevwoman.actions;

import org.ndx.codingame.lib2d.discrete.Direction;

public abstract class BuildDirection<Type extends WonderAction> {
	public final Direction direction;

	public BuildDirection(final Direction direction) {
		super();
		this.direction = direction;
	}
	public abstract Type b(final Direction dir2);
}