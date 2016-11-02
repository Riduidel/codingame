package org.ndx.codingame.lib2d.discrete;

public class ScoredDirection extends Direction {
	private int score;

	public ScoredDirection(int x, int y, String name) {
		super(x, y, name);
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "ScoredDirection [x=" + x + ", y=" + y + ", name=" + name + ", score=" + score + "]";
	}

}
