package org.ndx.codingame.thaleshkt.playground;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.ndx.codingame.gaming.tounittest.ToUnitTestFiller;
import org.ndx.codingame.gaming.tounittest.ToUnitTestHelpers;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.lib2d.shapes.Vector;
import org.ndx.codingame.thaleshkt.Constants;
import org.ndx.codingame.thaleshkt.actions.Move;
import org.ndx.codingame.thaleshkt.entities.AbstractEntity;
import org.ndx.codingame.thaleshkt.entities.EntityVisitor;
import org.ndx.codingame.thaleshkt.entities.Flag;
import org.ndx.codingame.thaleshkt.entities.UFO;
import org.ndx.codingame.thaleshkt.status.CanBoost;
import org.ndx.codingame.thaleshkt.status.ThalesStatus;

public class Playfield implements ToUnitTestFiller {
	public final Team my = new Team(Participant.MY);
	public final Team adversary = new Team(Participant.ADVERSARY);
	public final ThalesStatus status;
	private List<UFO> movingEntities;
	public Playfield(ThalesStatus status) {
		this.status = status;
	}
	public String compute() {
		Collection<Move> computed = my.compute(this);
		return computed.stream()
				.map(this::updateBoost)
				.map(Move::toString)
				.collect(Collectors.joining("\n")); 

	}
	private Move updateBoost(Move m) {
		status.get(CanBoost.class).update(m);
		return m;
	}
	public Side findMySide() {
		return my.flag.position.center.x<Constants.WIDTH/2 ? Side.LEFT : Side.RIGHT;
	}
	public StringBuilder build(final String effectiveCommand) {
		final StringBuilder returned = new StringBuilder();
		returned.append(status.toUnitTestConstructor(ToUnitTestHelpers.CONTENT_PREFIX));
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("Playfield p = new Playfield(status);\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append(my.first.toUnitTestConstructor("p.my.first = ")).append(";\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append(my.second.toUnitTestConstructor("p.my.second = ")).append(";\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append(my.flag.toUnitTestConstructor("p.my.flag = ")).append(";\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append(adversary.first.toUnitTestConstructor("p.adversary.first = ")).append(";\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append(adversary.second.toUnitTestConstructor("p.adversary.second = ")).append(";\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append(adversary.flag.toUnitTestConstructor("p.adversary.flag = ")).append(";\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX)
		.append("assertThat(p.compute()).isNotEqualTo(\"")
		.append(effectiveCommand.replace(
				"\n",
				"\\n\"+\n"+ToUnitTestHelpers.CONTENT_PREFIX+"\""))
		.append("\");\n");
		return returned;
	}
	
	public AbstractEntity getFlagOwner(Participant p) {
		Team ofParticipant = p.getParticipant(this);
		Team ofAdversary = p.getAdversary(this);
		if(ofParticipant.flag.position.center.x>=0) {
			return ofParticipant.flag;
		} else {
			if(ofAdversary.first.hasFlag) {
				return ofAdversary.first;
			} else {
				return ofAdversary.second;
			}
		}
	}
	public ContinuousPoint getPositionOfFlag(Participant p) {
		return getFlagOwner(p).accept(new EntityVisitor<ContinuousPoint>() {
			@Override
			public ContinuousPoint visitFlag(Flag flag) {
				return flag.position.center;
			}
			@Override
			public ContinuousPoint visitUFO(UFO ufo) {
				return ufo.position.center;
			}
		});
	}
	
	public List<UFO> movingEntities() {
		if(movingEntities==null) {
			movingEntities = Arrays.asList(my.first, my.second, adversary.first, adversary.second);
		}
		return movingEntities;
	}
	
	/**
	 * TODO optimize the fuck out of that shit, since it's gonna be called multiple times
	 * @param move
	 * @return
	 */
	public SortedSet<Collision> computeCollisionsOf(Move move) {
		TreeSet<Collision> returned = new TreeSet<>();
		Vector moveLineOfThrust = move.moving.lineOfThrust();
		for(UFO u : movingEntities()) {
			if(u!=move.moving) {
				Vector otherLineOfThrust = u.lineOfThrust();
				Collection<ContinuousPoint> intersection = moveLineOfThrust.toLine().intersectionWith(otherLineOfThrust.toLine());
				// Potential intersection detected. But is it forward or backward ?
				if(!intersection.isEmpty()) {
					for(ContinuousPoint p : intersection) {
						double times = moveLineOfThrust.numberOfTimesTo(p);
						if(times>0) {
							if(otherLineOfThrust.numberOfTimesTo(p)>0) {
								// WE HAVE A COLLISION UPCOMING !
								// TODO take circle in account !
								returned.add(new Collision(u, (int) times));
							}
						}
					}
				}
			}
		}
		return returned;
	}
}
