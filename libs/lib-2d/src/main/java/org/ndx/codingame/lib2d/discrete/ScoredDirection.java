package org.ndx.codingame.lib2d.discrete;

public class ScoredDirection<ScoreType> extends Direction {
	private ScoreType score;

	public ScoredDirection(int x, int y, String name) {
		super(x, y, name);
	}

	public ScoreType getScore() {
		return score;
	}

	public void setScore(ScoreType score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "ScoredDirection [x=" + x + ", y=" + y + ", name=" + name + ", score=" + score + "]";
	}

	public ScoredDirection<ScoreType> withScore(ScoreType score) {
		setScore(score);
		return this;
	}
}
