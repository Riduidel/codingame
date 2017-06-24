package org.ndx.codingame.wondevwoman.actions;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.ndx.codingame.gaming.actions.Action;
import org.ndx.codingame.gaming.tounittest.ConstructableInUnitTest;

public class Dual implements ConstructableInUnitTest, Action {
	private static final String CLASS_NAME = Dual.class.getSimpleName();

	public final Collection<String> actions;
	public final int playerIndex;
	public final String dir1;
	public final String dir2;

	public Dual(final String atype, final int index, final String dir1, final String dir2) {
		actions = Arrays.asList(atype.split("&"));
		playerIndex = index;
		this.dir1 = dir1;
		this.dir2 = dir2;
	}

	@Override
	public String toCommandString() {
		return String.format("%s %d %s %s", actionsToString(), playerIndex, dir1, dir2);
	}

	private String actionsToString() {
		return actions.stream().collect(Collectors.joining("&"));
	}

	@Override
	public String toString() {
		return toCommandString();
	}

	@Override
	public StringBuilder toUnitTestConstructor(final String multilinePrefix) {
		return new StringBuilder("new ").append(CLASS_NAME).append("(")
				.append("\"").append(actionsToString()).append("\", ")
				.append(playerIndex).append(", ")
				.append("\"").append(dir1).append("\"").append(", ")
				.append("\"").append(dir2).append("\"")
				;
	}
}
