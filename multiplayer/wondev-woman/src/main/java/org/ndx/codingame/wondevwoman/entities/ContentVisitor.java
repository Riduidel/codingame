package org.ndx.codingame.wondevwoman.entities;

public interface ContentVisitor<Returned> {

	Returned visitFloor(Floor floor);

	Returned visitHole(Hole hole);

}
