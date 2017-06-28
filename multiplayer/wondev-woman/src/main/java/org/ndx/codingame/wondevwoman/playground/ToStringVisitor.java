package org.ndx.codingame.wondevwoman.playground;

import org.ndx.codingame.gaming.tounittest.ToUnitTestHelpers;
import org.ndx.codingame.lib2d.discrete.PlaygroundAdapter;
import org.ndx.codingame.wondevwoman.entities.Content;
import org.ndx.codingame.wondevwoman.entities.ContentVisitor;
import org.ndx.codingame.wondevwoman.entities.Floor;
import org.ndx.codingame.wondevwoman.entities.Hole;

public class ToStringVisitor extends PlaygroundAdapter<StringBuilder, Content> implements ContentVisitor<Character> {
	/**
	 *
	 */
	private final Playfield playfield;

	ToStringVisitor(final Playfield playfield) {
		super(new StringBuilder());
		this.playfield = playfield;
	}

	@Override
	public void endVisitRow(final int y) {
		returned.append("\"");
		if(y<playfield.height-1) {
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