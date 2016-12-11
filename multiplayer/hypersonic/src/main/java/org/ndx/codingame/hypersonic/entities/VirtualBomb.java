package org.ndx.codingame.hypersonic.entities;

/**
 * Ca exemplose comme une bombe, mais on peut marcher dedans
 * @author ndelsaux
 *
 */
public class VirtualBomb extends Bomb {

	public VirtualBomb(final int owner, final int x, final int y, final int delay, final int range) {
		super(owner, x, y, delay, range);
	}

	@Override
	public <Type> Type accept(final ContentVisitor<Type> visitor) {
		return visitor.visitVirtualBomb(this);
	}
	
	@Override
	public boolean canBeWalkedOn() {
		return true;
	}
}
