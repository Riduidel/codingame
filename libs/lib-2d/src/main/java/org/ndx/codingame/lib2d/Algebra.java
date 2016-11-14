package org.ndx.codingame.lib2d;

public class Algebra {

	public static double[] solutionsOf(double A, double B, double C) {
		double delta = Math.sqrt(B*B-4*A*C);
		if(delta>0)
			return new double[] {(-B+delta)/(2*A), (-B-delta)/(2*A)};
		else if(delta==0)
			return new double[] {(-B)/(2*A)};
		else
			return new double[] {};
	}

	public static boolean isZero(double b) {
		return Math.abs(b)<Algebra.ZERO;
	}

	public static boolean isEquals(double y0, double y1) {
		return isZero(y0-y1);
	}

	public static final double ZERO = 0.00001;

}
