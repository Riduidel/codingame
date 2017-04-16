package org.ndx.codingame.carribeancoders.playground;

import org.ndx.codingame.libgraph.GraphProperty;

public interface Properties {
	public static enum Hexagonal {
		RIGHT() {
			@Override
			public Hexagonal opposite() {
				return Hexagonal.LEFT;
			}
		},
		TOP_RIGHT() {
			@Override
			public Hexagonal opposite() {
				return Hexagonal.BOTTOM_LEFT;
			}
		},
		TOP_LEFT() {
			@Override
			public Hexagonal opposite() {
				return Hexagonal.BOTTOM_RIGHT;
			}
		},
		LEFT() {
			@Override
			public Hexagonal opposite() {
				return Hexagonal.RIGHT;
			}
		},
		BOTTOM_LEFT() {
			@Override
			public Hexagonal opposite() {
				return Hexagonal.TOP_RIGHT;
			}
		},
		BOTTOM_RIGHT() {
			@Override
			public Hexagonal opposite() {
				return TOP_LEFT;
			}
		};
		
		public Hexagonal opposite() {
			throw new UnsupportedOperationException("Should be implemented in specific values");
		}
	}

	GraphProperty<Hexagonal> DIRECTION = new GraphProperty<>("DIRECTION");
}
