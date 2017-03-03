package org.ndx.codingame.ghostinthecell.entities;

public class TransportDeposit extends Attack {
	public final Transport remaining;
	
	public final boolean bombing;

	public TransportDeposit(final int owner, final int count, final boolean bombing, final Transport remaining) {
		super(owner, count);
		this.remaining = remaining;
		this.bombing = bombing;
	}
}