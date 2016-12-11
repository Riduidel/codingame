package org.ndx.codingame.hypersonic.entities;

/**
 * Ca exemplose comme une bombe, mais on peut marcher dedans
 * @author ndelsaux
 *
 */
public class PotentialBomb extends Bomb {

	public PotentialBomb(final int owner, final int x, final int y, final int delay, final int range) {
		super(owner, x, y, delay, range);
	}

	@Override
	public <Type> Type accept(final ContentVisitor<Type> visitor) {
		return visitor.visitPotentialBomb(this);
	}
	
	@Override
	public boolean canBeWalkedOn() {
		return true;
	}
}
