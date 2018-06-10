package org.ndx.codingame.fantastic.actions.spells;

import java.util.Collection;
import java.util.Optional;

import org.ndx.codingame.fantastic.Constants;
import org.ndx.codingame.fantastic.actions.Action;
import org.ndx.codingame.fantastic.entities.Entities;
import org.ndx.codingame.fantastic.entities.Entity;
import org.ndx.codingame.fantastic.entities.Wizard;
import org.ndx.codingame.fantastic.status.FantasticStatus;
import org.ndx.codingame.fantastic.status.TeamStatus;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.lib2d.shapes.Segment;
import org.ndx.codingame.lib2d.shapes.Vector;

public class PetrificusSpellBuilder extends AbstractSpellBuilder<Entity> {

	public PetrificusSpellBuilder() {
		super(Constants.MAGIC_PETRIFICUS_COST);
	}

	public Collection<? extends Action> testOn(final Entities entities, final FantasticStatus status, final Wizard wizard, final Entity bludger) {
		return testOn(entities, status, wizard, bludger, new PetrificusStatus());
	}

	@Override
	protected Optional<Action> doTestOn(final Entities entities, final FantasticStatus status, final Wizard wizard, final Entity entity) {
		final Segment defended = status.get(TeamStatus.class).getDefended();
		final Vector extended = new Vector(entity.position,
				new ContinuousPoint(entity.position.x+entity.speed.x*5, entity.position.y+entity.speed.y*5));
		if(extended.intersectsWith(defended)) {
			return Optional.of(new PetrificusSpell(defended, entity));
		}
		return Optional.empty();
	}

}
