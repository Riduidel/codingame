package org.ndx.codingame.wondevwoman.actions;

import org.ndx.codingame.gaming.actions.AbstractAction;
import org.ndx.codingame.gaming.tounittest.ConstructableInUnitTest;
import org.ndx.codingame.lib2d.discrete.Direction;

public abstract class WonderAction extends AbstractAction implements ConstructableInUnitTest {

	private final String command;

	private final String unitTestConstructor;

	public final int gamerIndex;

	private String fullCommandString;

	private final Direction first;

	private final Direction build;

	public WonderAction(final String commandString, final String unitTestConstructor, final int gamerIndex, final Direction direction1, final Direction direction2) {
		this.gamerIndex = gamerIndex;
		first = direction1;
		build = direction2;
		command = commandString;
		this.unitTestConstructor = unitTestConstructor;
	}

	public WonderAction(final String commandString, final String unitTestConstructor, final int gamerIndex, final String direction1, final String direction2) {
		this(commandString, unitTestConstructor, gamerIndex,
				DirectionMapping.NAMES_TO_DIRECTIONS.get(direction1),
				DirectionMapping.NAMES_TO_DIRECTIONS.get(direction2));
	}

	@Override
	public String toCommandString() {
		if(fullCommandString==null) {
			fullCommandString = String.format("%s %d %s %s", command, gamerIndex,
					getFirstDirectionName(),
					getBuildDirectionName());
		}
		return fullCommandString;
	}

	private String getBuildDirectionName() {
		return DirectionMapping.DIRECTIONS_TO_NAMES.get(build);
	}

	private String getFirstDirectionName() {
		return DirectionMapping.DIRECTIONS_TO_NAMES.get(first);
	}

	@Override
	public StringBuilder toUnitTestConstructor(final String multilinePrefix) {
		return new StringBuilder("B.")
				.append("g(").append(gamerIndex).append(")")
				.append(".").append(unitTestConstructor).append("(\"").append(getFirstDirectionName()).append("\")")
				.append(".b(\"").append(getBuildDirectionName()).append("\")")
				;
	}

	@Override
	public String toString() {
		return toCommandString();
	}

	public abstract <Type> Type accept(WonderActionVisitor<Type> wonderActionVisitor);


	public Direction getBuild() {
		return build;
	}

	protected Direction getFirstDirection() {
		return first;
	}

}
