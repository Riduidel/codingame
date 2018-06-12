package org.ndx.codingame.thaleshkt.playground;

public enum Participant {
	MY {
		@Override
		public Team getParticipant(Playfield playfield) {
			return playfield.my;
		}
		@Override
		public Team getAdversary(Playfield playfield) {
			return playfield.adversary;
		}
	},
	ADVERSARY {
		@Override
		public Team getParticipant(Playfield playfield) {
			return playfield.adversary;
		}
		@Override
		public Team getAdversary(Playfield playfield) {
			return playfield.my;
		}
	};

	public abstract Team getParticipant(Playfield playfield);

	public abstract Team getAdversary(Playfield playfield);
}