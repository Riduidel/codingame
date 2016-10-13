package org.ndx.codingame.hypersonic;

public interface ContentVisitor<Type> {

	Type visitNothing(Nothing nothing);

	Type visitBox(Box box);

	Type visitWall(Wall wall);

	Type visitGamer(Gamer gamer);

	Type visitBomb(Bomb bomb);

	Type visitItem(Item item);

	Type visitFire(Fire fire);

	Type visitFireThenItem(FireThenItem fireThenItem);

	Type visitBombDanger(BombDanger bombDanger);
}