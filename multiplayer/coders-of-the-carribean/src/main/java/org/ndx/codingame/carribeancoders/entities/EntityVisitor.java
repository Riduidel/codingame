package org.ndx.codingame.carribeancoders.entities;

public interface EntityVisitor<Type> {

	Type visitBarrel(Barrel barrel);

	Type visitCannonball(Cannonball cannonball);

	Type visitMine(Mine mine);

	Type visitShip(Ship ship);

}
