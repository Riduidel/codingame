package org.ndx.codingame.lib2d;

public class Rectangle {
	public final double top;
	public final double bottom;
	public final double left;
	public final double right;
	public Rectangle(double top, double bottom, double left, double right) {
		super();
		this.top = top;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
	}
	@Override
	public String toString() {
		return "Rectangle [top=" + top + ", bottom=" + bottom + ", left=" + left + ", right=" + right + "]";
	}
}
