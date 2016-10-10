package org.ndx.codingame.lib2d;

public class Algebra {

	public static double[] solutionsOf(double A, double B, double C) {
		double delta = Math.sqrt(B*B-4*A*C);
		
		return new double[] {(-B+delta)/(2*A), (-B-delta)/(2*A)};
	}

}
