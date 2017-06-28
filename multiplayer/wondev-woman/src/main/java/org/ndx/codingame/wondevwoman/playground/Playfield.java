package org.ndx.codingame.wondevwoman.playground;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.lib2d.discrete.Playground;
import org.ndx.codingame.wondevwoman.entities.Content;
import org.ndx.codingame.wondevwoman.entities.ContentVisitor;
import org.ndx.codingame.wondevwoman.entities.Floor;
import org.ndx.codingame.wondevwoman.entities.Gamer;
import org.ndx.codingame.wondevwoman.entities.Hole;

public class Playfield extends Playground<Content> {
	private static class ToDebugString extends ToStringVisitor {

		private final Map<DiscretePoint, Gamer> enemy;
		private final Map<DiscretePoint, Gamer> my;

		public ToDebugString(final Playfield playfield, final Map<DiscretePoint, Gamer> my, final Map<DiscretePoint, Gamer> enemy) {
			super(playfield);
			this.my = my;
			this.enemy = enemy;
		}

		@Override
		public void visit(final int x, final int y, final Content content) {
			returned.append("|");
			final DiscretePoint point = new DiscretePoint(x, y);
			if(my.containsKey(point)) {
				returned.append(my.get(point).index);
			} else {
				returned.append('_');
			}
			returned.append('_');
			super.visit(x, y, content);
			returned.append("_");
			if(enemy.containsKey(point)) {
				returned.append(enemy.get(point).index);
			} else {
				returned.append('_');
			}
		}
	}

	public static Playfield from(final List<String> rows) {
		Playfield playfield = null;
		for (int i = 0; i < rows.size(); i++) {
			final String row = rows.get(i);
			if (i == 0) {
				playfield = new Playfield(row.length(), rows.size());
			}
			final byte[] bytes = row.getBytes();
			for (int j = 0; j < bytes.length; j++) {
				final byte heightByte = bytes[j];
				if (heightByte == '.') {
					playfield.set(j, i, Hole.instance);
				} else {
					playfield.set(j, i, Floor.heightToFloor(heightByte));
				}
			}
		}
		return playfield;
	}

	public static Playfield from(final String...rows) {
		return from(Arrays.asList(rows));
	}

	public Playfield(final int width, final int height) {
		super(width, height);
	}

	/**
	 * Copy is possible since everything is final : it is not possible to change a floor height,
	 * I'll have to create a new one, and this is cool !
	 * @param playfield
	 */
	public Playfield(final Playfield playfield) {
		super(playfield);
	}

	@Override
	public boolean contains(final DiscretePoint point) {
		if(super.contains(point)) {
			return get(point).accept(new ContentVisitor<Boolean>() {

				@Override
				public Boolean visitFloor(final Floor floor) {
					return true;
				}

				@Override
				public Boolean visitHole(final Hole hole) {
					return false;
				}

			});
		} else {
			return false;
		}
	}

	public int getHeightOf(final Gamer gamer) {
		return getHeightOf(gamer.position);
	}

	public int getHeightOf(final DiscretePoint position) {
		return get(position).getHeight();
	}

	@Override
	public String toString() {
		return accept(new ToStringVisitor(this)).toString();
	}

	public String toDebugString(final Map<DiscretePoint, Gamer> my, final Map<DiscretePoint, Gamer> enemy) {
		return accept(new ToDebugString(this, my, enemy)).toString();
	}

}
