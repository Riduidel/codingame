package org.ndx.codingame.libgaming2d;

import org.ndx.codingame.gaming.actions.Action;
import org.ndx.codingame.lib2d.base.AbstractPoint;

public class MoveToPoint implements Action {
	String message;
	AbstractPoint target;

	public MoveToPoint(final AbstractPoint target) {
		this(target, "");
	}

	public MoveToPoint(final AbstractPoint target, final String message) {
		super();
		this.target = target;
		this.message = message;
	}
	@Override
	public String toCommandString() {
		if(message==null || message.isEmpty()) {
			return String.format("%d %d", (int) target.getX(), (int) target.getY());
		} else {
			return String.format("%d %d %s", (int) target.getX(), (int) target.getY(), message);
		}
	}

}
