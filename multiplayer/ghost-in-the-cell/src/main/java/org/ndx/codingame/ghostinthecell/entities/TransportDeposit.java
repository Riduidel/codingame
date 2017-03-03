package org.ndx.codingame.ghostinthecell.entities;

public class TransportDeposit extends Attack {
	public final Transport remaining;

	public TransportDeposit(final int owner, final int count, final Transport remaining) {
		super(owner, count);
		this.remaining = remaining;
	}
}