package org.ndx.codingame.fantastic.entities;

import org.ndx.codingame.fantastic.Constants;

public class Wizard extends Entity {
	public final int teamId;

	public final boolean holdingSnaffle;

	private boolean attacking;

	public Wizard(final int id, final double x, final double y, final double vx, final double vy, final int teamId, final boolean holdingSnaffle) {
		super(id, x, y, vx, vy);
		this.teamId = teamId;
		this.holdingSnaffle = holdingSnaffle;
	}
	public Wizard(final int id, final double x, final double y, final double vx, final double vy, final int teamId, final boolean holdingSnaffle, final boolean attacking) {
		this(id, x, y, vx, vy, teamId, holdingSnaffle);
		this.attacking = attacking;
	}

	@Override
	public <Type> Type accept(final EntityVisitor<Type> visitor) {
		return visitor.visitWizard(this);
	}

	@Override
	public double getRadius() {
		return Constants.RADIUS_WIZARD;
	}

	@Override
	public String toString() {
		return String.format("Wizard [id=%s, teamId=%s, holdingSnaffle=%s]", id, teamId, holdingSnaffle);
	}

	public void setAttacking(final boolean b) {
		attacking= true;
	}

	public boolean isAttacking() {
		return attacking;
	}

}
