package org.ndx.codingame.spring.challenge.entities;

public class VirtualPac extends AbstractPac {
	public final Content original;

	public VirtualPac(int x, int y, int pacId, boolean mine, Type type,
			int speedTurnsLeft, int abilityCooldown,
			Content original) {
		super(x, y, pacId, mine, type, speedTurnsLeft, abilityCooldown);
		this.original = original;
	}

	@Override
	public <Type> Type accept(ContentVisitor<Type> visitor) {
		return visitor.visitVirtualPac(this);
	}

	@Override
	public boolean canBeWalkedOnBy(AbstractPac pac) {
		if(pac.id==id) {
			return original.canBeWalkedOnBy(pac);
		} else {
			return false;
		}
	}

	@Override
	public int score() {
		return 0;
	}

	@Override
	public String toString() {
		return (mine ? "V" : "Z")+id;
	}
}
