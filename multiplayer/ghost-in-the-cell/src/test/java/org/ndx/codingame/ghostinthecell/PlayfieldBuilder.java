package org.ndx.codingame.ghostinthecell;

import org.ndx.codingame.ghostinthecell.playground.Playfield;

public class PlayfieldBuilder extends Playfield {
	public class PlayfieldEdgeListBuilder {
		public class PlayfieldEdgeBuilder {

			private final int to;

			public PlayfieldEdgeBuilder(final int to) {
				this.to = to;
			}

			public PlayfieldEdgeListBuilder d(final int distance) {
				connect(from, to, distance);
				return PlayfieldEdgeListBuilder.this;
			}


			public Playfield build() {
				return PlayfieldEdgeListBuilder.this.build();
			}
		}

		private final int from;

		public PlayfieldEdgeListBuilder(final int from) {
			this.from = from;
		}

		public PlayfieldEdgeBuilder t(final int to) {
			return new PlayfieldEdgeBuilder(to);
		}

		public PlayfieldBuilder i(final int factoryId, final int owner, final int cyborgs, final int production) {
			return PlayfieldBuilder.this.i(factoryId, owner, cyborgs, production);
		}

		public PlayfieldBuilder t(final int from, final int to, final int owner, final int count, final int distance) {
			return PlayfieldBuilder.this.t(from, to, owner, count, distance);
		}

		public PlayfieldBuilder e(final int i) {
			return PlayfieldBuilder.this.e(i);
		}

		public PlayfieldBuilder b(final int to, final int from, final int distance) {
			return PlayfieldBuilder.this.b(from, to, distance);
		}

		public Playfield build() {
			return PlayfieldBuilder.this.build();
		}
	}

	public PlayfieldBuilder i(final int factoryId, final int owner, final int cyborgs, final int production) {
		setFactoryInfos(factoryId, owner, cyborgs, production);
		return this;
	}

	public Playfield build() {
		return this;
	}

	public PlayfieldBuilder b(final int from, final int to, final int distance) {
		setMyBomb(from, to, distance);
		return this;
	}

	public PlayfieldEdgeListBuilder f(final int from) {
		return new PlayfieldEdgeListBuilder(from);
	}

	public PlayfieldBuilder t(final int from, final int to, final int owner, final int count, final int distance) {
		setTroop(from, to, owner, count, distance);
		return this;
	}

	public PlayfieldBuilder e(final int i) {
		setEnemyBomb(i);
		return this;
	}

	public PlayfieldBuilder at(final int i) {
		setTurn(i);
		return this;
	}
}
