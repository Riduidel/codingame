package org.ndx.codingame.carribeancoders.playground;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.ndx.codingame.carribeancoders.entities.Barrel;
import org.ndx.codingame.carribeancoders.entities.Entity;
import org.ndx.codingame.carribeancoders.entities.Ship;
import org.ndx.codingame.gaming.actions.Action;
import org.ndx.codingame.gaming.tounittest.ToUnitTestFiller;
import org.ndx.codingame.gaming.tounittest.ToUnitTestHelpers;

public class Playfield implements ToUnitTestFiller {
	private final Collection<Entity> entities = new ArrayList<>();

	private final GraphStorage storage = new GraphStorage();

	public Collection<Ship> getEnemyShips() {
		return getShipsOf(0);
	}
	private Collection<Ship> getShipsOf(final int i) {
		return entities.stream()
				.filter((e) -> e instanceof Ship)
				.map((e) -> (Ship) e)
				.filter((b) -> b.owner==i)
				.collect(Collectors.toList());
	}
	public Collection<Ship> getMyShips() {
		return getShipsOf(1);
	}
	
	public Collection<Barrel> getBarrels() {
		return entities.stream()
				.filter((e) -> e instanceof Barrel)
				.map((e) -> (Barrel) e)
				.collect(Collectors.toList());
		
	}

	public void add(final Entity entity) {
		entities.add(entity);
	}


	@Override
	public StringBuilder build() {
		final StringBuilder returned = new StringBuilder();
		returned.append(ToUnitTestHelpers.declaredFilledContainer(ToUnitTestHelpers.CONTENT_PREFIX, entities, List.class, Entity.class, "entities"));
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("Playfield playfield = new Playfield();\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("playfield.addAllEntities(entities);\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("assertThat(playfield.computeMoves()).isNotEmpty();\n");
		return returned;
	}

	public Collection<Action> computeMoves() {
        return getMyShips().stream()
	    	.map((b) -> b.computeMove(this))
	    	.collect(Collectors.toList());
	}
	public String movesToCommand() {
        return computeMoves().stream()
	    	.map(Action::toCommandString)
	    	.collect(Collectors.joining("\n"));
	}

	public void addAllEntities(final List<Entity> entities2) {
		entities.addAll(entities2);
	}
}
