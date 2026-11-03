package org.ndx.codingame.fall2026.playground;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ndx.codingame.fall2026.entities.House;
import org.ndx.codingame.fall2026.entities.Triangle;
import org.ndx.codingame.gaming.tounittest.ToUnitTestFiller;
import org.ndx.codingame.gaming.tounittest.ToUnitTestHelpers;
import org.ndx.codingame.libgraph.UndirectedGraph;

public class Playfield implements ToUnitTestFiller {
	private UndirectedGraph connectedHouses = new UndirectedGraph();
	private UndirectedGraph connectableHouses = new UndirectedGraph();
	private final List<House> entities = new ArrayList<>();
	private List<Triangle> triangles = new ArrayList<Triangle>();

	public void add(final House entity) {
		entities.add(entity);
	}


	@Override
	public StringBuilder build(String effectiveCommand) {
		final StringBuilder returned = new StringBuilder();
		returned.append(ToUnitTestHelpers.declaredFilledContainer(ToUnitTestHelpers.CONTENT_PREFIX, entities, List.class, House.class, "entities"));
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("Playfield playfield = new Playfield();\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("playfield.addAllEntities(entities);\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("assertThat(playfield.computeMoves()).isNotEmpty();\n");
		return returned;
	}


	/**
	 * Create next turn playfield
	 * @return
	 */
	public Playfield advance() {
		Playfield returned = new Playfield();
		returned.addAllEntities(entities.stream()
				.map(e -> e.advance())
				.collect(Collectors.toList()));
		return returned;
	}

//	public Collection<Action> computeMoves() {
//        return getMyShips().stream()
//	    	.map((b) -> b.computeMove(this))
//	    	.collect(Collectors.toList());
//	}
//	public String movesToCommand() {
//        return computeMoves().stream()
//	    	.map(Action::toCommandString)
//	    	.collect(Collectors.joining("\n"));
//	}


	public void addAllEntities(final List<House> entities2) {
		entities.addAll(entities2);
	}


	public void install(int houseId, int myUnits, int opponentUnits) {
		entities.get(houseId).setUnits(myUnits, opponentUnits);
	}


	public void registerConnectedHouses(int house1, int house2) {
		connectedHouses.getOrCreateEdgeBetween(house1, house2);
	}

	public void registerConnectableHouses(int house1, int house2) {
		connectableHouses.getOrCreateEdgeBetween(house1, house2);
	}


	public void addTriangle(int house1, int house2, int house3, int owner, boolean meCanCapture,
			boolean opponentCanCapture) {
		triangles.add(new Triangle(house1, house2, house3, owner, meCanCapture, opponentCanCapture));
	}

}
