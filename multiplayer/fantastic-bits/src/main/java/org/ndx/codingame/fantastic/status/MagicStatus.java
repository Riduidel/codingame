package org.ndx.codingame.fantastic.status;

import org.ndx.codingame.libstatus.StatusElement;

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

	@Override
	public StringBuilder toUnitTestConstructor(String multilinePrefix) {
		StringBuilder returned = new StringBuilder();
		returned.append("new MagicStatus(").append(magic).append(")");
		return returned;
	}
}