package org.ndx.codingame.greatescape.entities;

import org.ndx.codingame.gaming.ToUnitTest;

public interface GameElement extends ToUnitTest {

	GameElement NOTHING = Nothing.instance;

	public <Returned> Returned accept(GameElementVisitor<Returned> visitor);
}
