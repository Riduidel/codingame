package org.ndx.codingame.spring.challenge.entities;

public abstract class AbstractPac extends AbstractDistinctContent {

	public final boolean mine;
	public final Type type;
	public final int speedTurnsLeft;
	public final int abilityCooldown;
	public final int id;

	public AbstractPac(int x, int y, int pacId, boolean mine, Type type, int speedTurnsLeft, int abilityCooldown) {
		super(x, y);
		this.id = pacId;
		this.mine = mine;
		this.type = type;
		this.speedTurnsLeft = speedTurnsLeft;
		this.abilityCooldown = abilityCooldown;
	}

	public boolean isDangerousFor(AbstractPac pac) {
		return type.isDangerousFor(pac.type);
	}

	@Override
	public boolean canBeWalkedOnBy(AbstractPac pac) {
		if(pac==null)
			return false;
		if(pac.mine==mine) {
			return pac.id==id;
		} else {
			// Of course enemy pacs can be walked on !
			return !mine;
		}
	}
}
