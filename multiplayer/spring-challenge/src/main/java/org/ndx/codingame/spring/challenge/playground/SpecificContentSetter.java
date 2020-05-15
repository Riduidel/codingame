package org.ndx.codingame.spring.challenge.playground;

import org.ndx.codingame.lib2d.discrete.DiscretePoint;
import org.ndx.codingame.spring.challenge.entities.AbstractPac;
import org.ndx.codingame.spring.challenge.entities.BigPill;
import org.ndx.codingame.spring.challenge.entities.Content;
import org.ndx.codingame.spring.challenge.entities.ContentAdapter;
import org.ndx.codingame.spring.challenge.entities.Ground;
import org.ndx.codingame.spring.challenge.entities.Pac;
import org.ndx.codingame.spring.challenge.entities.SmallPill;

public class SpecificContentSetter extends ContentAdapter<Void> {
	private final SpringPlayfield playfield;
	private final Content previous;
	private final DiscretePoint location;
	private final Content next;

	SpecificContentSetter(SpringPlayfield playfield, Content previous, DiscretePoint location, Content next) {
		super(null);
		this.playfield = playfield;
		this.previous = previous;
		this.location = location;
		this.next = next;
	}

	@Override
	public Void visitBigPill(BigPill bigPill) {
		this.playfield.getBigPills().add((BigPill) next);
		return super.visitBigPill(bigPill);
	}

	@Override
	public Void visitPac(Pac pac) {
		this.playfield.getAllPacs().add((Pac) next);
		return super.visitPac(pac);
	}

	@Override
	public Void visitSmallPill(SmallPill smallPill) {
		this.playfield.getSmallPills().add((SmallPill) next);
		return super.visitSmallPill(smallPill);
	}

	@Override
	public Void visitGround(Ground ground) {
		if (previous instanceof BigPill) {
			this.playfield.getBigPills().remove((BigPill) previous);
		} else if (next instanceof SmallPill) {
			this.playfield.getSmallPills().remove((BigPill) previous);
		} else if (next instanceof Pac) {
			this.playfield.getSmallPills().remove((AbstractPac) previous);
		}
		return super.visitGround(ground);
	}
}