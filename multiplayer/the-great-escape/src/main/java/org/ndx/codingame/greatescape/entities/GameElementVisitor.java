package org.ndx.codingame.greatescape.entities;

public interface GameElementVisitor<Returned> {

	Returned visitGamer(Gamer gamer);

	Returned visitNothing(Nothing nothing);

	Returned visitWall(Wall wall);

}
