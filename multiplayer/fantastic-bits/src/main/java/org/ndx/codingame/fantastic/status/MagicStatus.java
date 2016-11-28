package org.ndx.codingame.fantastic.status;

public class MagicStatus implements StatusElement {
	int magic;

	public MagicStatus(int id) { this.magic = id; }

	@Override public void advanceOneTurn() { magic++; }

	public int getMagic() {
		return magic;
	}
	
	public void cast(int spellCost) {
		magic-=spellCost;
	}
}