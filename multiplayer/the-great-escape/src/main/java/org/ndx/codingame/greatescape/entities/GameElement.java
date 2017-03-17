package org.ndx.codingame.greatescape.entities;

import org.ndx.codingame.gaming.tounittest.ConstructableInUnitTest;

public interface GameElement extends ConstructableInUnitTest {

	GameElement NOTHING = Nothing.instance;

	public <Returned> Returned accept(GameElementVisitor<Returned> visitor);
}
