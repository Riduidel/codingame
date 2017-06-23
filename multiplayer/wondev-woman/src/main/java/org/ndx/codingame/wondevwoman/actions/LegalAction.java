package org.ndx.codingame.wondevwoman.actions;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class LegalAction {

	public final Collection<String> actions;
	public final int playerIndex;
	public final String dir1;
	public final String dir2;

	public LegalAction(final String atype, final int index, final String dir1, final String dir2) {
		actions = Arrays.asList(atype.split("&"));
		playerIndex = index;
		this.dir1 = dir1;
		this.dir2 = dir2;
	}

	public String toCommandString() {
		return String.format("%s %d %s %s", actions.stream().collect(Collectors.joining("&")), playerIndex, dir1, dir2);
	}

	@Override
	public String toString() {
		return toCommandString();
	}
}
