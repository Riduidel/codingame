package org.ndx.codingame.wondevwoman.playground;

import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.wondevwoman.entities.Content;

public class Playfield extends Playground<Content> {

	public Playfield(final int width, final int height, final Content defaultValue) {
		super(width, height, defaultValue);
	}

	public Playfield(final int width, final int height) {
		super(width, height);
	}

	public Playfield(final Playground<Content> playground) {
		super(playground);
	}
}