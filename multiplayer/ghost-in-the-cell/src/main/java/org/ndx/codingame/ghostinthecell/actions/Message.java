package org.ndx.codingame.ghostinthecell.actions;

public class Message implements Action {

	private final int enemy;
	private final int my;

	public Message(final int myProduction, final int enemyProduction) {
		my = myProduction;
		enemy = enemyProduction;
	}

	@Override
	public String toCommandString() {
		return String.format("MSG my %d/enemy %d", my, enemy);
	}

}
