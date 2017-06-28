package org.ndx.codingame.wondevwoman.actions;

public abstract class BuildString<Type extends WonderAction> {
	public final String direction;

	public BuildString(final String direction) {
		super();
		this.direction = direction;
	}
	public abstract Type b(final String dir2);
}