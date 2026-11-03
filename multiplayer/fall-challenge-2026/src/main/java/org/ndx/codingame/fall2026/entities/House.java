package org.ndx.codingame.fall2026.entities;

import org.ndx.codingame.gaming.tounittest.ConstructableInUnitTest;
import org.ndx.codingame.lib2d.discrete.DiscretePoint;

public class House  implements ConstructableInUnitTest {

	public final DiscretePoint position;
	public final int id;
	private Units units;

	public House(int houseX, int houseY, int houseId) {
		this.position = new DiscretePoint(houseX, houseY);
		this.id = houseId;
	}

	@Override
	public StringBuilder toUnitTestConstructor(String multilinePrefix) {

		StringBuilder returned = new StringBuilder("new ").append(getClass().getSimpleName()).append("(")
					.append(position.x).append(", ")
					.append(position.y).append(", ")
					;
		returned
			.append(id)
			;
		if(units!=null) {
			returned.append(", ").append(units.toUnitTestConstructor(multilinePrefix));
		}
		returned.append(')');
		return returned;
	}

	public House advance() {
		return new House(position.x, position.y, id);
	}

	public void setUnits(int myUnits, int opponentUnits) {
		this.units = new Units(myUnits, opponentUnits);
	}

}
