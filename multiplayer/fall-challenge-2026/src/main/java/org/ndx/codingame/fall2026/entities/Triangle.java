package org.ndx.codingame.fall2026.entities;

import org.ndx.codingame.gaming.tounittest.ConstructableInUnitTest;

public class Triangle implements ConstructableInUnitTest{

	public final int house1;
	public final int house2;
	public final int house3;
	public final int owner;
	public final boolean meCanCapture;
	public final boolean opponentCanCapture;

	public Triangle(int house1, int house2, int house3, int owner, boolean meCanCapture, boolean opponentCanCapture) {
		this.house1 = house1;
		this.house2 = house2;
		this.house3 = house3;
		this.owner = owner;
		this.meCanCapture = meCanCapture;
		this.opponentCanCapture = opponentCanCapture;
	}

	@Override
	public StringBuilder toUnitTestConstructor(String multilinePrefix) {
		return new StringBuilder("new ").append(getClass().getSimpleName()).append("(")
				.append(this.house1).append(", ")
				.append(this.house2).append(", ")
				.append(this.house3).append(", ")
				.append(this.owner).append(", ")
				.append(this.meCanCapture).append(", ")
				.append(this.opponentCanCapture).append(")");
	}

}
