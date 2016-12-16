package org.ndx.codingame.lib2d;

public class Algebra {

	public static double[] solutionsOf(final double A, final double B, final double C) {
		final double delta = Math.sqrt(B*B-4*A*C);
		if(delta>0) {
			return new double[] {(-B+delta)/(2*A), (-B-delta)/(2*A)};
		} else if(delta==0) {
			return new double[] {-B/(2*A)};
		} else {
			return new double[] {};
		}
	}

	public static boolean isZero(final double b) {
		return b<Algebra.ZERO && b>-Algebra.ZERO;
	}

	public static boolean isEquals(final double y0, final double y1) {
		return isZero(y0-y1);
	}

	public static final double ZERO = 0.00001;

}
