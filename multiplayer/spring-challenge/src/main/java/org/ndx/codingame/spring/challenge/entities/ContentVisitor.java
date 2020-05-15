package org.ndx.codingame.spring.challenge.entities;

public interface ContentVisitor<Type> {

	Type visitNothing(Nothing nothing);

	Type visitGround(Ground ground);
	
	Type visitWall(Wall wall);
	
	Type visitBigPill(BigPill bigPill);
	
	Type visitSmallPill(SmallPill smallPill);
	
	Type visitPac(Pac pac);

	Type visitPotentialSmallPill(PotentialSmallPill potentialSmallPill);

	Type visitPacTrace(PacTrace pacTrace);

	Type visitVirtualPac(VirtualPac virtualPac);
}
