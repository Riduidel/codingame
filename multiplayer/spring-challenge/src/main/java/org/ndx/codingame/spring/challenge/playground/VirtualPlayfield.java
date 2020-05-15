package org.ndx.codingame.spring.challenge.playground;

import java.util.ArrayList;
import java.util.List;

import org.ndx.codingame.lib2d.discrete.MutableProxy;
import org.ndx.codingame.spring.challenge.entities.BigPill;
import org.ndx.codingame.spring.challenge.entities.Content;
import org.ndx.codingame.spring.challenge.entities.SmallPill;

public class VirtualPlayfield extends MutableProxy<Content> {

	private List<SmallPill> smallPills;
	private List<BigPill> bigPills;

	public VirtualPlayfield(Playfield playfield) {
		super(playfield);
		this.bigPills = new ArrayList<>(playfield.bigPills);
		this.smallPills = new ArrayList<>(playfield.smallPills);
	}

}
