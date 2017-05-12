package org.ndx.codingame.libgaming2d;

import org.ndx.codingame.gaming.actions.AbstractAction;
import org.ndx.codingame.gaming.actions.Action;
import org.ndx.codingame.lib2d.base.AbstractPoint;

public class MoveToPoint extends AbstractAction implements Action {
	public final AbstractPoint target;
	public final String message;

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

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("MoveToPoint [target=");
		builder.append(target);
		if(message!=null) {
			builder.append(", message=");
			builder.append(message);
		}
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (target == null ? 0 : target.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final MoveToPoint other = (MoveToPoint) obj;
		if (target == null) {
			if (other.target != null) {
				return false;
			}
		} else if (!target.equals(other.target)) {
			return false;
		}
		return true;
	}

}
